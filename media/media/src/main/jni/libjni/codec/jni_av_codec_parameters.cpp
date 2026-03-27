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
 * jni_av_codec_parameters.cpp
 *
 * @author Hibate
 * Created on 2024/03/25.
 */

extern "C" {
#include <libavutil/pixdesc.h>
}

#include <jni_util.h>
#include <exceptions.h>

#include <text/text_utils.h>

#include "jni_av_codec_context.h"
#include "jni_av_codec_id.h"
#include "jni_av_codec_parameters.h"
#include "util/jni_av_channel_layout.h"
#include "util/jni_av_rational.h"

#define className "org/hibate/media/codec/impl/AVCodecParametersImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
};

static struct fields_t fields;

void avcodec_parameters_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVCodecParametersImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVCodecParametersImpl.ctx field");
            return;
        }
    }
}

AVCodecParameters* avcodec_parameters_getAVCodecParameters(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVCodecParameters*) env->GetLongField(thiz, fields.context);
}

void avcodec_parameters_setAVCodecParameters(JNIEnv* env, jobject thiz, AVCodecParameters* codecpar) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) codecpar);
}

jobject avcodec_parameters_alloc(JNIEnv* env, AVCodecParameters *codecpar) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    jobject object = env->NewObject(env->FindClass(className), fields.constructor);
    avcodec_parameters_setAVCodecParameters(env, object, codecpar);
    return object;
}

jint avcodec_parameters_setCodecContext(JNIEnv* env, jobject thiz, jobject jContext) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }

    const AVCodecContext* ctx = (nullptr == jContext) ? nullptr : avcodec_context_getAVCodecContext(env, jContext);
    if (nullptr == ctx) {
        return AVERROR(EINVAL);
    }

    return avcodec_parameters_from_context(codecpar, ctx);
}

jint avcodec_parameters_getMediaType(JNIEnv* env, jobject thiz) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVMEDIA_TYPE_UNKNOWN;
    }
    return (jint) codecpar->codec_type;
}

jint avcodec_parameters_setMediaType(JNIEnv* env, jobject thiz, jint jMediaType) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }
    codecpar->codec_type = (enum AVMediaType) jMediaType;
    return 0;
}

jstring avcodec_parameters_getCodecId(JNIEnv* env, jobject thiz) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return nullptr;
    }
    const char* name = avcodec_id_getName(codecpar->codec_id);
    return TextUtils::ToUTF8(env, name);
}

jint avcodec_parameters_setCodecId(JNIEnv* env, jobject thiz, jstring jName) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    if (nullptr == name) {
        return AVERROR(EINVAL);
    }
    const AVCodecDescriptor* descriptor = avcodec_id_getAVCodecDescriptor(name);
    env->ReleaseStringUTFChars(jName, name);
    if (nullptr == descriptor) {
        return AVERROR(EINVAL);
    }
    codecpar->codec_id = descriptor->id;
    return 0;
}

jint avcodec_parameters_getCodecTag(JNIEnv* env, jobject thiz) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return 0;
    }
    return (jint) codecpar->codec_tag;
}

jint avcodec_parameters_setCodecTag(JNIEnv* env, jobject thiz, jint jCodecTag) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }
    codecpar->codec_tag = jCodecTag;
    return 0;
}

jint avcodec_parameters_getExtraDataSize(JNIEnv* env, jobject thiz) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return 0;
    }
    return (jint) codecpar->extradata_size;
}

jstring avcodec_parameters_getFormat(JNIEnv* env, jobject thiz) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return nullptr;
    }
    if (AVMEDIA_TYPE_VIDEO == codecpar->codec_type) {
        const char* fmt = av_get_pix_fmt_name((enum AVPixelFormat) codecpar->format);
        return TextUtils::ToUTF8(env, fmt);
    }
    if (AVMEDIA_TYPE_AUDIO == codecpar->codec_type) {
        const char* fmt = av_get_sample_fmt_name((enum AVSampleFormat) codecpar->format);
        return TextUtils::ToUTF8(env, fmt);
    }
    return nullptr;
}

jint avcodec_parameters_setFormat(JNIEnv* env, jobject thiz, jstring jFormat) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }
    const char* name = env->GetStringUTFChars(jFormat, JNI_FALSE);
    if (nullptr == name) {
        return AVERROR(EINVAL);
    }
    if (AVMEDIA_TYPE_VIDEO == codecpar->codec_type) {
        codecpar->format = static_cast<int>(av_get_pix_fmt(name));
    } else if (AVMEDIA_TYPE_AUDIO == codecpar->codec_type) {
        codecpar->format = static_cast<int>(av_get_sample_fmt(name));
    }
    env->ReleaseStringUTFChars(jFormat, name);
    return 0;
}

jlong avcodec_parameters_getBitRate(JNIEnv* env, jobject thiz) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return 0;
    }
    return (jlong) codecpar->bit_rate;
}

jint avcodec_parameters_setBitRate(JNIEnv* env, jobject thiz, jlong jBitRate) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }
    codecpar->bit_rate = (int64_t) jBitRate;
    return 0;
}

jint avcodec_parameters_getWidth(JNIEnv* env, jobject thiz) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return 0;
    }
    return (jint) codecpar->width;
}

