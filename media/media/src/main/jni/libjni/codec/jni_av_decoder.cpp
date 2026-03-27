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
 * jni_av_decoder.cpp
 *
 * @author Hibate
 * Created on 2024/03/28.
 */

#include <cstdio>

#include <jni_util.h>
#include <exceptions.h>

#include "jni_av_packet.h"
#include "jni_av_decoder.h"
#include "util/jni_av_frame.h"

#define className "org/hibate/media/codec/impl/AVDecoderImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
    jfieldID codec;
};

static struct fields_t fields;

void avcodec_decoder_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVDecoderImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVDecoderImpl.ctx field");
            return;
        }
    }
    if (nullptr == fields.codec) {
        fields.codec = env->GetFieldID(env->FindClass(className), "codecContext",
                                       "Lorg/hibate/media/codec/AVCodecContext;");
        if (nullptr == fields.codec) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVDecoderImpl.codecContext field");
            return;
        }
    }
}

AVCodecContext* avcodec_decoder_getAVCodecContext(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVCodecContext*) env->GetLongField(thiz, fields.context);
}

void avcodec_decoder_setAVCodecContext(JNIEnv* env, jobject thiz, AVCodecContext* ctx) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) ctx);
}

jobject avcodec_decoder_alloc(JNIEnv* env, jobject thiz, AVCodecContext* ctx) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    jobject object = env->NewObject(env->FindClass(className), fields.constructor);
    avcodec_decoder_setAVCodecContext(env, object, ctx);
    env->SetObjectField(object, fields.codec, thiz);
    return object;
}

jint avcodec_decoder_send(JNIEnv* env, jobject thiz, jobject jPkt) {
    AVCodecContext* ctx = avcodec_decoder_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(EINVAL);
    }

    AVPacket* packet = (nullptr == jPkt) ? nullptr : avpacket_getAVPacket(env, jPkt);
    if (nullptr == packet) {
        return AVERROR(EINVAL);
    }

    return avcodec_send_packet(ctx, packet);
}

jint avcodec_decoder_receive(JNIEnv* env, jobject thiz, jobject jFrame) {
    AVCodecContext* ctx = avcodec_decoder_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(EINVAL);
    }

    AVFrame* frame = (nullptr == jFrame) ? nullptr : avutil_frame_getAVFrame(env, jFrame);
    if (nullptr == frame) {
        return AVERROR(EINVAL);
    }

    return avcodec_receive_frame(ctx, frame);
}

jint avcodec_decoder_flush(JNIEnv* env, jobject thiz) {
    AVCodecContext* ctx = avcodec_decoder_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return avcodec_send_packet(ctx, nullptr);
}

static JNINativeMethod methods[] = {
    {(char*) "send", (char*) "(Lorg/hibate/media/codec/AVPacket;)I", (void*) avcodec_decoder_send},
    {(char*) "receive", (char*) "(Lorg/hibate/media/util/AVFrame;)I", (void*) avcodec_decoder_receive},
    {(char*) "flush", (char*) "()I", (void*) avcodec_decoder_flush}
};

int register_avcodec_decoder(JNIEnv* env) {
    avcodec_decoder_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avcodec_decoder(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
    fields.codec = nullptr;
}

MethodLoader avCodecDecoderLoader = {register_avcodec_decoder, unregister_avcodec_decoder};
