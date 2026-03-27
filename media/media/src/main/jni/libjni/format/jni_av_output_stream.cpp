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
 * jni_av_output_stream.cpp
 *
 * @author Hibate
 * Created on 2024/03/29.
 */

extern "C" {
#include <libavformat/avformat.h>
}

#include <cstdio>

#include <jni_util.h>
#include <exceptions.h>

#include "jni_av_format_context.h"
#include "jni_av_output_stream.h"
#include "codec/jni_av_packet.h"
#include "util/jni_av_config.h"
#include "util/jni_av_dictionary.h"

#define className "org/hibate/media/format/impl/AVOutputStreamImpl"

jint avformat_output_stream_open(JNIEnv* env, jobject thiz, jobject jContext, jint jFlags) {
    AVFormatContext* ctx = (nullptr == jContext) ? nullptr : avformat_context_getAVFormatContext(env, jContext);
    if (nullptr == ctx) {
        return AVERROR(EINVAL);
    }

    if (ctx->oformat->flags & AVFMT_NOFILE) {
        return 0;
    }
    int ret = avio_open2(&ctx->pb, (const char*) ctx->url, jFlags, nullptr, nullptr);
    if (ret < 0) {
        char buffer[MEDIA_ERROR_BUFFER_MAX_LENGTH] = {0};
        char reason[MEDIA_ERROR_MAX_STRING_SIZE] = {0};
        av_make_error_string(reason, MEDIA_ERROR_MAX_STRING_SIZE, ret);
        snprintf(buffer, MEDIA_ERROR_BUFFER_MAX_LENGTH, "Open output stream failed. url: %s, ret: %d, msg: %s", ctx->url, ret, reason);
        throwException(env, IO_EXCEPTION, buffer);
        return AVERROR(EINVAL);
    }
    return ret;
}

jint avformat_output_stream_close(JNIEnv* env, jobject thiz, jobject jContext) {
    AVFormatContext* ctx = (nullptr == jContext) ? nullptr : avformat_context_getAVFormatContext(env, jContext);
    if (nullptr == ctx) {
        return AVERROR(EINVAL);
    }
    if (ctx->oformat && !(ctx->oformat->flags & AVFMT_NOFILE)) {
        avio_closep(&ctx->pb);
    }
    return 0;
}

jint avformat_output_stream_write_header(JNIEnv* env, jobject thiz, jobject jContext, jobject jDictionary) {
    AVFormatContext* ctx = (nullptr == jContext) ? nullptr : avformat_context_getAVFormatContext(env, jContext);
    if (nullptr == ctx) {
        return AVERROR(EINVAL);
    }

    AVDictionary* dictionary = nullptr;
    avutil_dictionary_getAVDictionary(env, jDictionary, &dictionary);
    return avformat_write_header(ctx, &dictionary);
}

jint avformat_output_stream_write_frame(JNIEnv* env, jobject thiz, jobject jContext, jobject jPkt, jboolean jInterleaved) {
    AVFormatContext* ctx = (nullptr == jContext) ? nullptr : avformat_context_getAVFormatContext(env, jContext);
    if (nullptr == ctx) {
        return AVERROR(EINVAL);
    }

    AVPacket* packet = (nullptr == jPkt) ? nullptr : avpacket_getAVPacket(env, jPkt);
    if (nullptr == packet) {
        return AVERROR(EINVAL);
    }

    return (jInterleaved == JNI_TRUE) ? av_interleaved_write_frame(ctx, packet) : av_write_frame(ctx, packet);
}

jint avformat_output_stream_write_trailer(JNIEnv* env, jobject thiz, jobject jContext) {
    AVFormatContext* ctx = (nullptr == jContext) ? nullptr : avformat_context_getAVFormatContext(env, jContext);
    if (nullptr == ctx) {
        return AVERROR(EINVAL);
    }

    return av_write_trailer(ctx);
}

static JNINativeMethod methods[] = {
    {(char*) "_open", (char*) "(Lorg/hibate/media/format/AVFormatContext;I)I", (void*) avformat_output_stream_open},
    {(char*) "_close", (char*) "(Lorg/hibate/media/format/AVFormatContext;)I", (void*) avformat_output_stream_close},
    {(char*) "_write", (char*) "(Lorg/hibate/media/format/AVFormatContext;Lorg/hibate/media/util/AVDictionary;)I", (void*) avformat_output_stream_write_header},
    {(char*) "_write", (char*) "(Lorg/hibate/media/format/AVFormatContext;Lorg/hibate/media/codec/AVPacket;Z)I", (void*) avformat_output_stream_write_frame},
    {(char*) "_write", (char*) "(Lorg/hibate/media/format/AVFormatContext;)I", (void*) avformat_output_stream_write_trailer}
};

int register_avformat_output_stream(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avformat_output_stream(JNIEnv* env) {
}

MethodLoader avFormatOutputStreamLoader = {register_avformat_output_stream, unregister_avformat_output_stream};
