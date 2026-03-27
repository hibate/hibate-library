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
 * jni_av_filter_factory.cpp
 *
 * @author Hibate
 * Created on 2026/03/21.
 */

#include <jni_util.h>

#include "jni_av_filter.h"

#define className "org/hibate/media/filter/impl/AVFilterFactoryImpl"

jobject avfilter_factory_getFilter(JNIEnv* env, jclass clazz, jstring jName) {
    const AVFilter *filter = nullptr;
    const char* name = nullptr;
    if (nullptr != jName) {
        name = env->GetStringUTFChars(jName, JNI_FALSE);
    }
    if (nullptr != name) {
        filter = avfilter_get_by_name(name);
    }
    if (nullptr != jName) {
        env->ReleaseStringUTFChars(jName, name);
    }
    return (nullptr == filter) ? nullptr : avfilter_filter_alloc(env, filter);
}

static JNINativeMethod methods[] = {
    {(char*) "_getFilter", (char*) "(Ljava/lang/String;)Lorg/hibate/media/filter/AVFilter;", (void*) avfilter_factory_getFilter},
};

int register_avfilter_factory(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avfilter_factory(JNIEnv* env) {
}

MethodLoader avFilterFactoryLoader = {register_avfilter_factory, unregister_avfilter_factory};
