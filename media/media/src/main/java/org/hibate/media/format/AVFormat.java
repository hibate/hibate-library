/*
 * Copyright (C) 2024 Hibate <ycaia86@126.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibate.media.format;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import org.hibate.media.util.AVDictionary;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
public interface AVFormat {

    /**
     * 输入流模式
     */
    int INPUT = 0;
    /**
     * 输出流模式
     */
    int OUTPUT = 1;

    @IntDef({INPUT, OUTPUT})
    @Retention(RetentionPolicy.SOURCE)
    @interface Mode {}

    /**
     * 输出格式: 通过类型分配
     */
    String OUTPUT_FORMAT = "format";

    @StringDef({OUTPUT_FORMAT})
    @Retention(RetentionPolicy.SOURCE)
    @interface Dictionary {}

    /**
     * 打开输入/输出流
     * @param url 文件路径
     * @return 返回 {@link AVFormatContext}
     */
    @NonNull
    AVFormatContext open(@Nullable File url) throws IOException, RuntimeException;

    /**
     * 打开输入/输出流
     * @param url 文件路径
     * @param dictionary 参数
     * @return 返回 {@link AVFormatContext}
     * @see Dictionary 输出流参数
     */
    @NonNull
    AVFormatContext open(@Nullable File url, @Nullable AVDictionary dictionary) throws IOException, RuntimeException;

    /**
     * 打开输入/输出流
     * @param url 路径
     * @return 返回 {@link AVFormatContext}
     */
    @NonNull
    AVFormatContext open(@Nullable String url) throws IOException, RuntimeException;

    /**
     * 打开输入/输出流
     * @param url 路径
     * @param dictionary 参数
     * @return 返回 {@link AVFormatContext}
     * @see Dictionary 输出流参数
     */
    @NonNull
    AVFormatContext open(@Nullable String url, @Nullable AVDictionary dictionary) throws IOException, RuntimeException;
}
