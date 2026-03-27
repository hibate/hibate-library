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
import org.hibate.media.util.AVRational;

import java.io.Closeable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
public interface AVPacket extends Closeable {

    /**
     * The packet contains a keyframe
     */
    int AV_PKT_FLAG_KEY = 0x0001;
    /**
     * The packet content is corrupted
     */
    int AV_PKT_FLAG_CORRUPT = 0x0002;
    /**
     * Flag is used to discard packets which are required to maintain valid
     * decoder state but are not required for output and should be dropped
     * after decoding.
     **/
    int AV_PKT_FLAG_DISCARD = 0x0004;
    /**
     * The packet comes from a trusted source.
     * <p>
     * Otherwise-unsafe constructs such as arbitrary pointers to data
     * outside the packet may be followed.
     */
    int AV_PKT_FLAG_TRUSTED = 0x0008;
    /**
     * Flag is used to indicate packets that contain frames that can
     * be discarded by the decoder.  I.e. Non-reference frames.
     */
    int AV_PKT_FLAG_DISPOSABLE = 0x0010;

    @IntDef({AV_PKT_FLAG_KEY, AV_PKT_FLAG_CORRUPT, AV_PKT_FLAG_DISCARD, AV_PKT_FLAG_TRUSTED, AV_PKT_FLAG_DISPOSABLE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Flags {}

    /**
     * 获取流索引
     * @return 返回流索引
     */
    int getIndex();

    /**
     * 设置流索引
     * - encoding: set by user
     * - decoding: read by user.
     * @param index 流索引
     * @return {@link AVPacket}
     */
    AVPacket setIndex(int index);

    /**
     * Presentation timestamp in AVStream->time_base units; the time at which
     * the decompressed packet will be presented to the user.
     * Can be AV_NOPTS_VALUE if it is not stored in the file.
     * pts MUST be larger or equal to dts as presentation cannot happen before
     * decompression, unless one wants to view hex dumps. Some formats misuse
     * the terms dts and pts/cts to mean something different. Such timestamps
     * must be converted to true pts/dts before they are stored in AVPacket.
     * @see org.hibate.media.util.AVUtils#AV_NOPTS_VALUE
     */
    long getPts();

    /**
     * Decompression timestamp in AVStream->time_base units; the time at which
     * the packet is decompressed.
     * Can be AV_NOPTS_VALUE if it is not stored in the file.
     * @see org.hibate.media.util.AVUtils#AV_NOPTS_VALUE
     */
    long getDts();

    /**
     * 获取数据大小
     * @return 返回数据大小
     */
    int getSize();

    /**
     * 获取数据标记
     * @return 返回数据标记
     */
    @Flags
    int getFlags();

    /**
     * 获取数据时长
     * @return 返回数据时长
     */
    long getDuration();

    /**
     * byte position in stream, -1 if unknown
     */
    long getPosition();

    /**
     * 设置流的字节位置, -1: 未知
     * @param position 字节位置
     * @return {@link AVPacket}
     */
    AVPacket setPosition(long position);

    /**
     * 将数据包中的有效时间字段 (timestamps / durations) 从一个时基转换为另一个时基, 具有未知值（AV_NOPTS_VALUE）的时间戳将被忽略
     * @param src 源时基
     * @param dst 目标时基
     * @return {@link AVPacket}
     * @see AVRational
     */
    @SuppressWarnings("UnusedReturnValue")
    AVPacket rescale(@NonNull AVRational src, @NonNull AVRational dst);

    /**
     * Wipe the packet.
     * <p>
     * Unreference the buffer referenced by the packet and reset the
     * remaining packet fields to their default values.
     *
     * @return {@link AVPacket}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVPacket unref();

    /**
     * 释放资源
     */
    void close();
}
