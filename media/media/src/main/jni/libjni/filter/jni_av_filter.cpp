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
 * jni_av_filter.cpp
 *
 * @author Hibate
 * Created on 2026/03/21.
 */

#include <jni_util.h>
#include <exceptions.h>

#include <text/text_utils.h>

#include "jni_av_filter.h"

#define className "org/hibate/media/filter/impl/AVFilterImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
};

static struct fields_t fields;

void avfilter_filter_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFilterImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFilterImpl.ctx field");
            return;
        }
    }
}

AVFilter* avfilter_filter_getAVFilter(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return reinterpret_cast<AVFilter *>(env->GetLongField(thiz, fields.context));
}

void avfilter_filter_setAVFilter(JNIEnv* env, jobject thiz, const AVFilter* filter) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, reinterpret_cast<jlong>(filter));
}

jobject avfilter_filter_alloc(JNIEnv* env, const AVFilter* filter) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    jobject object = env->NewObject(env->FindClass(className), fields.constructor);
    avfilter_filter_setAVFilter(env, object, filter);
    return object;
}

jstring avfilter_filter_getName(JNIEnv* env, jobject thiz) {
    const AVFilter* filter = avfilter_filter_getAVFilter(env, thiz);
    if (nullptr == filter) {
        return nullptr;
    }
    return TextUtils::ToUTF8(env, filter->name);
}

jstring avfilter_filter_getDescription(JNIEnv* env, jobject thiz) {
    const AVFilter* filter = avfilter_filter_getAVFilter(env, thiz);
    if (nullptr == filter) {
        return nullptr;
    }
    return TextUtils::ToUTF8(env, filter->description);
}

jint avfilter_filter_getFlags(JNIEnv* env, jobject thiz) {
    const AVFilter* filter = avfilter_filter_getAVFilter(env, thiz);
    if (nullptr == filter) {
        return 0;
    }
    return (jint) filter->flags;
}

static JNINativeMethod methods[] = {
    {(char*) "getName", (char*) "()Ljava/lang/String;", (void*) avfilter_filter_getName},
    {(char*) "getDescription", (char*) "()Ljava/lang/String;", (void*) avfilter_filter_getDescription},
    {(char*) "getFlags", (char*) "()I", (void*) avfilter_filter_getFlags},
};

int register_avfilter_filter(JNIEnv* env) {
    avfilter_filter_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avfilter_filter(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
}

MethodLoader avFilterLoader = {register_avfilter_filter, unregister_avfilter_filter};