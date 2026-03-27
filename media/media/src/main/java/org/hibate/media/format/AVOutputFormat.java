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
import org.hibate.media.codec.AVCodecID;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Hibate
 * Created on 2024/03/28.
 */
public interface AVOutputFormat {

    int AVFMT_NOFILE = 0x0001;
    /**
     * Needs '%d' in filename.
     */
    int AVFMT_NEEDNUMBER = 0x0002;
    /**
     * Format wants global header.
     */
    int AVFMT_GLOBALHEADER = 0x0040;
    /**
     * Format does not need / have any timestamps.
     */
    int AVFMT_NOTIMESTAMPS = 0x0080;
    /**
     * Format allows variable fps.
     */
    int AVFMT_VARIABLE_FPS = 0x0400;
    /**
     * Format does not need width/height
     */
    int AVFMT_NODIMENSIONS = 0x0800;
    /**
     * Format does not require any streams
     */
    int AVFMT_NOSTREAMS = 0x1000;
    /**
     * Format does not require strictly
     * increasing timestamps, but they must
     * still be monotonic
     */
    int AVFMT_TS_NONSTRICT = 0x20000;
    /**
     * Format allows muxing negative
     * timestamps. If not set the timestamp
     * will be shifted in av_write_frame and
     * av_interleaved_write_frame so they
     * start from 0.
     * The user or muxer can override this through
     * AVFormatContext.avoid_negative_ts
     */
    int AVFMT_TS_NEGATIVE = 0x40000;

    @IntDef({AVFMT_NOFILE, AVFMT_NEEDNUMBER, AVFMT_GLOBALHEADER, AVFMT_NOTIMESTAMPS, AVFMT_VARIABLE_FPS,
            AVFMT_NODIMENSIONS, AVFMT_NOSTREAMS, AVFMT_TS_NONSTRICT, AVFMT_TS_NEGATIVE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Flags {}

    /**
     * 获取以逗号分隔的格式名称列表. 新名称可以附加一个小凸起
     * @return 返回以逗号分隔的格式名称列表
     */
    String getName();

    /**
     * 获取格式的描述性名称，更便于阅读
     * @return 返回格式的描述性名称
     */
    String getLongName();

    /**
     * 获取以逗号分隔的 mime 类型列表. 用于在探测时检查匹配的 mime 类型
     * @return 返回以逗号分隔的 mime 类型列表
     */
    String getMimeType();

    /**
     * 默认音频编码器 id
     * @return 返回音频编码器 id
     */
    AVCodecID getAudioCodecId();

    /**
     * 默认视频编码器 id
     * @return 返回视频编码器 id
     */
    AVCodecID getVideoCodecId();

    /**
     * 默认字幕编码器 id
     * @return 返回字幕编码器 id
     */
    AVCodecID getSubtitleCodecId();

    /**
     * 获取数据标记
     * @return 返回数据标记
     */
    @Flags
    int getFlags();
}
