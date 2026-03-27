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
 * jni_av_filter_in_out.cpp
 *
 * @author Hibate
 * Created on 2026/03/21.
 */

#include <jni_util.h>
#include <exceptions.h>

#include <text/text_utils.h>

#include "jni_av_filter_context.h"
#include "jni_av_filter_in_out.h"

#define className "org/hibate/media/filter/impl/AVFilterInOutImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
};

static struct fields_t fields;

void avfilter_inout_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "(I)V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFilterInOutImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFilterInOutImpl.ctx field");
            return;
        }
    }
}

AVFilterInOut* avfilter_inout_getAVFilterInOut(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVFilterInOut*) env->GetLongField(thiz, fields.context);
}

void avfilter_inout_setAVFilterInOut(JNIEnv* env, jobject thiz, AVFilterInOut* filter) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) filter);
}

jobject avfilter_inout_allocate(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }
    return env->NewObject(env->FindClass(className), fields.constructor, 0);
}

void avfilter_inout_init(JNIEnv* env, jobject thiz) {
    AVFilterInOut* filter = avfilter_inout_alloc();
    if (nullptr == fields.context) {
        throwException(env, RUNTIME_EXCEPTION, "AVFilterInOut allocate failed");
        return;
    }
    avfilter_inout_setAVFilterInOut(env, thiz, filter);
}

jstring avfilter_inout_getName(JNIEnv* env, jobject thiz) {
    AVFilterInOut* filter = avfilter_inout_getAVFilterInOut(env, thiz);
    if (nullptr == filter) {
        return nullptr;
    }
    return TextUtils::ToUTF8(env, filter->name);
}

jint avfilter_inout_setName(JNIEnv* env, jobject thiz, jstring jName) {
    AVFilterInOut* filter = avfilter_inout_getAVFilterInOut(env, thiz);
    if (nullptr == filter) {
        return AVERROR(ENOENT);
    }

    const char* name = nullptr;
    if (nullptr != jName) {
        name = env->GetStringUTFChars(jName, JNI_FALSE);
    }
    filter->name = av_strdup(name);
    if (nullptr != jName) {
        env->ReleaseStringUTFChars(jName, name);
    }
    return 0;
}

jobject avfilter_inout_getFilterContext(JNIEnv* env, jobject thiz) {
    AVFilterInOut* filter = avfilter_inout_getAVFilterInOut(env, thiz);
    if ((nullptr == filter) || (nullptr == filter->filter_ctx)) {
        return nullptr;
    }
    return avfilter_context_alloc(env, filter->filter_ctx);
}

jint avfilter_inout_setFilterContext(JNIEnv* env, jobject thiz, jobject jContext) {
    AVFilterInOut* filter = avfilter_inout_getAVFilterInOut(env, thiz);
    if (nullptr == filter) {
        return AVERROR(ENOENT);
    }
    filter->filter_ctx = avfilter_context_getAVFilterContext(env, jContext);
    return 0;
}

jint avfilter_inout_getIndex(JNIEnv* env, jobject thiz) {
    AVFilterInOut* filter = avfilter_inout_getAVFilterInOut(env, thiz);
    if (nullptr == filter) {
        return 0;
    }
    return filter->pad_idx;
}

jint avfilter_inout_setIndex(JNIEnv* env, jobject thiz, jint jIndex) {
    AVFilterInOut* filter = avfilter_inout_getAVFilterInOut(env, thiz);
    if (nullptr == filter) {
        return AVERROR(ENOENT);
    }
    filter->pad_idx = jIndex;
    return 0;
}

jobject avfilter_inout_getNext(JNIEnv* env, jobject thiz) {
    AVFilterInOut* filter = avfilter_inout_getAVFilterInOut(env, thiz);
    if ((nullptr == filter) || (nullptr == filter->next)) {
        return nullptr;
    }
    jobject object = avfilter_inout_allocate(env);
    avfilter_inout_setAVFilterInOut(env, object, filter->next);
    return object;
}

jint avfilter_inout_setNext(JNIEnv* env, jobject thiz, jobject jNext) {
    AVFilterInOut* filter = avfilter_inout_getAVFilterInOut(env, thiz);
    if (nullptr == filter) {
        return AVERROR(ENOENT);
    }
    if (nullptr != filter->next) {
        avfilter_inout_free(&filter->next);
    }
    filter->next = avfilter_inout_getAVFilterInOut(env, jNext);
    return 0;
}

void avfilter_inout_close(JNIEnv* env, jobject thiz) {
    AVFilterInOut* filter = avfilter_inout_getAVFilterInOut(env, thiz);
    if (nullptr != filter) {
        avfilter_inout_free(&filter);
    }
    avfilter_inout_setAVFilterInOut(env, thiz, nullptr);
}

static JNINativeMethod methods[] = {
    {(char*) "_initialize", (char*) "()V", (void*) avfilter_inout_init},
    {(char*) "getName", (char*) "()Ljava/lang/String;", (void*) avfilter_inout_getName},
    {(char*) "_setName", (char*) "(Ljava/lang/String;)I", (void*) avfilter_inout_setName},
    {(char*) "getFilterContext", (char*) "()Lorg/hibate/media/filter/AVFilterContext;", (void*) avfilter_inout_getFilterContext},
    {(char*) "_setFilterContext", (char*) "(Lorg/hibate/media/filter/AVFilterContext;)I", (void*) avfilter_inout_setFilterContext},
    {(char*) "getIndex", (char*) "()I", (void*) avfilter_inout_getIndex},
    {(char*) "_setIndex", (char*) "(I)I", (void*) avfilter_inout_setIndex},
    {(char*) "getNext", (char*) "()Lorg/hibate/media/filter/AVFilterInOut;", (void*) avfilter_inout_getNext},
    {(char*) "_setNext", (char*) "(Lorg/hibate/media/filter/AVFilterInOut;)I", (void*) avfilter_inout_setNext},
    {(char*) "close", (char*) "()V", (void*) avfilter_inout_close},
};

int register_avfilter_inout(JNIEnv* env) {
    avfilter_inout_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avfilter_inout(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
}

MethodLoader avFilterInOutLoader = {register_avfilter_inout, unregister_avfilter_inout};
