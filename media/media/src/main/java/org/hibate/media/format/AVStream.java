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

import androidx.annotation.Nullable;
import org.hibate.media.codec.AVCodecParameters;
import org.hibate.media.util.AVRational;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
public interface AVStream {

    /**
     * 获取流索引
     * @return 返回流索引
     */
    int getIndex();

    /**
     * 获取帧时间戳的基本时间单位: 以秒为单位
     * @return 基本时间单位
     */
    AVRational getTimebase();

    /**
     * 设置帧时间戳的基本时间单位: 以秒为单位
     * @param rational 基本时间单位
     */
    AVStream setTimebase(AVRational rational);

    /**
     * 解码: 以流时间基线为单位, 按顺序对流的第一帧进行解码.
     * <p>该值可能是未定义 {@link org.hibate.media.util.AVUtils#AV_NOPTS_VALUE}</p>
     * @return 返回第一帧时间
     * @see org.hibate.media.util.AVUtils#AV_NOPTS_VALUE
     */
    long getStartTime();

    /**
     * 解码: 以流时间基线为单位, 流的总时长.
     * <p>如果源文件没有指定持续时间, 但指定了比特率, 该值将根据比特率和文件大小来估计</p>
     * @return 返回总时长
     */
    long getDuration();

    /**
     * 获取流数据总帧数, 未获取到返回 0
     * @return 返回流数据总帧数
     */
    long getFrames();

    /**
     * 获取帧率
     * @return 返回帧率
     */
    AVRational getFrameRate();

    /**
     * 获取帧率
     * @param context #{@link AVFormatContext}
     * @return 返回帧率
     */
    AVRational getFrameRate(@Nullable AVFormatContext context);

    /**
     * 获取与流关联的编解码器参数
     * @return 返回与流关联的编解码器参数
     */
    AVCodecParameters getCodecParameters();

    /**
     * 设置与流关联的编解码器参数
     * @param parameters 与流关联的编解码器参数
     */
    AVStream setCodecParameters(AVCodecParameters parameters);
}
