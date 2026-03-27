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
 * jni_av_filter_graph.cpp
 *
 * @author Hibate
 * Created on 2026/03/21.
 */

#include <jni_util.h>
#include <exceptions.h>

#include "jni_av_filter_graph.h"
#include "jni_av_filter_in_out.h"

#define className "org/hibate/media/filter/impl/AVFilterGraphImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
};

static struct fields_t fields{};

void avfilter_graph_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFilterGraphImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFilterGraphImpl.ctx field");
            return;
        }
    }
}

AVFilterGraph* avfilter_graph_getAVFilterGraph(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVFilterGraph*) env->GetLongField(thiz, fields.context);
}

void avfilter_graph_setAVFilterGraph(JNIEnv* env, jobject thiz, const AVFilterGraph* graph) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) graph);
}

void avfilter_graph_init(JNIEnv* env, jobject thiz) {
    AVFilterGraph* graph = avfilter_graph_alloc();
    if (nullptr == graph) {
        throwException(env, RUNTIME_EXCEPTION, "AVFilterGraph allocate failed");
        return;
    }
    avfilter_graph_setAVFilterGraph(env, thiz, graph);
}

jint avfilter_graph_setDescription(JNIEnv* env, jobject thiz, jstring jSpecs, jobject jInput, jobject jOutput) {
    AVFilterGraph* graph = avfilter_graph_getAVFilterGraph(env, thiz);
    if (nullptr == fields.context) {
        return AVERROR(ENOENT);
    }
    if (nullptr == jSpecs) {
        return AVERROR(EINVAL);
    }
    const char* specs = env->GetStringUTFChars(jSpecs, JNI_FALSE);
    AVFilterInOut* input = avfilter_inout_getAVFilterInOut(env, jInput);
    AVFilterInOut* output = avfilter_inout_getAVFilterInOut(env, jOutput);
    int ret = avfilter_graph_parse_ptr(graph, specs, &input, &output, nullptr);
    // 更新引用: avfilter_graph_parse_ptr 函数内部修改了指针
    avfilter_inout_setAVFilterInOut(env, jInput, input);
    avfilter_inout_setAVFilterInOut(env, jOutput, output);
    env->ReleaseStringUTFChars(jSpecs, specs);
    return ret;
}

jint avfilter_graph_prepare(JNIEnv* env, jobject thiz) {
    AVFilterGraph* graph = avfilter_graph_getAVFilterGraph(env, thiz);
    if (nullptr == graph) {
        return AVERROR(ENOENT);
    }
    return avfilter_graph_config(graph, nullptr);
}

void avfilter_graph_close(JNIEnv* env, jobject thiz) {
    AVFilterGraph* graph = avfilter_graph_getAVFilterGraph(env, thiz);
    if (nullptr != graph) {
        avfilter_graph_free(&graph);
    }
    avfilter_graph_setAVFilterGraph(env, thiz, nullptr);
}

static JNINativeMethod methods[] = {
    {(char*) "_initialize", (char*) "()V", (void*) avfilter_graph_init},
    {(char*) "_setDescription", (char*) "(Ljava/lang/String;Lorg/hibate/media/filter/AVFilterInOut;Lorg/hibate/media/filter/AVFilterInOut;)I", (void*) avfilter_graph_setDescription},
    {(char*) "_prepare", (char*) "()I", (void*) avfilter_graph_prepare},
    {(char*) "close", (char*) "()V", (void*) avfilter_graph_close},
};

int register_avfilter_graph(JNIEnv* env) {
    avfilter_graph_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avfilter_graph(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
}

MethodLoader avFilterGraphLoader = {register_avfilter_graph, unregister_avfilter_graph};
