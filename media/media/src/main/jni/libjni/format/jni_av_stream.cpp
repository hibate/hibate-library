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
 * jni_av_stream.cpp
 *
 * @author Hibate
 * Created on 2024/03/25.
 */

#include <jni_util.h>
#include <exceptions.h>

#include "jni_av_stream.h"
#include "jni_av_format_context.h"
#include "codec/jni_av_codec_parameters.h"
#include "util/jni_av_rational.h"

extern "C" {
#include <libswscale/swscale.h>
#include <libavutil/imgutils.h>
}

#define className "org/hibate/media/format/impl/AVStreamImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
};

static struct fields_t fields;

void avstream_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVStreamImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVStreamImpl.ctx field");
            return;
        }
    }
}

AVStream* avstream_getAVStream(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVStream*) env->GetLongField(thiz, fields.context);
}

void avstream_setAVStream(JNIEnv* env, jobject thiz, const AVStream* stream) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) stream);
}

jobject avstream_alloc(JNIEnv* env, const AVStream* stream) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    jobject object = env->NewObject(env->FindClass(className), fields.constructor);
    avstream_setAVStream(env, object, stream);
    return object;
}

jint avstream_getIndex(JNIEnv* env, jobject thiz) {
    AVStream* stream = avstream_getAVStream(env, thiz);
    if (nullptr == stream) {
        return 0;
    }
    return stream->index;
}

jobject avstream_getTimebase(JNIEnv* env, jobject thiz) {
    jobject object = avutil_rational_alloc(env);
    AVStream* stream = avstream_getAVStream(env, thiz);
    if (nullptr == stream) {
        return object;
    }

    avutil_rational_setAVRational(env, object, &stream->time_base);
    return object;
}

jint avstream_setTimebase(JNIEnv* env, jobject thiz, jobject jRational) {
    AVStream* stream = avstream_getAVStream(env, thiz);
    if (nullptr == stream) {
        return AVERROR(ENOENT);
    }
    if (nullptr == jRational) {
        return AVERROR(EINVAL);
    }

    AVRational rational = {0, 1};
    avutil_rational_getAVRational(env, jRational, &rational);
    stream->time_base = rational;
    return 0;
}

jlong avstream_getStartTime(JNIEnv* env, jobject thiz) {
    AVStream* stream = avstream_getAVStream(env, thiz);
    if (nullptr == stream) {
        return (jlong) AV_NOPTS_VALUE;
    }
    return (jlong) (static_cast<double>(stream->start_time) * av_q2d(stream->time_base));
}

jlong avstream_getDuration(JNIEnv* env, jobject thiz) {
    AVStream* stream = avstream_getAVStream(env, thiz);
    if (nullptr == stream) {
        return 0;
    }
    return (jlong) (static_cast<double>(stream->duration) * av_q2d(stream->time_base));
}

jlong avstream_getFrames(JNIEnv* env, jobject thiz) {
    AVStream* stream = avstream_getAVStream(env, thiz);
    if (nullptr == stream) {
        return 0;
    }
    return (jlong) stream->nb_frames;
}

jobject avstream_getFrameRate(JNIEnv* env, jobject thiz, jobject jContext) {
    jobject object = avutil_rational_alloc(env);
    AVStream* stream = avstream_getAVStream(env, thiz);
    if ((nullptr != stream) && (stream->codecpar->codec_type == AVMEDIA_TYPE_VIDEO)) {
        AVFormatContext* ctx = (nullptr == jContext) ? nullptr : avformat_context_getAVFormatContext(env, jContext);
        AVRational rational = av_guess_frame_rate(ctx, stream, nullptr);
        avutil_rational_setAVRational(env, object, &rational);
    }
    return object;
}

jobject avstream_getCodecParameters(JNIEnv* env, jobject thiz) {
    AVStream* stream = avstream_getAVStream(env, thiz);
    if (nullptr == stream) {
        return nullptr;
    }

    AVCodecParameters* codecpar = stream->codecpar;
    return avcodec_parameters_alloc(env, codecpar);
}

jint avstream_setCodecParameters(JNIEnv* env, jobject thiz, jobject jParameters) {
    AVStream* stream = avstream_getAVStream(env, thiz);
    if (nullptr == stream) {
        return AVERROR(ENOENT);
    }

    AVCodecParameters* codecpar = (nullptr == jParameters) ? nullptr : avcodec_parameters_getAVCodecParameters(env, jParameters);
    if (nullptr == codecpar) {
        return AVERROR(EINVAL);
    }
    return avcodec_parameters_copy(stream->codecpar, codecpar);
}

static JNINativeMethod methods[] = {
    {(char*) "getIndex", (char*) "()I", (void*) avstream_getIndex},
    {(char*) "getTimebase", (char*) "()Lorg/hibate/media/util/AVRational;", (void*) avstream_getTimebase},
    {(char*) "_setTimebase", (char*) "(Lorg/hibate/media/util/AVRational;)I", (void*) avstream_setTimebase},
    {(char*) "getStartTime", (char*) "()J", (void*) avstream_getStartTime},
    {(char*) "getDuration", (char*) "()J", (void*) avstream_getDuration},
    {(char*) "getFrames", (char*) "()J", (void*) avstream_getFrames},
    {(char*) "getFrameRate", (char*) "(Lorg/hibate/media/format/AVFormatContext;)Lorg/hibate/media/util/AVRational;", (void*) avstream_getFrameRate},
    {(char*) "getCodecParameters", (char*) "()Lorg/hibate/media/codec/AVCodecParameters;", (void*) avstream_getCodecParameters},
    {(char*) "_setCodecParameters", (char*) "(Lorg/hibate/media/codec/AVCodecParameters;)I", (void*) avstream_setCodecParameters}
};

int register_avstream(JNIEnv* env) {
    avstream_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avstream(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
}

MethodLoader avStreamLoader = {register_avstream, unregister_avstream};
