package org.hibate.media;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.hibate.media.codec.*;
import org.hibate.media.codec.impl.AVCodecFactoryImpl;
import org.hibate.media.codec.impl.AVPacketImpl;
import org.hibate.media.filter.*;
import org.hibate.media.filter.impl.AVFilterContextImpl;
import org.hibate.media.filter.impl.AVFilterFactoryImpl;
import org.hibate.media.filter.impl.AVFilterGraphImpl;
import org.hibate.media.filter.impl.AVFilterInOutImpl;
import org.hibate.media.format.*;
import org.hibate.media.format.impl.AVFormatImpl;
import org.hibate.media.format.impl.AVInputStreamImpl;
import org.hibate.media.format.impl.AVOutputStreamImpl;
import org.hibate.media.util.Factory;
import org.hibate.media.util.Source;
import org.hibate.media.util.*;
import org.hibate.media.util.Error;
import org.hibate.media.util.impl.AVDictionaryImpl;
import org.hibate.media.util.impl.AVFrameImpl;
import org.hibate.media.util.impl.AVOptionsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;
import java.util.function.Function;

/**
 * 转码示例
 * @author Hibate
 * Created on 2024/03/30.
 */
public class TranscodeTest {

    private static final Logger logger =
            LoggerFactory.getLogger(TranscodeTest.class);

    private File file;
    private File directory;

    private AVFormatContext ifmt_ctx = null;
    private AVFormatContext ofmt_ctx = null;
    private AVFilters[] filter_ctx = null;
    private AVContext[] stream_ctx = null;

    @BeforeEach
    public void init() {
        // TODO 请设置参数
    }

    @Test
    public void transcode() {
        if (directory != null && file != null) {
            Source source = Factory.create(directory, file);
            this.transcode(source);
        }
    }

    @SuppressWarnings("resource")
    private void transcode(@NonNull Source source) {
        AVPacket packet = null;
        try {
            if (!this.open_input_file(source.getUrl())) {
                return;
            }
            if (!this.open_output_file(source.getFile().getPath(), true)) {
                return;
            }

            AVInputStream is = new AVInputStreamImpl(this.ifmt_ctx);
            AVOutputStream os = new AVOutputStreamImpl(this.ofmt_ctx);
            // init muxer, write output file header
            if (os.write() < 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Error occurred when opening output file");
                }
                throw new RuntimeException("Error occurred when opening output file");
            }

            if (!this.init_filters()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Error occurred when init filters");
                }
                throw new RuntimeException("Error occurred when opening init filters");
            }

            packet = new AVPacketImpl();

            // read all packets
            for (;;) {
                int ret = is.read(packet);
                if (ret < 0) {
                    break;
                }

                if (this.filter_ctx[packet.getIndex()] != null) {
                    AVContext context = this.stream_ctx[packet.getIndex()];
                    if ((context == null) || (context.getDecoder() == null) || (context.getEncoder() == null)) {
                        packet.unref();
                        continue;
                    }

                    if (context.getDecoder().send(packet) < 0) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Decoding failed");
                        }
                        packet.unref();
                        break;
                    }