jint avcodec_parameters_setWidth(JNIEnv* env, jobject thiz, jint jWidth) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }
    codecpar->width = jWidth;
    return 0;
}

jint avcodec_parameters_getHeight(JNIEnv* env, jobject thiz) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return 0;
    }
    return (jint) codecpar->height;
}

jint avcodec_parameters_setHeight(JNIEnv* env, jobject thiz, jint jHeight) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }
    codecpar->height = jHeight;
    return 0;
}

jobject avcodec_parameters_getAspectRatio(JNIEnv* env, jobject thiz) {
    jobject object = avutil_rational_alloc(env);
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr != codecpar) {
        avutil_rational_setAVRational(env, object, &codecpar->sample_aspect_ratio);
    }
    return object;
}

jint avcodec_parameters_setAspectRatio(JNIEnv* env, jobject thiz, jobject jRational) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }

    AVRational rational = {0, 1};
    avutil_rational_getAVRational(env, jRational, &rational);
    codecpar->sample_aspect_ratio = rational;
    return 0;
}

jint avcodec_parameters_getSampleRate(JNIEnv* env, jobject thiz) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return 0;
    }
    return (jint) codecpar->sample_rate;
}

jint avcodec_parameters_setSampleRate(JNIEnv* env, jobject thiz, jint jSampleRate) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }
    codecpar->sample_rate = jSampleRate;
    return 0;
}

jobject avcodec_parameters_getChannelLayout(JNIEnv* env, jobject thiz) {
    jobject object = avutil_channel_layout_alloc(env);
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr != codecpar) {
        avutil_channel_layout_setAVChannelLayout(env, object, &codecpar->ch_layout);
    }
    return object;
}

jint avcodec_parameters_setChannelLayout(JNIEnv* env, jobject thiz, jobject jLayout) {
    AVCodecParameters* codecpar = avcodec_parameters_getAVCodecParameters(env, thiz);
    if (nullptr == codecpar) {
        return AVERROR(ENOENT);
    }

    AVChannelLayout* ch_layout = (nullptr == jLayout) ? nullptr : avutil_channel_layout_getAVChannelLayout(env, jLayout);
    if (nullptr == ch_layout) {
        return AVERROR(EINVAL);
    }
    return av_channel_layout_copy(&codecpar->ch_layout, ch_layout);
}

static JNINativeMethod methods[] = {
    {(char*) "_setCodecContext", (char*) "(Lorg/hibate/media/codec/AVCodecContext;)I", (void*) avcodec_parameters_setCodecContext},
    {(char*) "_getMediaType", (char*) "()I", (void*) avcodec_parameters_getMediaType},
    {(char*) "_setMediaType", (char*) "(I)I", (void*) avcodec_parameters_setMediaType},
    {(char*) "_getCodecId", (char*) "()Ljava/lang/String;", (void*) avcodec_parameters_getCodecId},
    {(char*) "_setCodecId", (char*) "(Ljava/lang/String;)I", (void*) avcodec_parameters_setCodecId},
    {(char*) "_getCodecTag", (char*) "()I", (void*) avcodec_parameters_getCodecTag},
    {(char*) "_setCodecTag", (char*) "(I)I", (void*) avcodec_parameters_setCodecTag},
    {(char*) "_getExtraDataSize", (char*) "()I", (void*) avcodec_parameters_getExtraDataSize},
    {(char*) "_getFormat", (char*) "()Ljava/lang/String;", (void*) avcodec_parameters_getFormat},
    {(char*) "_setFormat", (char*) "(Ljava/lang/String;)I", (void*) avcodec_parameters_setFormat},
    {(char*) "getBitRate", (char*) "()J", (void*) avcodec_parameters_getBitRate},
    {(char*) "_setBitRate", (char*) "(J)I", (void*) avcodec_parameters_setBitRate},
    {(char*) "getWidth", (char*) "()I", (void*) avcodec_parameters_getWidth},
    {(char*) "_setWidth", (char*) "(I)I", (void*) avcodec_parameters_setWidth},
    {(char*) "getHeight", (char*) "()I", (void*) avcodec_parameters_getHeight},
    {(char*) "_setHeight", (char*) "(I)I", (void*) avcodec_parameters_setHeight},
    {(char*) "getAspectRatio", (char*) "()Lorg/hibate/media/util/AVRational;", (void*) avcodec_parameters_getAspectRatio},
    {(char*) "_setAspectRatio", (char*) "(Lorg/hibate/media/util/AVRational;)I", (void*) avcodec_parameters_setAspectRatio},
    {(char*) "getSampleRate", (char*) "()I", (void*) avcodec_parameters_getSampleRate},
    {(char*) "_setSampleRate", (char*) "(I)I", (void*) avcodec_parameters_setSampleRate},
    {(char*) "getChannelLayout", (char*) "()Lorg/hibate/media/util/AVChannelLayout;", (void*) avcodec_parameters_getChannelLayout},
    {(char*) "_setChannelLayout", (char*) "(Lorg/hibate/media/util/AVChannelLayout;)I", (void*) avcodec_parameters_setChannelLayout}
};

int register_avcodec_parameters(JNIEnv* env) {
    avcodec_parameters_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avcodec_parameters(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
}

MethodLoader avCodecParametersLoader = {register_avcodec_parameters, unregister_avcodec_parameters};
