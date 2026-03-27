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
 * jni_av_codec_context.cpp
 *
 * @author Hibate
 * Created on 2024/03/27.
 */

#define TAG "org.hibate.media.codec.AVCodecContext"

extern "C" {
#include <libavutil/pixdesc.h>
}

#include <jni_util.h>
#include <exceptions.h>
#include <logger.h>

#include <text/text_utils.h>

#include "jni_av_codec_context.h"
#include "jni_av_codec_id.h"
#include "jni_av_codec_parameters.h"
#include "jni_av_decoder.h"
#include "jni_av_encoder.h"
#include "util/jni_av_channel_layout.h"
#include "util/jni_av_dictionary.h"
#include "util/jni_av_rational.h"

#define className "org/hibate/media/codec/impl/AVCodecContextImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
    jfieldID codec;
};

static struct fields_t fields;

void avcodec_context_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVCodecContextImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVCodecContextImpl.ctx field");
            return;
        }
    }
    if (nullptr == fields.codec) {
        fields.codec = env->GetFieldID(env->FindClass(className), "codec", "Lorg/hibate/media/codec/AVCodec;");
        if (nullptr == fields.codec) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVCodecContextImpl.codec field");
            return;
        }
    }
}

AVCodecContext* avcodec_context_getAVCodecContext(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVCodecContext*) env->GetLongField(thiz, fields.context);
}

void avcodec_context_setAVCodecContext(JNIEnv* env, jobject thiz, AVCodecContext* ctx) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) ctx);
}

jobject avcodec_context_alloc(JNIEnv* env, AVCodec* codec, jobject thiz) {
    if ((nullptr == fields.constructor) || (nullptr == codec)) {
        return nullptr;
    }

    AVCodecContext* ctx = avcodec_alloc_context3(codec);
    if (nullptr == ctx) {
        return nullptr;
    }

    jobject object = env->NewObject(env->FindClass(className), fields.constructor);
    avcodec_context_setAVCodecContext(env, object, ctx);
    env->SetObjectField(object, fields.codec, thiz);
    return object;
}

jint avcodec_context_setCodecParameters(JNIEnv* env, jobject thiz, jobject jCodecpar) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        throwException(env, ILLEGAL_STATE_EXCEPTION, "AVCodecContext native ctx is null");
        return AVERROR(ENOENT);
    }

    if (nullptr == jCodecpar) {
        return AVERROR(EINVAL);
    }

    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, jCodecpar);
    if (nullptr == codecpar) {
        throwException(env, ILLEGAL_STATE_EXCEPTION, "AVCodecParameters native ctx is null");
        return AVERROR(ENOENT);
    }
    return avcodec_parameters_to_context(ctx, codecpar);
}

jint avcodec_context_getMediaType(JNIEnv* env, jobject thiz) {
    const AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVMEDIA_TYPE_UNKNOWN;
    }
    return (jint) ctx->codec_type;
}

jstring avcodec_context_getCodecId(JNIEnv* env, jobject thiz) {
    const AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return nullptr;
    }
    const char* name = avcodec_id_getName(ctx->codec_id);
    return TextUtils::ToUTF8(env, name);
}

jint avcodec_context_getCodecTag(JNIEnv* env, jobject thiz) {
    const AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return (jint) ctx->codec_tag;
}

jint avcodec_context_setCodecTag(JNIEnv* env, jobject thiz, jint jCodecTag) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    ctx->codec_tag = jCodecTag;
    return 0;
}

jobject avcodec_context_getTimebase(JNIEnv* env, jobject thiz) {
    jobject object = avutil_rational_alloc(env);
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return object;
    }

    avutil_rational_setAVRational(env, object, &ctx->time_base);
    return object;
}

jint avcodec_context_setTimebase(JNIEnv* env, jobject thiz, jobject jRational) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    if (nullptr == jRational) {
        return AVERROR(EINVAL);
    }

    AVRational rational = {0, 1};
    avutil_rational_getAVRational(env, jRational, &rational);
    ctx->time_base = rational;
    return 0;
}

jobject avcodec_context_getPktTimebase(JNIEnv* env, jobject thiz) {
    jobject object = avutil_rational_alloc(env);
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return object;
    }

    avutil_rational_setAVRational(env, object, &ctx->pkt_timebase);
    return object;
}

jint avcodec_context_setPktTimebase(JNIEnv* env, jobject thiz, jobject jRational) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    if (nullptr == jRational) {
        return AVERROR(EINVAL);
    }

    AVRational rational = {0, 1};
    avutil_rational_getAVRational(env, jRational, &rational);
    ctx->pkt_timebase = rational;
    return 0;
}

jobject avcodec_context_getFrameRate(JNIEnv* env, jobject thiz) {
    jobject object = avutil_rational_alloc(env);
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr != ctx) {
        avutil_rational_setAVRational(env, object, &ctx->framerate);
    }
    return object;
}

