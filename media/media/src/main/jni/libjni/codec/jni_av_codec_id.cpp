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

/**
 * jni_av_codec_id.cpp
 *
 * @author Hibate
 * Created on 2026/03/25.
 */

#include "jni_av_codec_id.h"

const AVCodecDescriptor* avcodec_id_getAVCodecDescriptor(const char* name) {
    if (nullptr == name) {
        return nullptr;
    }

    if (strcmp(name, "AV_CODEC_ID_PCM_ZORK") == 0) {
        return avcodec_descriptor_get(AV_CODEC_ID_PCM_ZORK);
    }
    if (strcmp(name, "AV_CODEC_ID_PROBE") == 0) {
        return avcodec_descriptor_get(AV_CODEC_ID_PROBE);
    }
    if (strcmp(name, "AV_CODEC_ID_MPEG4SYSTEMS") == 0) {
        return avcodec_descriptor_get(AV_CODEC_ID_MPEG4SYSTEMS);
    }
    if (strcmp(name, "AV_CODEC_ID_FFMETADATA") == 0) {
        return avcodec_descriptor_get(AV_CODEC_ID_FFMETADATA);
    }
    return avcodec_descriptor_get_by_name(name);
}

const char* avcodec_id_getName(enum AVCodecID codec) {
    if (AV_CODEC_ID_PCM_ZORK == codec) {
        return "AV_CODEC_ID_PCM_ZORK";
    }
    if (AV_CODEC_ID_PROBE == codec) {
        return "AV_CODEC_ID_PROBE";
    }
    if (AV_CODEC_ID_MPEG4SYSTEMS == codec) {
        return "AV_CODEC_ID_MPEG4SYSTEMS";
    }
    if (AV_CODEC_ID_FFMETADATA == codec) {
        return "AV_CODEC_ID_FFMETADATA";
    }
    const AVCodecDescriptor *descriptor = avcodec_descriptor_get(codec);
    return (nullptr == descriptor) ? nullptr : descriptor->name;
}
