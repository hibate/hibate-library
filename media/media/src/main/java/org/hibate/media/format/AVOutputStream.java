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
import org.hibate.media.codec.AVPacket;
import org.hibate.media.util.AVDictionary;

import java.io.Closeable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Hibate
 * Created on 2024/03/29.
 */
public interface AVOutputStream extends Closeable {

    /**
     * read-only
     */
    int AVIO_FLAG_READ = 1;
    /**
     * write-only
     */
    int AVIO_FLAG_WRITE = 2;
    /**
     * read-write pseudo flag
     */
    int AVIO_FLAG_READ_WRITE = AVIO_FLAG_READ | AVIO_FLAG_WRITE;

    @IntDef({AVIO_FLAG_READ, AVIO_FLAG_WRITE, AVIO_FLAG_READ_WRITE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Flags {}

    /**
     * Allocate the stream private data and write the stream header to
     * an output media file
     * @return 返回状态: ret < 0 - 失败
     */
    int write();

    /**
     * Allocate the stream private data and write the stream header to
     * an output media file
     * @param dictionary An ::AVDictionary filled with AVFormatContext and
     *                   muxer-private options.
     *                   On return this parameter will be destroyed and replaced with
     *                   a dict containing options that were not found. May be NULL.
     * @return 返回状态: ret < 0 - 失败
     */
    int write(@Nullable AVDictionary dictionary);

    /**
     * Write a packet to an output media file.
     * <p>
     * This function passes the packet directly to the muxer, without any buffering
     * or reordering. The caller is responsible for correctly interleaving the
     * packets if the format requires it. Callers that want libavformat to handle
     * the interleaving should call av_interleaved_write_frame() instead of this
     * function.
     *
     * @param packet 数据包
     * @return 返回状态: ret < 0 - 失败
     */
    int write(@NonNull AVPacket packet);

    /**
     * Write a packet to an output media file ensuring correct interleaving.
     * <p>
     * This function will buffer the packets internally as needed to make sure the
     * packets in the output file are properly interleaved, usually ordered by
     * increasing dts. Callers doing their own interleaving should call
     * av_write_frame() instead of this function.
     * <p>
     * Using this function instead of av_write_frame() can give muxers advance
     * knowledge of future packets, improving e.g. the behaviour of the mp4
     * muxer for VFR content in fragmenting mode.
     *
     * @param packet 数据包
     * @param interleaved 交错写入
     * @return 返回状态: ret < 0 - 失败
     */
    int write(@NonNull AVPacket packet, boolean interleaved);

    /**
     * Write the stream trailer to an output media file and free the
     * file private data.
     * <p>
     * May only be called after a successful call to avformat_write_header.
     */
    void close();
}