                    while (true) {
                        ret = context.getDecoder().receive(context.getFrame());
                        if ((ret == AVError.AVERROR_EOF.getStatusCode()) || (ret == AVError.AVERROR(Error.EAGAIN))) {
                            break;
                        } else if (ret < 0) {
                            packet.unref();
                            if (logger.isDebugEnabled()) {
                                logger.debug("Error occurred when receive decoded frame");
                            }
                            return;
                        }

                        context.getFrame().setPts(context.getFrame().getBestEffortTimestamp());
                        ret = this.filter_encode_write_frame(context.getFrame(), os, packet.getIndex());
                        if (ret < 0) {
                            packet.unref();
                            if (logger.isDebugEnabled()) {
                                logger.debug("Error occurred when writing output file");
                            }
                            throw new RuntimeException("Error occurred when writing output file");
                        }
                    }
                } else {
                    // remux this frame without reencoding
                    packet.rescale(this.ifmt_ctx.getStream(packet.getIndex()).getTimebase(),
                            this.ofmt_ctx.getStream(packet.getIndex()).getTimebase());
                    ret = os.write(packet, true);
                    if (ret < 0) {
                        packet.unref();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Error occurred when writing output file");
                        }
                        throw new RuntimeException("Error occurred when writing output file");
                    }
                }
                packet.unref();
            }

            // flush decoders, filters and encoders
            for (int i = 0; i < this.ifmt_ctx.getStreams(); i++) {
                if (this.filter_ctx[i] == null) {
                    continue;
                }
                AVContext context = this.stream_ctx[i];
                if ((context == null) || (context.getDecoder() == null) || (context.getEncoder() == null)) {
                    continue;
                }

                /* flush decoder */
                int ret = context.getDecoder().flush();
                if (ret < 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Flushing decoding failed");
                    }
                    return;
                }
                while (true) {
                    ret = context.getDecoder().receive(context.getFrame());
                    if (ret == AVError.AVERROR_EOF.getStatusCode()) {
                        break;
                    } else if (ret < 0) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Error occurred when receive decoded frame");
                        }
                        return;
                    }

                    context.getFrame().setPts(context.getFrame().getBestEffortTimestamp());
                    ret = this.filter_encode_write_frame(context.getFrame(), os, i);
                    if (ret < 0) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Error occurred when writing filter encode frame");
                        }
                        throw new RuntimeException("Error occurred when writing filter encode frame");
                    }
                }

                // flush filter
                ret = filter_encode_write_frame(null, os, i);
                if (ret < 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Flushing filter failed");
                    }
                    return;
                }

                // flush encoder
                ret = flush_encoder(os, i);
                if (ret < 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Error occurred when flush encoder");
                    }
                    return;
                }
            }

            os.close();
        } catch (Exception e) {
            logger.debug("Unable to open input stream.", e);
        } finally {
            if (packet != null) {
                packet.close();
            }
            this.close();
        }
    }

    private int flush_encoder(@NonNull AVOutputStream os, int index) {
        AVCodecContext ctx = this.stream_ctx[index].getEncoder().getCodecContext();
        if ((ctx.getCodec().getCapabilities() & AVCodec.AV_CODEC_CAP_DELAY) == 0) {
            return 0;
        }
        return encode_write_frame(os, index, true);
    }

    /**
     * 打开输入流
     * @param url 流地址
     * @return true 成功, false 失败
     */
    private boolean open_input_file(@NonNull String url) {
        AVFormatContext ctx = null;
        AVContext[] contexts = null;
        try {
            AVCodecFactory factory = new AVCodecFactoryImpl();
            AVFormat format = new AVFormatImpl();
            ctx = format.open(url);
            contexts = new AVContext[ctx.getStreams()];
            for (int i = 0; i < ctx.getStreams(); i++) {
                AVStream stream = ctx.getStream(i);
                AVCodecParameters parameters = stream.getCodecParameters();
                Optional<AVCodec> optional = factory.getDecoder(parameters.getCodecId());
                if (optional.isEmpty()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failed to find decoder for stream: {}", i);
                    }
                    throw new RuntimeException("Failed to find decoder for stream: " + i);
                }
                AVCodecContext context = optional.get().getCodecContext();
                context.setCodecParameters(parameters);
                // Inform the decoder about the timebase for the packet timestamps.
                // This is highly recommended, but not mandatory.
                context.setPktTimebase(stream.getTimebase());

                contexts[i] = new AVContext();
                // Reencode video & audio and remux subtitles etc.
                if (context.getMediaType() == AVMediaType.AVMEDIA_TYPE_VIDEO ||
                        context.getMediaType() == AVMediaType.AVMEDIA_TYPE_AUDIO) {
                    if (context.getMediaType() == AVMediaType.AVMEDIA_TYPE_VIDEO) {
                        context.setFrameRate(stream.getFrameRate(ctx));
                    }
                    // Open decoder
                    Optional<AVDecoder> decoder = context.open();
                    if (decoder.isEmpty()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Failed to open decoder for stream: {}", i);
                        }
                        throw new RuntimeException("Failed to open decoder for stream: " + i);
                    }

                    contexts[i].setDecoder(decoder.get());
                    contexts[i].setFrame(new AVFrameImpl());
                }
            }

            ctx.print();
            this.ifmt_ctx = ctx;
            this.stream_ctx = contexts;
            return true;
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to open input stream: {}", url, e);
            }
            this.close(ctx, null, contexts);
        }
        return false;
    }

    /**
     * 打开输出流
     * @param url 流地址
     * @return true 成功, false 失败
     */
    @SuppressWarnings({"SpellCheckingInspection", "SameParameterValue"})
    private boolean open_output_file(@NonNull String url, boolean faststart) {
        AVFormatContext ctx = null;
        try {
            AVCodecFactory factory = new AVCodecFactoryImpl();
            AVFormat format = new AVFormatImpl(AVFormat.OUTPUT);
            ctx = format.open(url);
            for (int i = 0; i < this.ifmt_ctx.getStreams(); i++) {
                Optional<AVStream> streams = ctx.addStream();
                if (streams.isEmpty()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failed allocating output stream");
                    }
                    throw new RuntimeException("Failed allocating output stream");
                }

                AVCodecContext decoder = this.stream_ctx[i].getDecoder().getCodecContext();
                AVStream istream = this.ifmt_ctx.getStream(i);
                AVStream ostream = streams.get();
                if (decoder.getMediaType() == AVMediaType.AVMEDIA_TYPE_VIDEO ||
                        decoder.getMediaType() == AVMediaType.AVMEDIA_TYPE_AUDIO) {
                    // in this example, we choose transcoding to same codec
                    Optional<AVCodec> optional = factory.getEncoder(decoder.getCodecId());
                    if (optional.isEmpty()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Necessary encoder not found: {}", decoder.getCodecId());
                        }
                        throw new RuntimeException("Necessary encoder not found: " + decoder.getCodecId());
                    }

                    // In this example, we transcode to same properties (picture size, sample rate etc.).
                    // These properties can be changed for output streams easily using filters
                    AVCodecContext context = optional.get().getCodecContext();
                    if (decoder.getMediaType() == AVMediaType.AVMEDIA_TYPE_VIDEO) {
                        AVPixelFormat[] pixelFormats = optional.get().getPixelFormats();
                        context.setWidth(decoder.getWidth())
                                .setHeight(decoder.getHeight())
                                .setAspectRatio(decoder.getAspectRatio())
                                // take first format from list of supported formats
                                .setPixelFormat((pixelFormats.length > 0) ? pixelFormats[0] : decoder.getPixelFormat());
                        // video time_base can be set to whatever is handy and supported by encoder
                        context.setTimebase(decoder.getFrameRate().reverse());
                    } else {
                        AVSampleFormat[] sampleFormats = optional.get().getSampleFormats();
                        context.setSampleRate(decoder.getSampleRate())
                                .setChannelLayout(decoder.getChannelLayout())
                                // take first format from list of supported formats
                                .setSampleFormat((sampleFormats.length > 0) ? sampleFormats[0] : decoder.getSampleFormat());
                        context.setTimebase(new AVRational(1, decoder.getSampleRate()));
                    }

                    Optional<AVOutputFormat> oft = ctx.getOutputFormat();
                    int GLOBAL_HEADER = oft.map(avOutputFormat -> avOutputFormat.getFlags() & AVOutputFormat.AVFMT_GLOBALHEADER).orElse(0);
                    if (GLOBAL_HEADER != 0) {
                        context.setFlags(context.getFlags() | AVCodecContext.AV_CODEC_FLAG_GLOBAL_HEADER);
                    }

                    /* Third parameter can be used to pass settings to encoder */
                    Optional<AVEncoder> encoder = context.open();
                    if (encoder.isEmpty()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Cannot open {} encoder for stream: {}", optional.get().getCodecName(), i);
                        }
                        throw new RuntimeException("Cannot open " + optional.get().getCodecName() + " encoder for stream: " + i);
                    }

                    ostream.getCodecParameters().setCodecContext(context);
                    ostream.setTimebase(context.getTimebase());
                    if (this.stream_ctx[i] != null) {
                        this.stream_ctx[i].setEncoder(encoder.get());
                    }
                } else if (decoder.getMediaType() == AVMediaType.AVMEDIA_TYPE_UNKNOWN) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Elementary stream {} is of unknown type, cannot proceed", i);
                    }
                    throw new RuntimeException("Elementary stream " + i + " is of unknown type, cannot proceed");
                } else {
                    /* if this stream must be remuxed */
                    ostream.setCodecParameters(istream.getCodecParameters());
                    ostream.setTimebase(istream.getTimebase());
                }
            }

            Function<String, Boolean> function = s -> {
                if (s.isEmpty()) {
                    return false;
                }
                final String[] extensions = {
                        ".mp4", ".mov", ".3gp", ".f4v",
                };
                String path = s.toLowerCase();
                for (String suffix : extensions) {
                    if (path.endsWith(suffix)) {
                        return true;
                    }
                }
                return false;
            };
            if (faststart && function.apply(url)) {
                AVOptions options = new AVOptionsImpl(ctx);
                options.put("movflags", "faststart", AVOptions.AV_OPT_SEARCH_CHILDREN);
            }

            ctx.print();
            this.ofmt_ctx = ctx;
            return true;
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to open output stream: {}", url, e);
            }
            this.close(null, ctx, null);
        }
        return false;
    }

    private boolean init_filters() {
        AVFilters[] filters = null;
        try {
            filters = new AVFilters[this.ifmt_ctx.getStreams()];
            for (int i = 0; i < this.ifmt_ctx.getStreams(); i++) {
                AVCodecParameters parameters = this.ifmt_ctx.getStream(i).getCodecParameters();
                if (!(parameters.getMediaType() == AVMediaType.AVMEDIA_TYPE_AUDIO ||
                        parameters.getMediaType() == AVMediaType.AVMEDIA_TYPE_VIDEO)) {
                    filters[i] = null;
                    continue;
                }

                String specs = (parameters.getMediaType() == AVMediaType.AVMEDIA_TYPE_VIDEO) ?
                        "null" : /* passthrough (dummy) filter for video */
                        "anull"; /* passthrough (dummy) filter for audio */
                Optional<AVFilters> optional = this.init_filter(this.stream_ctx[i], specs);
                if (optional.isEmpty()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Elementary stream {} filter init failed, cannot proceed", i);
                    }
                    throw new RuntimeException("Elementary stream " + i + " filter init failed, cannot proceed");
                }
                AVFilters filter = optional.get();
                filter.setPacket(new AVPacketImpl());
                filter.setFrame(new AVFrameImpl());
                filters[i] = filter;
            }

            this.filter_ctx = filters;
            return true;
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to init filters");
            }
            this.close(filters);
        }
        return false;
    }

    private Optional<AVFilters> init_filter(@NonNull AVContext context, String specs) {
        AVFilter iFilter;
        AVFilter oFilter;
        AVFilterContext iFilterContext;
        AVFilterContext oFilterContext;
        AVFilterInOut inputs = new AVFilterInOutImpl();
        AVFilterInOut outputs = new AVFilterInOutImpl();
        AVFilterGraph graph = new AVFilterGraphImpl();

        Function<AVRational, String> function = rational -> String.format("%d/%d",
                rational.getNumerator(), rational.getDenominator());

        AVFilterFactory factory = new AVFilterFactoryImpl();
        AVCodecContext decoder = context.getDecoder().getCodecContext();
        try {
            if (decoder.getMediaType() == AVMediaType.AVMEDIA_TYPE_VIDEO) {
                Optional<AVFilter> iOptional = factory.getFilter("buffer");
                Optional<AVFilter> oOptional = factory.getFilter("buffersink");
                if (iOptional.isEmpty() || oOptional.isEmpty()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Filtering source element not found");
                    }
                    return Optional.empty();
                }
                iFilter = iOptional.get();
                oFilter = oOptional.get();

                AVDictionary dictionary = new AVDictionaryImpl();
                dictionary.put("width", decoder.getWidth());
                dictionary.put("height", decoder.getHeight());
                dictionary.put("pix_fmt", decoder.getPixelFormat().getName());
                dictionary.put("time_base", function.apply(decoder.getPktTimebase()));
                dictionary.put("pixel_aspect", function.apply(decoder.getAspectRatio()));
                iFilterContext = new AVFilterContextImpl(graph, iFilter, "in");
                iFilterContext.setDictionary(dictionary);

                oFilterContext = new AVFilterContextImpl(graph, oFilter, "out");
                AVOptions options = new AVOptionsImpl(oFilterContext);
                options.putPixelFormat("pix_fmts",
                        context.getEncoder().getCodecContext().getPixelFormat(),
                        AVOptions.AV_OPT_SEARCH_CHILDREN);
                oFilterContext.setDictionary(null);
            } else if (decoder.getMediaType() == AVMediaType.AVMEDIA_TYPE_AUDIO) {
                Optional<AVFilter> iOptional = factory.getFilter("abuffer");
                Optional<AVFilter> oOptional = factory.getFilter("abuffersink");
                if (iOptional.isEmpty() || oOptional.isEmpty()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Filtering source element not found");
                    }
                    return Optional.empty();
                }
                iFilter = iOptional.get();
                oFilter = oOptional.get();

                Function<String, String> emptyIfNull = s -> (s == null) ? "" : s;
                AVCodecContext ctx = context.getDecoder().getCodecContext();
                AVChannelLayout layout = ctx.getChannelLayout();
                if (layout.getChannelOrder() == AVChannelOrder.AV_CHANNEL_ORDER_UNSPEC) {
                    layout.setDefaults(layout.getChannels());
                }

                AVDictionary dictionary = new AVDictionaryImpl();
                dictionary.put("time_base", function.apply(ctx.getPktTimebase()));
                dictionary.put("sample_rate", ctx.getSampleRate());
                dictionary.put("sample_fmt", emptyIfNull.apply(ctx.getSampleFormat().getName()));
                dictionary.put("channel_layout", emptyIfNull.apply(layout.getDescription()));
                iFilterContext = new AVFilterContextImpl(graph, iFilter, "in");
                iFilterContext.setDictionary(dictionary);

                oFilterContext = new AVFilterContextImpl(graph, oFilter, "out");
                AVOptions options = new AVOptionsImpl(oFilterContext);
                options.putSampleFormat("sample_fmts",
                        context.getEncoder().getCodecContext().getSampleFormat(),
                        AVOptions.AV_OPT_SEARCH_CHILDREN);
                options.put("ch_layouts", emptyIfNull.apply(
                                context.getEncoder().getCodecContext().getChannelLayout().getDescription()),
                        AVOptions.AV_OPT_SEARCH_CHILDREN);
                options.put("sample_rates",
                        String.valueOf(context.getEncoder().getCodecContext().getSampleRate()),
                        AVOptions.AV_OPT_SEARCH_CHILDREN);
                if (context.getEncoder().getCodecContext().getFrameSize() > 0) {
                    oFilterContext.setFrameSize(context.getEncoder().getCodecContext().getFrameSize());
                }
                oFilterContext.setDictionary(null);
            } else {
                throw new RuntimeException("Unknown");
            }

            /* Endpoints for the filter graph. */
            outputs.setName("in");
            outputs.setFilterContext(iFilterContext);
            outputs.setIndex(0);
            outputs.setNext(null);

            inputs.setName("out");
            inputs.setFilterContext(oFilterContext);
            inputs.setIndex(0);
            inputs.setNext(null);

            graph.setDescription(specs, inputs, outputs);
            graph.prepare();

            AVFilters filters = new AVFilters();
            filters.setInputContext(iFilterContext);
            filters.setOutputContext(oFilterContext);
            filters.setFilterGraph(graph);

            outputs.close();
            inputs.close();
            return Optional.of(filters);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to init filter");
            }
        }

        outputs.close();
        inputs.close();
        graph.close();
        return Optional.empty();
    }

    private int encode_write_frame(@NonNull AVOutputStream os, int index, boolean flush) {
        AVContext context = this.stream_ctx[index];
        AVFilters filters = this.filter_ctx[index];
        AVFrame frame = flush ? null : filters.getFrame();
        AVPacket packet = filters.getPacket();

        if (logger.isDebugEnabled()) {
            logger.debug("Encoding frame");
        }
        if (flush) {
            logger.debug("Flush {}, index: {}", context.getDecoder().getCodecContext().getMediaType(), index);
        }

        // encode filtered frame
        packet.unref();

        if ((frame != null) && (frame.getPts() != AVUtils.AV_NOPTS_VALUE)) {
            frame.setPts(AVMath.rescale(frame.getPts(), frame.getTimebase(), context.getEncoder()
                    .getCodecContext().getTimebase()));
        }
        AVEncoder encoder = context.getEncoder();
        int ret = encoder.send(frame);
        if (ret < 0) {
            return ret;
        }

        while (ret >= 0) {
            ret = encoder.receive(packet);
            if ((ret == AVError.AVERROR(Error.EAGAIN)) || (ret == AVError.AVERROR_EOF.getStatusCode())) {
                return 0;
            }

            // prepare packet for muxing
            packet.setIndex(index);
            packet.rescale(encoder.getCodecContext().getTimebase(),
                    this.ofmt_ctx.getStream(index).getTimebase());
            if (logger.isDebugEnabled()) {
                logger.debug("Muxing frame");
            }
            ret = os.write(packet, true);
        }
        return ret;
    }

    @SuppressWarnings("resource")
    private int filter_encode_write_frame(@Nullable AVFrame frame, @NonNull AVOutputStream os, int index) {
        AVFilters filters = this.filter_ctx[index];

        if (logger.isDebugEnabled()) {
            logger.debug("Pushing decoded frame to filters");
        }

        // push the decoded frame into the filter graph
        int ret = filters.getInputContext().send(frame, 0);
        if (ret < 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Error while feeding the filter graph");
            }
            return ret;
        }

        // pull filtered frames from the filter graph
        while (true) {
            if (logger.isDebugEnabled()) {
                logger.debug("Pulling filtered frame from filters");
            }
            ret = filters.getOutputContext().receive(filters.getFrame());
            if (ret < 0) {
                /* if no more frames for output - returns AVERROR(EAGAIN)
                 * if flushed and no more frames for output - returns AVERROR_EOF
                 * rewrite retcode to 0 to show it as normal procedure completion
                 */
                if ((ret == AVError.AVERROR(Error.EAGAIN)) || (ret == AVError.AVERROR_EOF.getStatusCode())) {
                    ret = 0;
                }
                break;
            }

            filters.getFrame().setTimebase(filters.getOutputContext().getTimebase());
            filters.getFrame().setPictureType(AVPictureType.AV_PICTURE_TYPE_NONE);
            ret = encode_write_frame(os, index, false);
            filters.getFrame().unref();
            if (ret < 0) {
                break;
            }
        }
        return ret;
    }

    private void close() {
        this.close(this.ifmt_ctx, this.ofmt_ctx, this.stream_ctx);
        this.close(this.filter_ctx);
    }

    private void close(@Nullable AVFormatContext ifmt_ctx, @Nullable AVFormatContext ofmt_ctx,
                       @Nullable AVContext[] stream_ctx) {
        if (stream_ctx != null) {
            for (AVContext context : stream_ctx) {
                if (context == null) {
                    continue;
                }
                if (context.getFrame() != null) {
                    context.getFrame().close();
                }
                if (context.getDecoder() != null) {
                    context.getDecoder().getCodecContext().close();
                }
            }
        }
        if (ofmt_ctx != null) {
            ofmt_ctx.close();
        }
        if (ifmt_ctx != null) {
            ifmt_ctx.close();
        }
    }

    private void close(@Nullable AVFilters[] filter_ctx) {
        if (filter_ctx != null) {
            for (AVFilters filters : filter_ctx) {
                if (filters == null) {
                    continue;
                }
                if (filters.getFrame() != null) {
                    filters.getFrame().close();
                }
                if (filters.getPacket() != null) {
                    filters.getPacket().close();
                }
                if (filters.getFilterGraph() != null) {
                    filters.getFilterGraph().close();
                }
            }
        }
    }

    protected static class AVFilters {

        private AVFilterContext buffer_sink_ctx;
        private AVFilterContext buffer_ctx;
        private AVFilterGraph graph;
        private AVFrame frame;
        private AVPacket packet;

        public AVFilters() {
        }

        public AVFilterContext getOutputContext() {
            return this.buffer_sink_ctx;
        }

        public void setOutputContext(AVFilterContext context) {
            this.buffer_sink_ctx = context;
        }

        public AVFilterContext getInputContext() {
            return this.buffer_ctx;
        }

        public void setInputContext(AVFilterContext context) {
            this.buffer_ctx = context;
        }

        public AVFilterGraph getFilterGraph() {
            return this.graph;
        }

        public void setFilterGraph(AVFilterGraph graph) {
            this.graph = graph;
        }

        public AVFrame getFrame() {
            return frame;
        }

        public void setFrame(AVFrame frame) {
            this.frame = frame;
        }

        public AVPacket getPacket() {
            return packet;
        }

        public void setPacket(AVPacket packet) {
            this.packet = packet;
        }
    }

    protected static class AVContext {

        private AVDecoder decoder;
        private AVEncoder encoder;
        private AVFrame frame;

        public AVContext() {
        }

        public AVDecoder getDecoder() {
            return decoder;
        }

        public void setDecoder(AVDecoder decoder) {
            this.decoder = decoder;
        }

        public AVEncoder getEncoder() {
            return encoder;
        }

        public void setEncoder(AVEncoder encoder) {
            this.encoder = encoder;
        }

        public AVFrame getFrame() {
            return frame;
        }

        public void setFrame(AVFrame frame) {
            this.frame = frame;
        }
    }
}
