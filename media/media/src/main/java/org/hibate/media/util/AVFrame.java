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

package org.hibate.media.util;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.io.Closeable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
public interface AVFrame extends Closeable {

    int FF_LAMBDA_MAX = 256 * 128 - 1;

    /**
     * The frame data may be corrupted, e.g. due to decoding errors.
     */
    int AV_FRAME_FLAG_CORRUPT = 1;
    /**
     * A flag to mark frames that are keyframes.
     */
    int AV_FRAME_FLAG_KEY = 1 << 1;
    /**
     * A flag to mark the frames which need to be decoded, but shouldn't be output.
     */
    int AV_FRAME_FLAG_DISCARD = (1 << 2);
    /**
     * A flag to mark frames whose content is interlaced.
     */
    int AV_FRAME_FLAG_INTERLACED = (1 << 3);
    /**
     * A flag to mark frames where the top field is displayed first if the content
     * is interlaced.
     */
    int AV_FRAME_FLAG_TOP_FIELD_FIRST = (1 << 4);

    @IntDef({AV_FRAME_FLAG_CORRUPT, AV_FRAME_FLAG_KEY, AV_FRAME_FLAG_DISCARD, AV_FRAME_FLAG_INTERLACED, AV_FRAME_FLAG_TOP_FIELD_FIRST})
    @Retention(RetentionPolicy.SOURCE)
    @interface Flags {}

    /**
     * 获取视频宽度, 仅限视频数据
     * @return 返回视频宽度
     */
    int getWidth();

    /**
     * 获取视频高度, 仅限视频数据
     * @return 返回视频高度
     */
    int getHeight();

    /**
     * 获取数据标记
     * @return 返回数据标记
     */
    @Flags
    int getFlags();

    /**
     * format of the frame, -1 if unknown or unset
     * Values correspond to enum {@link AVPixelFormat} for video frames,
     * enum {@link AVSampleFormat} for audio
     */
    int getFormat();

    /**
     * 获取图片类型
     * @return 返回图片类型
     */
    @NonNull
    AVPictureType getPictureType();

    /**
     * 设置图片类型
     * @param pictureType 图片类型
     * @return {@link AVFrame}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVFrame setPictureType(AVPictureType pictureType);

    /**
     * Presentation timestamp in time_base units (time when frame should be shown to user).
     * @see org.hibate.media.util.AVUtils#AV_NOPTS_VALUE
     */
    long getPts();

    /**
     * Presentation timestamp in time_base units (time when frame should be shown to user).
     * @see org.hibate.media.util.AVUtils#AV_NOPTS_VALUE
     */
    AVFrame setPts(long pts);

    /**
     * DTS copied from the AVPacket that triggered returning this frame. (if frame threading isn't used)
     * This is also the Presentation time of this AVFrame calculated from
     * only AVPacket.dts values without pts values.
     * @see org.hibate.media.util.AVUtils#AV_NOPTS_VALUE
     */
    long getDts();

    /**
     * Time base for the timestamps in this frame.
     * In the future, this field may be set on frames output by decoders or
     * filters, but its value will be by default ignored on input to encoders
     * or filters.
     */
    AVRational getTimebase();

    /**
     * Time base for the timestamps in this frame.
     * In the future, this field may be set on frames output by decoders or
     * filters, but its value will be by default ignored on input to encoders
     * or filters.
     */
    AVFrame setTimebase(AVRational rational);

    /**
     * quality (between 1 (good) and {@link #FF_LAMBDA_MAX} (bad))
     * @see #FF_LAMBDA_MAX
     */
    int getQuality();

    /**
     * 获取音频采样率, 仅限音频数据
     * @return 返回音频采样率
     */
    int getSampleRate();

    /**
     * frame timestamp estimated using various heuristics, in stream time base
     * - encoding: unused
     * - decoding: set by libavcodec, read by user.
     */
    long getBestEffortTimestamp();

    /**
     * 获取声道数
     * @return 返回声道数
     */
    AVChannelLayout getChannelLayout();

    /**
     * Duration of the frame, in the same units as pts. 0 if unknown.
     */
    long getDuration();

    /**
     * Unreference all the buffers referenced by frame and reset the frame fields.
     *
     * @return {@link AVFrame}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVFrame unref();

    /**
     * 释放资源
     */
    void close();
}
