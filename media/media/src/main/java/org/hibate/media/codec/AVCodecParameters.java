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

package org.hibate.media.codec;

import androidx.annotation.NonNull;
import org.hibate.media.util.AVChannelLayout;
import org.hibate.media.util.AVPixelFormat;
import org.hibate.media.util.AVRational;
import org.hibate.media.util.AVSampleFormat;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
public interface AVCodecParameters {

    /**
     * 设置编解码器上下文: avcodec_parameters_from_context
     * @param context 编解码器上下文
     * @return {@link AVCodecParameters}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVCodecParameters setCodecContext(@NonNull AVCodecContext context);

    /**
     * 获取编解码数据类型
     * @return 返回编解码数据类型
     */
    AVMediaType getMediaType();

    /**
     * 设置编解码数据类型
     * @param mediaType 编解码数据类型
     * @return {@link AVCodecParameters}
     */
    AVCodecParameters setMediaType(AVMediaType mediaType);

    /**
     * 获取编解码器 id
     * @return 返回编解码器 id
     */
    AVCodecID getCodecId();

    /**
     * 设置编解码器 id
     * @param id 编解码器 id
     * @return {@link AVCodecParameters}
     */
    AVCodecParameters setCodecId(AVCodecID id);

    /**
     * 获取编解码器的附加信息
     * @return 返回编解码器的附加信息
     */
    int getCodecTag();

    /**
     * 设置编解码器的附加信息
     * @param codecTag 编解码器的附加信息
     * @return {@link AVCodecParameters}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVCodecParameters setCodecTag(int codecTag);

    /**
     * 获取解码器所需的额外二进制数据大小
     * @return 返回数据大小
     */
    int getExtraDataSize();

    /**
     * 获取像素格式, 仅限视频数据
     * @return 像素格式
     */
    AVPixelFormat getPixelFormat();

    /**
     * 设置像素格式, 仅限视频数据
     * @param format 像素格式
     * @return {@link AVCodecParameters}
     */
    AVCodecParameters setPixelFormat(AVPixelFormat format);

    /**
     * 获取采样格式, 仅限音频数据
     * @return 返回采样格式
     */
    AVSampleFormat getSampleFormat();

    /**
     * 设置采样格式, 仅限音频数据
     * @param format 采样格式
     * @return {@link AVCodecParameters}
     */
    AVCodecParameters setSampleFormat(AVSampleFormat format);

    /**
     * 获取编解码数据平均比特率, 单位 bit/s
     * @return 返回编解码数据平均比特率
     */
    long getBitRate();

    /**
     * 设置编解码数据平均比特率, 单位 bit/s`
     * @param bitRate 比特率
     * @return {@link AVCodecParameters}
     */
    AVCodecParameters setBitRate(long bitRate);

    /**
     * 获取视频宽度, 仅限视频数据
     * @return 返回视频宽度
     */
    int getWidth();

    /**
     * 设置视频宽度, 仅限视频数据
     * @param width 宽度
     * @return {@link AVCodecParameters}
     */
    AVCodecParameters setWidth(int width);

    /**
     * 获取视频高度, 仅限视频数据
     * @return 返回视频高度
     */
    int getHeight();

    /**
     * 设置视频高度, 仅限视频数据
     * @param height 高度
     * @return {@link AVCodecParameters}
     */
    AVCodecParameters setHeight(int height);

    /**
     * 获取视频宽高比: width / height, 仅限视频数据
     * @return 返回视频宽高比
     */
    AVRational getAspectRatio();

    /**
     * 设置视频宽高比: width / height, 仅限视频数据
     * @param aspectRatio 视频宽高比
     * @return {@link AVCodecParameters}
     */
    AVCodecParameters setAspectRatio(AVRational aspectRatio);

    /**
     * 获取音频采样率, 仅限音频数据
     * @return 返回音频采样率
     */
    int getSampleRate();

    /**
     * 设置音频采样率, 仅限音频数据
     * @param sampleRate 采样率
     * @return {@link AVCodecParameters}
     */
    AVCodecParameters setSampleRate(int sampleRate);

    /**
     * 获取声道数, 仅限音频数据
     * @return 返回声道数
     */
    AVChannelLayout getChannelLayout();

    /**
     * 设置声道数, 仅限音频数据
     * @param channels 声道数
     * @return {@link AVCodecParameters}
     */
    AVCodecParameters setChannelLayout(AVChannelLayout channels);
}
