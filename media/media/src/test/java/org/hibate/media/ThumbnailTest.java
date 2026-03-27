package org.hibate.media;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.hibate.media.codec.*;
import org.hibate.media.codec.impl.AVCodecFactoryImpl;
import org.hibate.media.codec.impl.AVPacketImpl;
import org.hibate.media.format.*;
import org.hibate.media.format.impl.AVFormatImpl;
import org.hibate.media.format.impl.AVInputStreamImpl;
import org.hibate.media.format.impl.AVOutputStreamImpl;
import org.hibate.media.util.Action;
import org.hibate.media.util.Factory;
import org.hibate.media.util.Source;
import org.hibate.media.util.*;
import org.hibate.media.util.impl.AVDictionaryImpl;
import org.hibate.media.util.impl.AVFrameImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 截图示例
 * @author Hibate
 * Created on 2024/03/30.
 */
public class ThumbnailTest {

    private static final Logger logger =
            LoggerFactory.getLogger(ThumbnailTest.class);

    private File file;
    private File directory;

    @BeforeEach
    public void init() {
        // TODO 请设置参数
    }

    @Test
    public void thumbnail() {
        final int times = 10;
        final AtomicInteger integer = new AtomicInteger(0);
        if (directory != null && file != null) {
            Source source = Factory.create(directory, file, s -> "-" + integer.get() + ".jpg");
            this.thumbnail(source, frame ->
                    integer.getAndAdd(1) < times ? Action.AVAILABLE : Action.DONE);
        }
    }

    private void thumbnail(@NonNull Source source, @NonNull Function<AVFrame, Action> function) {
        String url = source.getUrl();
        Map<Integer, AVDecoder> decoders = new HashMap<>();
        try (AVFormatContext ctx = this.open(url)) {
            AVCodecFactory factory = new AVCodecFactoryImpl();
            for (int i = 0; i < ctx.getStreams(); i++) {
                AVStream stream = ctx.getStream(i);
                AVCodecParameters parameters = stream.getCodecParameters();
                if (parameters.getMediaType() != AVMediaType.AVMEDIA_TYPE_VIDEO) {
                    continue;
                }

                Optional<AVCodec> optional = factory.getDecoder(parameters.getCodecId());
                if (optional.isEmpty()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failed to find codec for stream: {}", i);
                    }
                    throw new RuntimeException("Failed to find codec for stream: " + i);
                }
                AVCodecContext context = optional.get().getCodecContext();
                context.setCodecParameters(parameters);
                Optional<AVDecoder> decoder = context.open();
                if (decoder.isEmpty()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failed to open decoder for stream: {}", i);
                    }
                    throw new RuntimeException("Failed to open decoder for stream: " + i);
                }

                decoders.put(i, decoder.get());
            }

            AVPacket packet = new AVPacketImpl();
            AVFrame frame = new AVFrameImpl();
            AVInputStream is = new AVInputStreamImpl(ctx);
            Action action = Action.NEXT;
            while (action != Action.DONE) {
                int code = is.read(packet);
                if (code == AVError.AVERROR_EOF.getStatusCode()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("End of the input stream. url: {}", url);
                    }
                    break;
                } else if (code < 0) {
                    continue;
                }

                AVDecoder decoder = decoders.get(packet.getIndex());
                if (decoder != null) {
                    code = decoder.send(packet);
                    if (code < 0) {
                        packet.unref();
                        continue;
                    }

                    while (true) {
                        code = decoder.receive(frame);
                        if (code == AVError.AVERROR_EOF.getStatusCode()) {
                            break;
                        } else if (code < 0) {
                            break;
                        }

                        action = function.apply(frame);
                        if ((action == Action.AVAILABLE) && !this.save(frame, source.getFile())) {
                            action = Action.DONE;
                            break;
                        }
                    }
                }
                packet.unref();
            }
            frame.close();
            packet.close();
        } catch (Exception e) {
            if (logger.isInfoEnabled()) {
                logger.info("Unable to open input stream.", e);
            }
        } finally {
            for (AVDecoder decoder : decoders.values()) {
                decoder.getCodecContext().close();
            }
        }
    }

    /**
     * 打开视频链接
     * @param url 视频链接
     * @return 返回对象
     * @throws IOException 打开失败时抛出异常
     */
    @NonNull
    private AVFormatContext open(@NonNull String url) throws IOException {
        AVDictionary dictionary = new AVDictionaryImpl();
        if (url.startsWith("rtsp://")) {
            dictionary.put("rtsp_transport", "tcp");
        }
        dictionary.put("timeout", Duration.ofSeconds(3).toMillis() * 1000); // us

        AVFormat format = new AVFormatImpl();
        return format.open(url, dictionary);
    }

    /**
     * 保存数据帧
     * @param frame 数据帧
     * @param file 目标文件
     * @return true 保存成功, false 保存失败
     */
    private boolean save(@Nullable AVFrame frame, @Nullable File file) {
        if ((frame == null) || (file == null)) {
            return false;
        }

        AVFormatContext context = null;
        AVCodecContext ctx = null;
        try {
            AVCodecFactory factory = new AVCodecFactoryImpl();
            AVFormat format = new AVFormatImpl(AVFormat.OUTPUT);
            context = format.open(file);

            if (context.getOutputFormat().isEmpty()) {
                return false;
            }

            AVOutputFormat outputFormat = context.getOutputFormat().get();
            Optional<AVStream> streams = context.addStream();
            if (streams.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to create new stream to output format.");
                }
                return false;
            }
            AVStream stream = streams.get();
            AVCodecParameters parameters = stream.getCodecParameters();
            parameters.setMediaType(AVMediaType.AVMEDIA_TYPE_VIDEO)
                    .setCodecId(outputFormat.getVideoCodecId())
                    .setPixelFormat(AVPixelFormat.AV_PIX_FMT_YUVJ420P)
                    .setWidth(frame.getWidth())
                    .setHeight(frame.getHeight());

            Optional<AVCodec> codecs = factory.getEncoder(parameters.getCodecId());
            if (codecs.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to find codec for output stream.");
                }
                return false;
            }
            ctx = codecs.get().getCodecContext();
            ctx.setCodecParameters(parameters);
            ctx.setTimebase(new AVRational(1, 25));

            Optional<AVEncoder> encoders = ctx.open();
            if (encoders.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to open encoder for output stream.");
                }
                return false;
            }
            AVEncoder encoder = encoders.get();

            int code = encoder.send(frame);
            if (code < 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to send frame for encode.");
                }
                return false;
            }

            boolean successful = false;
            AVPacket packet = new AVPacketImpl();
            while (true) {
                code = encoder.receive(packet);
                if (code == AVError.AVERROR_EOF.getStatusCode()) {
                    break;
                } else if (code < 0) {
                    break;
                }

                AVOutputStream outputStream = new AVOutputStreamImpl(context);
                outputStream.write();
                outputStream.write(packet);
                outputStream.close();
                packet.unref();
                successful = true;
            }
            packet.close();
            return successful;
        } catch (Exception e) {
            if (logger.isInfoEnabled()) {
                logger.info("Unable to open output stream.", e);
            }
        } finally {
            if (ctx != null) {
                ctx.close();
            }
            if (context != null) {
                context.close();
            }
        }
        return false;
    }
}
