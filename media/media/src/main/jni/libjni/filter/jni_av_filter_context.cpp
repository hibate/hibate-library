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
 * jni_av_filter_context.cpp
 *
 * @author Hibate
 * Created on 2026/03/21.
 */

extern "C" {
#include <libavfilter/buffersink.h>
#include <libavfilter/buffersrc.h>
}

#include <jni_util.h>
#include <exceptions.h>

#include <text/text_utils.h>

#include "jni_av_filter.h"
#include "jni_av_filter_context.h"
#include "jni_av_filter_graph.h"
#include "util/jni_av_dictionary.h"
#include "util/jni_av_frame.h"
#include "util/jni_av_rational.h"

#define className "org/hibate/media/filter/impl/AVFilterContextImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
};

static struct fields_t fields;

void avfilter_context_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFilterContextImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFilterContextImpl.ctx field");
            return;
        }
    }
}

AVFilterContext* avfilter_context_getAVFilterContext(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return reinterpret_cast<AVFilterContext *>(env->GetLongField(thiz, fields.context));
}

void avfilter_context_setAVFilterContext(JNIEnv* env, jobject thiz, const AVFilterContext* ctx) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, reinterpret_cast<jlong>(ctx));
}

jobject avfilter_context_alloc(JNIEnv* env, const AVFilterContext* ctx) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    jobject object = env->NewObject(env->FindClass(className), fields.constructor);
    avfilter_context_setAVFilterContext(env, object, ctx);
    return object;
}

void avfilter_context_init(JNIEnv* env, jobject thiz, jobject jGraph, jobject jFilter, jstring jName) {
    char* name = nullptr;
    const AVFilterContext* ctx = nullptr;
    AVFilterGraph* graph = avfilter_graph_getAVFilterGraph(env, jGraph);
    const AVFilter* filter = avfilter_filter_getAVFilter(env, jFilter);
    if (nullptr != jName) {
        const char* text = env->GetStringUTFChars(jName, JNI_FALSE);
        name = av_strdup(text);
        env->ReleaseStringUTFChars(jName, text);
    }
    ctx = avfilter_graph_alloc_filter(graph, filter, name);
    if (nullptr == ctx) {
        av_free(name);
        throwException(env, RUNTIME_EXCEPTION, "AVFilterContext allocate failed");
        return;
    }
    avfilter_context_setAVFilterContext(env, thiz, ctx);
}

jobject avfilter_context_getName(JNIEnv* env, jclass thiz) {
    const AVFilterContext* ctx = avfilter_context_getAVFilterContext(env, thiz);
    if (nullptr == ctx) {
        return nullptr;
    }
    return TextUtils::ToUTF8(env, ctx->name);
}

jobject avfilter_context_getTimebase(JNIEnv* env, jobject thiz) {
    AVFilterContext* ctx = avfilter_context_getAVFilterContext(env, thiz);
    AVRational rational = {0, 1};
    if (nullptr != ctx) {
        rational = av_buffersink_get_time_base(ctx);
    }
    jobject object = avutil_rational_alloc(env);
    avutil_rational_setAVRational(env, object, &rational);
    return object;
}

jint avfilter_context_setDictionary(JNIEnv* env, jobject thiz, jobject jDictionary) {
    AVFilterContext* ctx = avfilter_context_getAVFilterContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }

    AVDictionary* dictionary = nullptr;
    if (nullptr != jDictionary) {
        avutil_dictionary_getAVDictionary(env, jDictionary, &dictionary);
    }
    const int ret = avfilter_init_dict(ctx, &dictionary);
    av_dict_free(&dictionary);
    return (ret < 0) ? ret : 0;
}

jint avfilter_context_setFrameSize(JNIEnv* env, jobject thiz, jint jFrameSize) {
    AVFilterContext* ctx = avfilter_context_getAVFilterContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    av_buffersink_set_frame_size(ctx, (unsigned) jFrameSize);
    return 0;
}

jint avfilter_context_send(JNIEnv* env, jobject thiz, jobject jFrame, jint jFlags) {
    AVFilterContext* ctx = avfilter_context_getAVFilterContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    AVFrame* frame = avutil_frame_getAVFrame(env, jFrame);
    return av_buffersrc_add_frame_flags(ctx, frame, jFlags);
}

jint avfilter_context_receive(JNIEnv* env, jobject thiz, jobject jFrame) {
    AVFilterContext* ctx = avfilter_context_getAVFilterContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    AVFrame* frame = avutil_frame_getAVFrame(env, jFrame);
    return av_buffersink_get_frame(ctx, frame);
}

void avfilter_context_close(JNIEnv* env, jobject thiz) {
    AVFilterContext* ctx = avfilter_context_getAVFilterContext(env, thiz);
    if (nullptr != ctx) {
        avfilter_free(ctx);
    }
    avfilter_context_setAVFilterContext(env, thiz, nullptr);
}

static JNINativeMethod methods[] = {
    {(char*) "_initialize", (char*) "(Lorg/hibate/media/filter/AVFilterGraph;Lorg/hibate/media/filter/AVFilter;Ljava/lang/String;)V", (void*) avfilter_context_init},
    {(char*) "getName", (char*) "()Ljava/lang/String;", (void*) avfilter_context_getName},
    {(char*) "getTimebase", (char*) "()Lorg/hibate/media/util/AVRational;", (void*) avfilter_context_getTimebase},
    {(char*) "_setDictionary", (char*) "(Lorg/hibate/media/util/AVDictionary;)I", (void*) avfilter_context_setDictionary},
    {(char*) "_setFrameSize", (char*) "(I)I", (void*) avfilter_context_setFrameSize},
    {(char*) "send", (char*) "(Lorg/hibate/media/util/AVFrame;I)I", (void*) avfilter_context_send},
    {(char*) "receive", (char*) "(Lorg/hibate/media/util/AVFrame;)I", (void*) avfilter_context_receive},
    {(char*) "close", (char*) "()V", (void*) avfilter_context_close},
};

int register_avfilter_context(JNIEnv* env) {
    avfilter_context_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avfilter_context(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
}

MethodLoader avFilterContextLoader = {register_avfilter_context, unregister_avfilter_context};
