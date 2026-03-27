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
 * jni_av_channel_layout.cpp
 *
 * @author Hibate
 * Created on 2024/03/30.
 */

extern "C" {
#include <libavutil/error.h>
#include <libavutil/bprint.h>
}

#include <jni_util.h>
#include <exceptions.h>

#include <text/text_utils.h>

#include "jni_av_channel_layout.h"

#define className "org/hibate/media/util/impl/AVChannelLayoutImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
};

static struct fields_t fields;

void avutil_channel_layout_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVChannelLayoutImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVChannelLayoutImpl.ctx field");
            return;
        }
    }
}

AVChannelLayout* avutil_channel_layout_getAVChannelLayout(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVChannelLayout*) env->GetLongField(thiz, fields.context);
}

void avutil_channel_layout_setAVChannelLayout(JNIEnv* env, jobject thiz, AVChannelLayout* layout) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) layout);
}

jobject avutil_channel_layout_alloc(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    return env->NewObject(env->FindClass(className), fields.constructor);
}

jstring avutil_channel_layout_getDescription(JNIEnv* env, jobject thiz) {
    AVChannelLayout* layout = avutil_channel_layout_getAVChannelLayout(env, thiz);
    if (nullptr == layout) {
        return nullptr;
    }

    AVBPrint bPrint{};
    av_bprint_init(&bPrint, 0, AV_BPRINT_SIZE_UNLIMITED);
    const int ret = av_channel_layout_describe_bprint(layout, &bPrint);
    jstring value = ret == 0 ? TextUtils::ToUTF8(env, bPrint.str) : nullptr;
    av_bprint_finalize(&bPrint, nullptr);
    return value;
}

jint avutil_channel_layout_getChannelOrder(JNIEnv* env, jobject thiz) {
    AVChannelLayout* layout = avutil_channel_layout_getAVChannelLayout(env, thiz);
    if (nullptr == layout) {
        return AV_CHANNEL_ORDER_UNSPEC;
    }
    return (jint) layout->order;
}

jint avutil_channel_layout_setChannelOrder(JNIEnv* env, jobject thiz, jint jOrder) {
    AVChannelLayout* layout = avutil_channel_layout_getAVChannelLayout(env, thiz);
    if (nullptr == layout) {
        return AVERROR(ENOENT);
    }
    layout->order = (enum AVChannelOrder) jOrder;
    return 0;
}

jint avutil_channel_layout_getChannels(JNIEnv* env, jobject thiz) {
    AVChannelLayout* layout = avutil_channel_layout_getAVChannelLayout(env, thiz);
    if (nullptr == layout) {
        return 0;
    }
    return (jint) layout->nb_channels;
}

jint avutil_channel_layout_setChannels(JNIEnv* env, jobject thiz, jint jChannels) {
    AVChannelLayout* layout = avutil_channel_layout_getAVChannelLayout(env, thiz);
    if (nullptr == layout) {
        return AVERROR(ENOENT);
    }
    layout->nb_channels = jChannels;
    return 0;
}

jint avutil_channel_layout_setDefaults(JNIEnv* env, jobject thiz, jint jChannels) {
    AVChannelLayout* layout = avutil_channel_layout_getAVChannelLayout(env, thiz);
    if (nullptr == layout) {
        return AVERROR(ENOENT);
    }
    av_channel_layout_default(layout, jChannels);
    return 0;
}

static JNINativeMethod methods[] = {
    {(char*) "getDescription", (char*) "()Ljava/lang/String;", (void*) avutil_channel_layout_getDescription},
    {(char*) "_getChannelOrder", (char*) "()I", (void*) avutil_channel_layout_getChannelOrder},
    {(char*) "_setChannelOrder", (char*) "(I)I", (void*) avutil_channel_layout_setChannelOrder},
    {(char*) "getChannels", (char*) "()I", (void*) avutil_channel_layout_getChannels},
    {(char*) "_setChannels", (char*) "(I)I", (void*) avutil_channel_layout_setChannels},
    {(char*) "_setDefaults", (char*) "(I)I", (void*) avutil_channel_layout_setDefaults}
};

int register_avutil_channel_layout(JNIEnv* env) {
    avutil_channel_layout_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avutil_channel_layout(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
}

MethodLoader avUtilChannelLayoutLoader = {register_avutil_channel_layout, unregister_avutil_channel_layout};
