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
 * jni_av_frame.cpp
 *
 * @author Hibate
 * Created on 2024/03/27.
 */

#include <cstdio>

#include <jni_util.h>
#include <exceptions.h>

#include "jni_av_channel_layout.h"
#include "jni_av_frame.h"
#include "util/jni_av_rational.h"

#define className "org/hibate/media/util/impl/AVFrameImpl"

struct fields_t {
    jfieldID context;
};

static struct fields_t fields;

void avutil_frame_initialize(JNIEnv* env) {
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFrameImpl.ctx field");
            return;
        }
    }
}

AVFrame* avutil_frame_getAVFrame(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVFrame*) env->GetLongField(thiz, fields.context);
}

void avutil_frame_setAVFrame(JNIEnv* env, jobject thiz, AVFrame* frame) {
    if (nullptr == fields.context) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) frame);
}

void avutil_frame_init(JNIEnv* env, jobject thiz) {
    AVFrame* frame = av_frame_alloc();
    if (nullptr == frame) {
        throwException(env, RUNTIME_EXCEPTION, "AVFrame allocate failed");
        return;
    }
    avutil_frame_setAVFrame(env, thiz, frame);
}

jint avutil_frame_getWidth(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return 0;
    }
    return (jint) frame->width;
}

jint avutil_frame_getHeight(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return 0;
    }
    return (jint) frame->height;
}

jint avutil_frame_getFlags(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return AV_FRAME_FLAG_CORRUPT;
    }
    return (jint) frame->flags;
}

jint avutil_frame_getFormat(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return AV_FRAME_FLAG_CORRUPT;
    }
    return (jint) frame->format;
}

jint avutil_frame_getPictureType(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return AV_PICTURE_TYPE_NONE;
    }
    return (jint) frame->pict_type;
}

jint avutil_frame_setPictureType(JNIEnv* env, jobject thiz, jint jType) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return AVERROR(ENOENT);
    }
    frame->pict_type = static_cast<AVPictureType>(jType);
    return 0;
}

jlong avutil_frame_getPts(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return (jlong) AV_NOPTS_VALUE;
    }
    return (jlong) frame->pts;
}

jint avutil_frame_setPts(JNIEnv* env, jobject thiz, jlong jPts) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return AVERROR(ENOENT);
    }
    frame->pts = (int64_t) jPts;
    return 0;
}

jlong avutil_frame_getDts(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return (jlong) AV_NOPTS_VALUE;
    }
    return (jlong) frame->pkt_dts;
}

jobject avutil_frame_getTimebase(JNIEnv* env, jobject thiz) {
    jobject object = avutil_rational_alloc(env);
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return object;
    }

    avutil_rational_setAVRational(env, object, &frame->time_base);
    return object;
}

jint avutil_frame_setTimebase(JNIEnv* env, jobject thiz, jobject jRational) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return AVERROR(ENOENT);
    }
    if (nullptr == jRational) {
        return AVERROR(EINVAL);
    }

    AVRational rational = {0, 1};
    avutil_rational_getAVRational(env, jRational, &rational);
    frame->time_base = rational;
    return 0;
}

jint avutil_frame_getQuality(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return 0;
    }
    return (jint) frame->quality;
}

jint avutil_frame_getSampleRate(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return 0;
    }
    return (jint) frame->sample_rate;
}

jlong avutil_frame_getBestEffortTimestamp(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return 0;
    }
    return (jlong) frame->best_effort_timestamp;
}

jobject avutil_frame_getChannelLayout(JNIEnv* env, jobject thiz) {
    jobject object = avutil_channel_layout_alloc(env);
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr != frame) {
        avutil_channel_layout_setAVChannelLayout(env, object, &frame->ch_layout);
    }
    return object;
}

jlong avutil_frame_getDuration(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return 0;
    }
    return (jint) frame->duration;
}

jint avutil_frame_unref(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr == frame) {
        return AVERROR(ENOENT);
    }
    av_frame_unref(frame);
    return 0;
}

void avutil_frame_close(JNIEnv* env, jobject thiz) {
    AVFrame* frame = avutil_frame_getAVFrame(env, thiz);
    if (nullptr != frame) {
        av_frame_free(&frame);
    }
    avutil_frame_setAVFrame(env, thiz, nullptr);
}

static JNINativeMethod methods[] = {
    {(char*) "_initialize", (char*) "()V", (void*) avutil_frame_init},
    {(char*) "getWidth", (char*) "()I", (void*) avutil_frame_getWidth},
    {(char*) "getHeight", (char*) "()I", (void*) avutil_frame_getHeight},
    {(char*) "getFlags", (char*) "()I", (void*) avutil_frame_getFlags},
    {(char*) "getFormat", (char*) "()I", (void*) avutil_frame_getFormat},
    {(char*) "_getPictureType", (char*) "()I", (void*) avutil_frame_getPictureType},
    {(char*) "_setPictureType", (char*) "(I)I", (void*) avutil_frame_setPictureType},
    {(char*) "getPts", (char*) "()J", (void*) avutil_frame_getPts},
    {(char*) "_setPts", (char*) "(J)I", (void*) avutil_frame_setPts},
    {(char*) "getDts", (char*) "()J", (void*) avutil_frame_getDts},
    {(char*) "getTimebase", (char*) "()Lorg/hibate/media/util/AVRational;", (void*) avutil_frame_getTimebase},
    {(char*) "_setTimebase", (char*) "(Lorg/hibate/media/util/AVRational;)I", (void*) avutil_frame_setTimebase},
    {(char*) "getQuality", (char*) "()I", (void*) avutil_frame_getQuality},
    {(char*) "getSampleRate", (char*) "()I", (void*) avutil_frame_getSampleRate},
    {(char*) "getBestEffortTimestamp", (char*) "()J", (void*) avutil_frame_getBestEffortTimestamp},
    {(char*) "getChannelLayout", (char*) "()Lorg/hibate/media/util/AVChannelLayout;", (void*) avutil_frame_getChannelLayout},
    {(char*) "getDuration", (char*) "()J", (void*) avutil_frame_getDuration},
    {(char*) "_unref", (char*) "()I", (void*) avutil_frame_unref},
    {(char*) "close", (char*) "()V", (void*) avutil_frame_close}
};

int register_avutil_frame(JNIEnv* env) {
    avutil_frame_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avutil_frame(JNIEnv* env) {
    fields.context = nullptr;
}

MethodLoader avUtilFrameLoader = {register_avutil_frame, unregister_avutil_frame};
