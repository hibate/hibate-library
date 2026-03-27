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

import androidx.annotation.NonNull;

import java.io.Closeable;
import java.util.Optional;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
public interface AVFormatContext extends Closeable {

    /**
     * Enabled when required by target format
     */
    int AVFMT_AVOID_NEG_TS_AUTO = -1;
    /**
     * Do not shift timestamps even when they are negative
     */
    int AVFMT_AVOID_NEG_TS_DISABLED = 0;
    /**
     * Shift timestamps so they are non negative
     */
    int AVFMT_AVOID_NEG_TS_MAKE_NON_NEGATIVE = 1;
    /**
     * Shift timestamps so that they start at 0
     */
    int AVFMT_AVOID_NEG_TS_MAKE_ZERO = 2;

    /**
     * 获取输入容器格式
     * 仅限解封装模式
     * @return 返回输入容器格式
     */
    @NonNull
    Optional<AVInputFormat> getInputFormat();

    /**
     * 获取输出容器格式
     * 仅限封装模式
     * @return 返回输出容器格式
     */
    @NonNull
    Optional<AVOutputFormat> getOutputFormat();

    /**
     * 获取流数量
     * @return 返回流数量
     */
    int getStreams();

    /**
     * 获取数据流
     * @param index 流索引
     * @return 返回数据流
     */
    @NonNull
    AVStream getStream(int index) throws IllegalArgumentException;

    /**
     * 新增数据流, 内存分配失败返回空
     * @return 数据流
     */
    Optional<AVStream> addStream();

    /**
     * 获取输入/输出地址
     * @return 返回输入/输出地址
     */
    String getUrl();

    /**
     * 获取第一帧位置, 单位 s
     * 仅限解封装模式
     * @return 返回第一帧位置
     */
    long getStartTime();

    /**
     * 获取流时长, 单位 s
     * 仅限解封装模式
     * @return 返回流时长
     */
    long getDuration();

    /**
     * 获取所有流的比特率, 单位 bit/s, 不可用返回 0
     * @return 返回所有流的比特率
     */
    long getBitRate();

    /**
     * 在复用过程中避免使用负时间戳
     * @param flags 标记
     */
    AVFormatContext setAvoidNegativeTs(int flags);

    /**
     * 打印详细信息
     * @return {@link AVFormatContext}
     */
    AVFormatContext print();

    /**
     * 打印详细信息
     * @param index 流索引
     * @return {@link AVFormatContext}
     */
    AVFormatContext print(int index);

    /**
     * 释放资源
     */
    void close();
}
