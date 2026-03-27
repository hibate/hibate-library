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
 * jni_av_codec.cpp
 *
 * @author Hibate
 * Created on 2024/03/25.
 */

#include <cstdio>

extern "C" {
#include <libavutil/pixdesc.h>
}

#include <jni_util.h>
#include <exceptions.h>

#include <text/text_utils.h>

#include "jni_av_codec.h"
#include "jni_av_codec_context.h"
#include "jni_av_codec_id.h"

#define className "org/hibate/media/codec/impl/AVCodecImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
};

static struct fields_t fields;

void avcodec_codec_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVCodecImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVCodecImpl.ctx field");
            return;
        }
    }
}

AVCodec* avcodec_codec_getAVCodec(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return reinterpret_cast<AVCodec *>(env->GetLongField(thiz, fields.context));
}

void avcodec_codec_setAVCodec(JNIEnv* env, jobject thiz, const AVCodec* codec) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, reinterpret_cast<jlong>(codec));
}

jobject avcodec_codec_alloc(JNIEnv* env, const AVCodec* codec) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    jobject object = env->NewObject(env->FindClass(className), fields.constructor);
    avcodec_codec_setAVCodec(env, object, codec);
    return object;
}

jstring avcodec_codec_getCodecName(JNIEnv* env, jobject thiz) {
    const AVCodec* codec = avcodec_codec_getAVCodec(env, thiz);
    if (nullptr == codec) {
        throwException(env, ILLEGAL_STATE_EXCEPTION, "AVCodec native ctx is null");
        return nullptr;
    }

    const char* name = avcodec_get_name(codec->id);
    return TextUtils::ToUTF8(env, name);
}

jint avcodec_codec_getCapabilities(JNIEnv* env, jobject thiz) {
    const AVCodec* codec = avcodec_codec_getAVCodec(env, thiz);
    if (nullptr == codec) {
        return 0;
    }
    return codec->capabilities;
}

jobject avcodec_codec_getCodecContext(JNIEnv* env, jobject thiz) {
    AVCodec* codec = avcodec_codec_getAVCodec(env, thiz);
    if (nullptr == codec) {
        throwException(env, ILLEGAL_STATE_EXCEPTION, "AVCodec native ctx is null");
        return nullptr;
    }

    jobject object = avcodec_context_alloc(env, codec, thiz);
    if (nullptr == object) {
        throwException(env, RUNTIME_EXCEPTION, "AVCodecContext allocate failed");
        return nullptr;
    }
    return object;
}

jstring avcodec_codec_getCodecId(JNIEnv* env, jobject thiz) {
    AVCodec* codec = avcodec_codec_getAVCodec(env, thiz);
    if (nullptr == codec) {
        throwException(env, ILLEGAL_STATE_EXCEPTION, "AVCodec native ctx is null");
        return nullptr;
    }

    const char* name = avcodec_id_getName(codec->id);
    return TextUtils::ToUTF8(env, name);
}

jint avcodec_codec_getMediaType(JNIEnv* env, jobject thiz) {
    const AVCodec* codec = avcodec_codec_getAVCodec(env, thiz);
    if (nullptr == codec) {
        throwException(env, ILLEGAL_STATE_EXCEPTION, "AVCodec native ctx is null");
        return AVMEDIA_TYPE_UNKNOWN;
    }

    const AVMediaType mediaType = avcodec_get_type(codec->id);
    return mediaType;
}

jboolean avcodec_codec_isEncoder(JNIEnv* env, jobject thiz) {
    const AVCodec* codec = avcodec_codec_getAVCodec(env, thiz);
    if (nullptr == codec) {
        throwException(env, ILLEGAL_STATE_EXCEPTION, "AVCodec native ctx is null");
        return JNI_FALSE;
    }

    return av_codec_is_encoder(codec) ? JNI_TRUE : JNI_FALSE;
}

jobjectArray avcodec_codec_getPixelFormats(JNIEnv* env, jobject thiz) {
    const AVCodec* codec = avcodec_codec_getAVCodec(env, thiz);
    if (nullptr == codec) {
        return nullptr;
    }

    const AVPixelFormat *pix_fmts = nullptr;
    const int ret = avcodec_get_supported_config(nullptr, codec, AV_CODEC_CONFIG_PIX_FORMAT, 0,
        (const void **)(&pix_fmts), nullptr);
    if ((ret < 0) || (nullptr == pix_fmts)) {
        return nullptr;
    }

    int size = 0;
    while (pix_fmts[size] != AV_PIX_FMT_NONE) {
        size++;
    }

    jobjectArray array = env->NewObjectArray(size, env->FindClass("java/lang/String"), nullptr);
    for (int i = 0; i < size; i++) {
        const char* name = av_get_pix_fmt_name(pix_fmts[i]);
        jstring fmt = TextUtils::ToUTF8(env, name);
        env->SetObjectArrayElement(array, i, fmt);
        env->DeleteLocalRef(fmt);
    }
    return array;
}

jobjectArray avcodec_codec_getSampleFormats(JNIEnv* env, jobject thiz) {
    const AVCodec* codec = avcodec_codec_getAVCodec(env, thiz);
    if (nullptr == codec) {
        return nullptr;
    }

    const AVSampleFormat *sample_fmts = nullptr;
    const int ret = avcodec_get_supported_config(nullptr, codec, AV_CODEC_CONFIG_SAMPLE_FORMAT, 0,
        (const void **)(&sample_fmts), nullptr);
    if ((ret < 0) || (nullptr == sample_fmts)) {
        return nullptr;
    }

    int size = 0;
    while (sample_fmts[size] != AV_SAMPLE_FMT_NONE) {
        size++;
    }

    jobjectArray array = env->NewObjectArray(size, env->FindClass("java/lang/String"), nullptr);
    for (int i = 0; i < size; i++) {
        const char* name = av_get_sample_fmt_name(sample_fmts[i]);
        jstring fmt = TextUtils::ToUTF8(env, name);
        env->SetObjectArrayElement(array, i, fmt);
        env->DeleteLocalRef(fmt);
    }
    return array;
}

static JNINativeMethod methods[] = {
    {(char*) "getCodecName", (char*) "()Ljava/lang/String;", (void*) avcodec_codec_getCodecName},
    {(char*) "getCapabilities", (char*) "()I", (void*) avcodec_codec_getCapabilities},
    {(char*) "getCodecContext", (char*) "()Lorg/hibate/media/codec/AVCodecContext;", (void*) avcodec_codec_getCodecContext},
    {(char*) "_getCodecId", (char*) "()Ljava/lang/String;", (void*) avcodec_codec_getCodecId},
    {(char*) "_getMediaType", (char*) "()I", (void*) avcodec_codec_getMediaType},
    {(char*) "_isEncoder", (char*) "()Z", (void*) avcodec_codec_isEncoder},
    {(char*) "_getPixelFormats", (char*) "()[Ljava/lang/String;", (void*) avcodec_codec_getPixelFormats},
    {(char*) "_getSampleFormats", (char*) "()[Ljava/lang/String;", (void*) avcodec_codec_getSampleFormats},
};

int register_avcodec(JNIEnv* env) {
    avcodec_codec_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avcodec(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
}

MethodLoader avCodecLoader = {register_avcodec, unregister_avcodec};