jint avcodec_context_setFrameRate(JNIEnv* env, jobject thiz, jobject jRational) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    if (nullptr == jRational) {
        return AVERROR(EINVAL);
    }

    AVRational rational = {0, 1};
    avutil_rational_getAVRational(env, jRational, &rational);
    ctx->framerate = rational;
    return 0;
}

jlong avcodec_context_getBitRate(JNIEnv* env, jobject thiz) {
    const AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return (jlong) ctx->bit_rate;
}

jint avcodec_context_setBitRate(JNIEnv* env, jobject thiz, jlong jBitRate) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    ctx->bit_rate = (int64_t) jBitRate;
    return 0;
}

jint avcodec_context_getWidth(JNIEnv* env, jobject thiz) {
    const AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return (jint) ctx->width;
}

jint avcodec_context_setWidth(JNIEnv* env, jobject thiz, jint jWidth) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    ctx->width = jWidth;
    return 0;
}

jint avcodec_context_getHeight(JNIEnv* env, jobject thiz) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return (jint) ctx->height;
}

jint avcodec_context_setHeight(JNIEnv* env, jobject thiz, jint jHeight) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    ctx->height = jHeight;
    return 0;
}

jobject avcodec_context_getAspectRatio(JNIEnv* env, jobject thiz) {
    jobject object = avutil_rational_alloc(env);
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr != ctx) {
        avutil_rational_setAVRational(env, object, &ctx->sample_aspect_ratio);
    }
    return object;
}

jint avcodec_context_setAspectRatio(JNIEnv* env, jobject thiz, jobject jRational) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }

    AVRational rational = {0, 1};
    avutil_rational_getAVRational(env, jRational, &rational);
    ctx->sample_aspect_ratio = rational;
    return 0;
}

jstring avcodec_context_getPixelFormat(JNIEnv* env, jobject thiz) {
    const AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return nullptr;
    }
    const char* fmt = av_get_pix_fmt_name(ctx->pix_fmt);
    return TextUtils::ToUTF8(env, fmt);
}

jint avcodec_context_setPixelFormat(JNIEnv* env, jobject thiz, jstring jFormat) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    const char* name = env->GetStringUTFChars(jFormat, JNI_FALSE);
    if (nullptr == name) {
        return AVERROR(EINVAL);
    }
    ctx->pix_fmt = av_get_pix_fmt(name);
    env->ReleaseStringUTFChars(jFormat, name);
    return 0;
}

jint avcodec_context_getSampleRate(JNIEnv* env, jobject thiz) {
    const AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return (jint) ctx->sample_rate;
}

jint avcodec_context_setSampleRate(JNIEnv* env, jobject thiz, jint jSampleRate) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    ctx->sample_rate = jSampleRate;
    return 0;
}

jstring avcodec_context_getSampleFormat(JNIEnv* env, jobject thiz) {
    const AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return nullptr;
    }
    const char* fmt = av_get_sample_fmt_name(ctx->sample_fmt);
    return TextUtils::ToUTF8(env, fmt);
}

jint avcodec_context_setSampleFormat(JNIEnv* env, jobject thiz, jstring jFormat) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    const char* name = env->GetStringUTFChars(jFormat, JNI_FALSE);
    if (nullptr == name) {
        return AVERROR(EINVAL);
    }
    ctx->sample_fmt = av_get_sample_fmt(name);
    env->ReleaseStringUTFChars(jFormat, name);
    return 0;
}

jobject avcodec_context_getChannelLayout(JNIEnv* env, jobject thiz) {
    jobject object = avutil_channel_layout_alloc(env);
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr != ctx) {
        avutil_channel_layout_setAVChannelLayout(env, object, &ctx->ch_layout);
    }
    return object;
}

jint avcodec_context_setChannelLayout(JNIEnv* env, jobject thiz, jobject jLayout) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }

    AVChannelLayout* ch_layout = (nullptr == jLayout) ? nullptr : avutil_channel_layout_getAVChannelLayout(env, jLayout);
    if (nullptr == ch_layout) {
        return AVERROR(EINVAL);
    }
    return av_channel_layout_copy(&ctx->ch_layout, ch_layout);
}

jint avcodec_context_getFrameSize(JNIEnv* env, jobject thiz) {
    const AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return (jint) ctx->frame_size;
}

jint avcodec_context_getFlags(JNIEnv* env, jobject thiz) {
    const AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return (jint) ctx->flags;
}

jint avcodec_context_setFlags(JNIEnv* env, jobject thiz, jint jFlags) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    ctx->flags = jFlags;
    return 0;
}

jobject avcodec_context_open(JNIEnv* env, jobject thiz, jobject jDictionary) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    AVCodec* codec = (nullptr == ctx) ? nullptr : const_cast<AVCodec *>(ctx->codec);
    if (nullptr == codec) {
        return nullptr;
    }

    const int encoder = av_codec_is_encoder(codec);
    AVDictionary* dictionary = nullptr;
    avutil_dictionary_getAVDictionary(env, jDictionary, &dictionary);
    const int ret = avcodec_open2(ctx, codec, &dictionary);
    av_dict_free(&dictionary);
    if (ret < 0) {
        LINFO("Open %s[%s] failed. code: %d", encoder ? "encoder" : "decoder", avcodec_get_name(codec->id), ret);
        return nullptr;
    }
    return encoder ? avcodec_encoder_alloc(env, thiz, ctx) : avcodec_decoder_alloc(env, thiz, ctx);
}

