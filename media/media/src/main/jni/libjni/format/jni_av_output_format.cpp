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
 * jni_av_output_format.cpp
 *
 * @author Hibate
 * Created on 2024/03/28.
 */

#include <cstdio>

#include <jni_util.h>
#include <exceptions.h>

#include <text/text_utils.h>

#include "jni_av_output_format.h"
#include "codec/jni_av_codec_id.h"

#define className "org/hibate/media/format/impl/AVOutputFormatImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
};

static struct fields_t fields;

void avformat_output_format_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVOutputFormatImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVOutputFormatImpl.ctx field");
            return;
        }
    }
}

AVFormatContext* avformat_output_format_getAVFormatContext(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVFormatContext*) env->GetLongField(thiz, fields.context);
}

void avformat_output_format_setAVFormatContext(JNIEnv* env, jobject thiz, AVFormatContext* ctx) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) ctx);
}

jobject avformat_output_format_alloc(JNIEnv* env, AVFormatContext* ctx) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    jobject object = env->NewObject(env->FindClass(className), fields.constructor);
    avformat_output_format_setAVFormatContext(env, object, ctx);
    return object;
}

jstring avformat_output_format_getName(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_output_format_getAVFormatContext(env, thiz);
    if ((nullptr == ctx) || (nullptr == ctx->oformat)) {
        return nullptr;
    }

    const char* name = ctx->oformat->name;
    return TextUtils::ToUTF8(env, name);
}

jstring avformat_output_format_getLongName(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_output_format_getAVFormatContext(env, thiz);
    if ((nullptr == ctx) || (nullptr == ctx->oformat)) {
        return nullptr;
    }

    const char* name = ctx->oformat->long_name;
    return TextUtils::ToUTF8(env, name);
}

jstring avformat_output_format_getMimeType(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_output_format_getAVFormatContext(env, thiz);
    if ((nullptr == ctx) || (nullptr == ctx->oformat)) {
        return nullptr;
    }

    const char* name = ctx->oformat->mime_type;
    return TextUtils::ToUTF8(env, name);
}

jstring avformat_output_format_getAudioCodecId(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_output_format_getAVFormatContext(env, thiz);
    if ((nullptr == ctx) || (nullptr == ctx->oformat)) {
        return nullptr;
    }
    const char* name = avcodec_id_getName(ctx->oformat->audio_codec);
    return TextUtils::ToUTF8(env, name);
}

jstring avformat_output_format_getVideoCodecId(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_output_format_getAVFormatContext(env, thiz);
    if ((nullptr == ctx) || (nullptr == ctx->oformat)) {
        return nullptr;
    }
    const char* name = avcodec_id_getName(ctx->oformat->video_codec);
    return TextUtils::ToUTF8(env, name);
}

jstring avformat_output_format_getSubtitleCodecId(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_output_format_getAVFormatContext(env, thiz);
    if ((nullptr == ctx) || (nullptr == ctx->oformat)) {
        return nullptr;
    }
    const char* name = avcodec_id_getName(ctx->oformat->subtitle_codec);
    return TextUtils::ToUTF8(env, name);
}

jint avformat_output_format_getFlags(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_output_format_getAVFormatContext(env, thiz);
    if ((nullptr == ctx) || (nullptr == ctx->oformat)) {
        return AVFMT_NOFILE;
    }

    return (jint) ctx->oformat->flags;
}

static JNINativeMethod methods[] = {
    {(char*) "getName", (char*) "()Ljava/lang/String;", (void*) avformat_output_format_getName},
    {(char*) "getLongName", (char*) "()Ljava/lang/String;", (void*) avformat_output_format_getLongName},
    {(char*) "getMimeType", (char*) "()Ljava/lang/String;", (void*) avformat_output_format_getMimeType},
    {(char*) "_getAudioCodecId", (char*) "()Ljava/lang/String;", (void*) avformat_output_format_getAudioCodecId},
    {(char*) "_getVideoCodecId", (char*) "()Ljava/lang/String;", (void*) avformat_output_format_getVideoCodecId},
    {(char*) "_getSubtitleCodecId", (char*) "()Ljava/lang/String;", (void*) avformat_output_format_getSubtitleCodecId},
    {(char*) "getFlags", (char*) "()I", (void*) avformat_output_format_getFlags}
};

int register_avformat_output_format(JNIEnv* env) {
    avformat_output_format_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avformat_output_format(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
}

MethodLoader avFormatOutputFormatLoader = {register_avformat_output_format, unregister_avformat_output_format};
