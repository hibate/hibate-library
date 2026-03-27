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

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.hibate.media.util.*;

import java.io.Closeable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
public interface AVCodecContext extends Closeable {

    /**
     * Place global headers in extradata instead of every keyframe.
     */
    int AV_CODEC_FLAG_GLOBAL_HEADER = (1 << 22);

    @IntDef({AV_CODEC_FLAG_GLOBAL_HEADER})
    @Retention(RetentionPolicy.SOURCE)
    @interface Flags {}

    @NonNull
    AVCodec getCodec();

    /**
     * 设置编解码器上下文参数: avcodec_parameters_to_context
     * @param parameters 编解码器上下文参数
     * @return {@link AVCodecContext}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVCodecContext setCodecParameters(@NonNull AVCodecParameters parameters);

    /**
     * 获取编码数据类型
     * @return 返回编码数据类型
     */
    AVMediaType getMediaType();

    /**
     * 获取编码器 id
     * @return 返回编码器 id
     */
    AVCodecID getCodecId();

    /**
     * 获取编解码器的附加信息
     * @return 返回编解码器的附加信息
     */
    int getCodecTag();

    /**
     * 设置编解码器的附加信息
     * @param codecTag 编解码器的附加信息
     * @return {@link AVCodecContext}
     */
    AVCodecContext setCodecTag(int codecTag);

    /**
     * 获取帧时间戳的基本时间单位: 以秒为单位
     * @return 基本时间单位
     */
    AVRational getTimebase();

    /**
     * 设置帧时间戳的基本时间单位: 以秒为单位
     * @param rational 基本时间单位
     * @return {@link AVCodecContext}
     */
    AVCodecContext setTimebase(AVRational rational);

    /**
     * 获取表示 pkt_dts/pts、AVPacket.dts/pts 的基本时间单位
     * @return 基本时间单位
     */
    AVRational getPktTimebase();

    /**
     * 设置表示 pkt_dts/pts、AVPacket.dts/pts 的基本时间单位
     * encoding: unused.
     * decoding: set by user.
     * @param rational 基本时间单位
     * @return {@link AVCodecContext}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVCodecContext setPktTimebase(AVRational rational);

    /**
     * 获取帧率
     * @return 返回帧率
     */
    AVRational getFrameRate();

    /**
     * 设置帧率
     * @param frameRate 帧率
     * @return {@link AVCodecContext}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVCodecContext setFrameRate(AVRational frameRate);

    /**
     * 获取编码数据平均比特率, 单位 bit/s
     * @return 返回编码数据平均比特率
     */
    long getBitRate();

    /**
     * 设置编解码数据平均比特率, 单位 bit/s`
     * @param bitRate 比特率
     * @return {@link AVCodecContext}
     */
    AVCodecContext setBitRate(long bitRate);

    /**
     * 获取视频宽度, 仅限视频数据
     * @return 返回视频宽度
     */
    int getWidth();

    /**
     * 设置视频宽度, 仅限视频数据
     * @param width 宽度
     * @return {@link AVCodecContext}
     */
    AVCodecContext setWidth(int width);

    /**
     * 获取视频高度, 仅限视频数据
     * @return 返回视频高度
     */
    int getHeight();

    /**
     * 设置视频高度, 仅限视频数据
     * @param height 高度
     * @return {@link AVCodecContext}
     */
    AVCodecContext setHeight(int height);

    /**
     * 获取视频宽高比: width / height, 仅限视频数据
     * @return 返回视频宽高比
     */
    AVRational getAspectRatio();

    /**
     * 设置视频宽高比: width / height, 仅限视频数据
     * @return {@link AVCodecContext}
     */
    AVCodecContext setAspectRatio(AVRational aspectRatio);

    /**
     * 获取像素格式, 仅限视频数据
     * @return 像素格式
     */
    AVPixelFormat getPixelFormat();

    /**
     * 设置像素格式, 仅限视频数据
     * @param format 像素格式
     * @return {@link AVCodecContext}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVCodecContext setPixelFormat(AVPixelFormat format);

    /**
     * 获取音频采样率, 仅限音频数据
     * @return 返回音频采样率
     */
    int getSampleRate();

    /**
     * 设置音频采样率, 仅限音频数据
     * @param sampleRate 采样率
     * @return {@link AVCodecContext}
     */
    AVCodecContext setSampleRate(int sampleRate);

    /**
     * 获取采样格式, 仅限音频数据
     * @return 返回采样格式
     */
    AVSampleFormat getSampleFormat();

    /**
     * 设置采样格式, 仅限音频数据
     * @param format 采样格式
     * @return {@link AVCodecContext}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVCodecContext setSampleFormat(AVSampleFormat format);

    /**
     * 获取声道数
     * @return 返回声道数
     */
    AVChannelLayout getChannelLayout();

    /**
     * 设置声道数, 仅限音频数据
     * @param channels 声道数
     * @return {@link AVCodecContext}
     */
    AVCodecContext setChannelLayout(AVChannelLayout channels);

    /**
     * 获取音频帧中每通道的样本数, 仅限音频数据
     * @return 返回样本数
     */
    int getFrameSize();

    /**
     * 获取数据标记
     * @return 返回数据标记
     */
    @Flags
    int getFlags();

    /**
     * 设置数据标记
     * @param flags 数据标记
     * @return {@link AVCodecContext}
     */
    AVCodecContext setFlags(@Flags int flags);

    /**
     * 打开编解码器
     * @return 编解码器
     * @throws RuntimeException 打开失败
     */
    <T extends AVCoder> Optional<T> open();

    /**
     * 打开编解码器
     * @param dictionary 参数
     * @return 编解码器
     * @throws RuntimeException 打开失败
     */
    @NonNull
    <T extends AVCoder> Optional<T> open(@Nullable AVDictionary dictionary);

    /**
     * 关闭资源
     */
    void close();
}