void avcodec_context_close(JNIEnv* env, jobject thiz) {
    AVCodecContext* ctx = avcodec_context_getAVCodecContext(env, thiz);
    if (nullptr != ctx) {
        avcodec_free_context(&ctx);
    }
    avcodec_context_setAVCodecContext(env, thiz, nullptr);
}

static JNINativeMethod methods[] = {
    {(char*) "_setCodecParameters", (char*) "(Lorg/hibate/media/codec/AVCodecParameters;)I", (void*) avcodec_context_setCodecParameters},
    {(char*) "_getMediaType", (char*) "()I", (void*) avcodec_context_getMediaType},
    {(char*) "_getCodecId", (char*) "()Ljava/lang/String;", (void*) avcodec_context_getCodecId},
    {(char*) "_getCodecTag", (char*) "()I", (void*) avcodec_context_getCodecTag},
    {(char*) "_setCodecTag", (char*) "(I)I", (void*) avcodec_context_setCodecTag},
    {(char*) "getTimebase", (char*) "()Lorg/hibate/media/util/AVRational;", (void*) avcodec_context_getTimebase},
    {(char*) "_setTimebase", (char*) "(Lorg/hibate/media/util/AVRational;)I", (void*) avcodec_context_setTimebase},
    {(char*) "getPktTimebase", (char*) "()Lorg/hibate/media/util/AVRational;", (void*) avcodec_context_getPktTimebase},
    {(char*) "_setPktTimebase", (char*) "(Lorg/hibate/media/util/AVRational;)I", (void*) avcodec_context_setPktTimebase},
    {(char*) "getFrameRate", (char*) "()Lorg/hibate/media/util/AVRational;", (void*) avcodec_context_getFrameRate},
    {(char*) "_setFrameRate", (char*) "(Lorg/hibate/media/util/AVRational;)I", (void*) avcodec_context_setFrameRate},
    {(char*) "getBitRate", (char*) "()J", (void*) avcodec_context_getBitRate},
    {(char*) "_setBitRate", (char*) "(J)I", (void*) avcodec_context_setBitRate},
    {(char*) "getWidth", (char*) "()I", (void*) avcodec_context_getWidth},
    {(char*) "_setWidth", (char*) "(I)I", (void*) avcodec_context_setWidth},
    {(char*) "getHeight", (char*) "()I", (void*) avcodec_context_getHeight},
    {(char*) "_setHeight", (char*) "(I)I", (void*) avcodec_context_setHeight},
    {(char*) "getAspectRatio", (char*) "()Lorg/hibate/media/util/AVRational;", (void*) avcodec_context_getAspectRatio},
    {(char*) "_setAspectRatio", (char*) "(Lorg/hibate/media/util/AVRational;)I", (void*) avcodec_context_setAspectRatio},
    {(char*) "_getPixelFormat", (char*) "()Ljava/lang/String;", (void*) avcodec_context_getPixelFormat},
    {(char*) "_setPixelFormat", (char*) "(Ljava/lang/String;)I", (void*) avcodec_context_setPixelFormat},
    {(char*) "getSampleRate", (char*) "()I", (void*) avcodec_context_getSampleRate},
    {(char*) "_setSampleRate", (char*) "(I)I", (void*) avcodec_context_setSampleRate},
    {(char*) "_getSampleFormat", (char*) "()Ljava/lang/String;", (void*) avcodec_context_getSampleFormat},
    {(char*) "_setSampleFormat", (char*) "(Ljava/lang/String;)I", (void*) avcodec_context_setSampleFormat},
    {(char*) "getChannelLayout", (char*) "()Lorg/hibate/media/util/AVChannelLayout;", (void*) avcodec_context_getChannelLayout},
    {(char*) "_setChannelLayout", (char*) "(Lorg/hibate/media/util/AVChannelLayout;)I", (void*) avcodec_context_setChannelLayout},
    {(char*) "getFrameSize", (char*) "()I", (void*) avcodec_context_getFrameSize},
    {(char*) "getFlags", (char*) "()I", (void*) avcodec_context_getFlags},
    {(char*) "_setFlags", (char*) "(I)I", (void*) avcodec_context_setFlags},
    {(char*) "_open", (char*) "(Lorg/hibate/media/util/AVDictionary;)Lorg/hibate/media/codec/AVCoder;", (void*) avcodec_context_open},
    {(char*) "close", (char*) "()V", (void*) avcodec_context_close},
};

int register_avcodec_context(JNIEnv* env) {
    avcodec_context_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avcodec_context(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
    fields.codec = nullptr;
}

MethodLoader avCodecContextLoader = {register_avcodec_context, unregister_avcodec_context};
