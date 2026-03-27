package org.hibate.media.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.function.Function;

/**
 * @author Hibate
 * Created on 2024/09/04.
 */
public interface Factory {

    /**
     * 创建数据源
     * @param directory 存储路径
     * @param url 视频文件
     * @return 返回数据源
     */
    static Source create(@NonNull File directory, @NonNull String url) {
        return new SourceImpl(directory, new String[] {url});
    }

    /**
     * 创建数据源
     * @param directory 存储路径
     * @param url 视频文件
     * @param function 回调
     * @return 返回数据源
     */
    static Source create(@NonNull File directory, @NonNull String url,
                         @Nullable Function<String, String> function) {
        return new SourceImpl(directory, new String[] {url}, function);
    }

    /**
     * 创建数据源
     * @param directory 存储路径
     * @param urls 视频文件
     * @return 返回数据源
     */
    static Source create(@NonNull File directory, @NonNull String[] urls) {
        return new SourceImpl(directory, urls);
    }

    /**
     * 创建数据源
     * @param directory 存储路径
     * @param urls 视频文件
     * @param function 回调
     * @return 返回数据源
     */
    static Source create(@NonNull File directory, @NonNull String[] urls,
                         @Nullable Function<String, String> function) {
        return new SourceImpl(directory, urls, function);
    }

    /**
     * 创建数据源
     * @param directory 存储路径
     * @param file 视频文件
     * @return 返回数据源
     */
    static Source create(@NonNull File directory, @NonNull File file) {
        return new SourceImpl(directory, new String[] {file.getAbsolutePath()});
    }

    /**
     * 创建数据源
     * @param directory 存储路径
     * @param file 视频文件
     * @param function 回调
     * @return 返回数据源
     */
    static Source create(@NonNull File directory, @NonNull File file,
                         @Nullable Function<String, String> function) {
        return new SourceImpl(directory, new String[] {file.getAbsolutePath()}, function);
    }
}
