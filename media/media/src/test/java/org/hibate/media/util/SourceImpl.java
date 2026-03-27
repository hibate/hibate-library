package org.hibate.media.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.hibate.logger.natives.LoggerOverSlf4j;
import org.hibate.logger.natives.Priority;
import org.hibate.media.util.impl.AVLoggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Hibate
 * Created on 2024/09/04.
 */
class SourceImpl implements Source {

    private static final Logger logger =
            LoggerFactory.getLogger(SourceImpl.class);

    private final File directory;
    private final String[] urls;
    private final Function<String, String> function;
    private String url;

    public SourceImpl(@NonNull File directory, @NonNull String[] urls) {
        this(directory, urls, null);
    }

    public SourceImpl(@NonNull File directory, @NonNull String[] urls,
                      @Nullable Function<String, String> function) {
        this.directory = directory;
        this.urls = urls;
        this.function = (function != null) ? function : s -> {
            if ((s == null) || s.isEmpty()) {
                return "";
            }
            int index = s.lastIndexOf(".");
            return (index > 0) ? s.substring(index + 1) : s;
        };
        this.initialize();
    }

    private void initialize() {
        // 初始化原生库日志
        org.hibate.logger.natives.Logger logger = org.hibate.logger.natives.LoggerFactory.getLogger();
        logger.setPriority(Priority.DEBUG);
        logger.setLineEnabled(false);
        logger.addLoggerListener(new LoggerOverSlf4j());
        // 初始化 FFmpeg 原生日志级别
        // 注意: 请确认 logback 配置中 FFmpeg 原生日志的过滤情况
        AVLogger avLogger = new AVLoggerImpl();
        avLogger.setPriority(AVLogger.AV_LOG_WARNING);
    }

    @Override
    public String getUrl() {
        BiFunction<Integer, Integer, Integer> factory = (min, max) -> {
            if ((max - min) < 1) {
                return min;
            }
            SecureRandom random = new SecureRandom();
            return random.nextInt(max - min + 1) + min;
        };
        this.url = this.urls[factory.apply(0, this.urls.length - 1)];
        if (logger.isDebugEnabled()) {
            logger.debug("Source url: {}", this.url);
        }
        return this.url;
    }

    @Override
    public File getFile() {
        return this.getFile(this.function.apply(this.url));
    }

    @Override
    public File getFile(@Nullable String extension) {
        extension = ((extension == null) || extension.isEmpty()) ? "" : (!extension.contains(".") ? ("." + extension) : extension);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String sb = simpleDateFormat.format(new Date()) + extension;
        return new File(this.directory, sb);
    }
}
