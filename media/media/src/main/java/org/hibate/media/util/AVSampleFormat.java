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

/**
 * libavutil/samplefmt.c
 * @author Hibate
 * Created on 2024/03/29.
 */
public enum AVSampleFormat {

    AV_SAMPLE_FMT_NONE(),
    AV_SAMPLE_FMT_U8("u8"),            ///< unsigned 8 bits
    AV_SAMPLE_FMT_S16("s16"),          ///< signed 16 bits
    AV_SAMPLE_FMT_S32("s32"),          ///< signed 32 bits
    AV_SAMPLE_FMT_FLT("flt"),          ///< float
    AV_SAMPLE_FMT_DBL("dbl"),          ///< double

    AV_SAMPLE_FMT_U8P("u8p"),          ///< unsigned 8 bits, planar
    AV_SAMPLE_FMT_S16P("s16p"),        ///< signed 16 bits, planar
    AV_SAMPLE_FMT_S32P("s32p"),        ///< signed 32 bits, planar
    AV_SAMPLE_FMT_FLTP("fltp"),        ///< float, planar
    AV_SAMPLE_FMT_DBLP("dblp"),        ///< double, planar
    AV_SAMPLE_FMT_S64("s64"),          ///< signed 64 bits
    AV_SAMPLE_FMT_S64P("s64p"),        ///< signed 64 bits, planar

    ;

    private final String name;

    AVSampleFormat() {
        this.name = null;
    }

    AVSampleFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static AVSampleFormat of(String name) {
        for (AVSampleFormat format : AVSampleFormat.values()) {
            if ((format.getName() != null) && format.getName().equals(name)) {
                return format;
            }
        }
        return AVSampleFormat.AV_SAMPLE_FMT_NONE;
    }
}
