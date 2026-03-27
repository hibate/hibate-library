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
 * jni_av_input_stream.cpp
 *
 * @author Hibate
 * Created on 2024/03/27.
 */

extern "C" {
#include <libavformat/avformat.h>
}

#include <cstdio>

#include <jni_util.h>

#include "jni_av_format_context.h"
#include "jni_av_input_stream.h"
#include "codec/jni_av_packet.h"

#define className "org/hibate/media/format/impl/AVInputStreamImpl"

jint avformat_input_stream_read(JNIEnv* env, jobject thiz, jobject jContext, jobject jPkt) {
    AVFormatContext* ctx = (nullptr == jContext) ? nullptr : avformat_context_getAVFormatContext(env, jContext);
    if (nullptr == ctx) {
        return AVERROR(EAGAIN);
    }

    AVPacket* packet = (nullptr == jPkt) ? nullptr : avpacket_getAVPacket(env, jPkt);
    if (nullptr == packet) {
        return AVERROR(EAGAIN);
    }

    int ret = av_read_frame(ctx, packet);
    if ((ret < 0) && (ret == AVERROR_EOF || avio_feof(ctx->pb))) {
        return AVERROR_EOF;
    }
    return ret;
}

static JNINativeMethod methods[] = {
    {(char*) "_read", (char*) "(Lorg/hibate/media/format/AVFormatContext;Lorg/hibate/media/codec/AVPacket;)I", (void*) avformat_input_stream_read},
};

int register_avformat_input_stream(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avformat_input_stream(JNIEnv* env) {
}

MethodLoader avFormatInputStreamLoader = {register_avformat_input_stream, unregister_avformat_input_stream};
