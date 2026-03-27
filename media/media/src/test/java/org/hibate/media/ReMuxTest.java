package org.hibate.media;

import androidx.annotation.NonNull;
import org.hibate.media.codec.AVCodecParameters;
import org.hibate.media.codec.AVMediaType;
import org.hibate.media.codec.AVPacket;
import org.hibate.media.codec.impl.AVPacketImpl;
import org.hibate.media.format.*;
import org.hibate.media.format.impl.AVFormatImpl;
import org.hibate.media.format.impl.AVInputStreamImpl;
import org.hibate.media.format.impl.AVOutputStreamImpl;
import org.hibate.media.util.Factory;
import org.hibate.media.util.Source;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 转封装示例
 * @author Hibate
 * Created on 2024/09/04.
 */
public class ReMuxTest {

    private static final Logger logger =
            LoggerFactory.getLogger(ReMuxTest.class);

    private File file;
    private File directory;

    @BeforeEach
    public void init() {
        // TODO 请设置参数
    }

    @Test
    public void remux() {
        if (directory != null && file != null) {
            Source source = Factory.create(directory, file);
            this.remux(source);
        }
    }

    private void remux(@NonNull Source source) {
        final String url = source.getUrl();

        AVFormatContext ifmt_ctx = null;
        AVFormatContext ofmt_ctx = null;
        AVPacket packet = null;
        try {
            ifmt_ctx = new AVFormatImpl().open(url);
            ofmt_ctx = new AVFormatImpl(AVFormat.OUTPUT).open(source.getFile());

            int index = 0;
            Map<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < ifmt_ctx.getStreams(); i++) {
                AVStream in_stream = ifmt_ctx.getStream(i);
                AVCodecParameters parameters = in_stream.getCodecParameters();

                AVMediaType mediaType = parameters.getMediaType();
                if ((mediaType != AVMediaType.AVMEDIA_TYPE_AUDIO) &&
                        (mediaType != AVMediaType.AVMEDIA_TYPE_VIDEO) &&
                        (mediaType != AVMediaType.AVMEDIA_TYPE_SUBTITLE)) {
                    map.put(i, -1);
                    continue;
                }

                map.put(i, index++);

                Optional<AVStream> optional = ofmt_ctx.addStream();
                if (optional.isEmpty()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failed allocating output stream.");
                    }
                    throw new RuntimeException("Failed allocating output stream.");
                }
                AVStream out_stream = optional.get();
                out_stream = out_stream.setCodecParameters(parameters);
                out_stream.getCodecParameters().setCodecTag(0);
            }

            AVOutputStream os = new AVOutputStreamImpl(ofmt_ctx);
            if (os.write() < 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Error occurred when opening output file.");
                }
                throw new RuntimeException("Error occurred when opening output file.");
            }

            packet = new AVPacketImpl();
            AVInputStream is = new AVInputStreamImpl(ifmt_ctx);
            while (true) {
                int ret = is.read(packet);
                if (ret < 0) {
                    break;
                }

                if ((packet.getIndex() >= ifmt_ctx.getStreams()) ||
                        !map.containsKey(packet.getIndex()) ||
                        (map.getOrDefault(packet.getIndex(), -1) < 0)) {
                    packet.unref();
                    continue;
                }

                AVStream in_stream = ifmt_ctx.getStream(packet.getIndex());
                AVStream out_stream = ofmt_ctx.getStream(packet.getIndex());
                packet.setIndex(map.get(packet.getIndex()));

                // copy packet
                packet.rescale(in_stream.getTimebase(), out_stream.getTimebase());
                packet.setPosition(-1);

                ret = os.write(packet, true);
                if (ret < 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Error muxing packet.");
                    }
                    break;
                }
            }
            os.close();
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to open input stream: {}", url, e);
            }
        } finally {
            if (packet != null) {
                packet.close();
            }
            if (ofmt_ctx != null) {
                ofmt_ctx.close();
            }
            if (ifmt_ctx != null) {
                ifmt_ctx.close();
            }
        }
    }
}
