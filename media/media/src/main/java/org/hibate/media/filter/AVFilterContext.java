/*
 * Copyright (C) 2026 Hibate <ycaia86@126.com>
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

package org.hibate.media.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.hibate.media.util.AVDictionary;
import org.hibate.media.util.AVFrame;
import org.hibate.media.util.AVRational;

import java.io.Closeable;

/**
 * @author Hibate
 * Created on 2026/03/21.
 */
public interface AVFilterContext extends Closeable {

    /**
     * 获取实例名称
     * @return 返回名称
     */
    String getName();

    /**
     * 获取帧时间戳的基本时间单位: 以秒为单位
     * @return 基本时间单位
     */
    AVRational getTimebase();

    /**
     * 设置参数: avfilter_init_dict
     * @param dictionary 参数
     * @return {@link AVFilterContext}
     */
    AVFilterContext setDictionary(@Nullable AVDictionary dictionary) throws IllegalArgumentException;

    /**
     * 设置音频缓冲区接收器的帧大小: av_buffersink_set_frame_size
     * @return {@link AVFilterContext}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVFilterContext setFrameSize(int frameSize);

    /**
     * 向缓冲区源添加数据帧: av_buffersrc_add_frame_flags
     * @param frame 数据帧
     * @return >= 0 成功, < 0 失败
     */
    int send(AVFrame frame);

    /**
     * 向缓冲区源添加数据帧: av_buffersrc_add_frame_flags
     * @param frame 数据帧
     * @param flags 标记
     * @return >= 0 成功, < 0 失败
     */
    int send(AVFrame frame, int flags);

    /**
     * 从接收端获取包含已过滤数据的帧, 并将其放入帧中: av_buffersink_get_frame
     * @param frame 数据帧
     * @return >= 0 成功, < 0 失败
     */
    int receive(@NonNull AVFrame frame);

    /**
     * 释放资源
     */
    void close();
}
