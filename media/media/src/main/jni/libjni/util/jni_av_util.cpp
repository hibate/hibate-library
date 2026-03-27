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
 * jni_av_util.cpp
 *
 * @author Hibate
 * Created on 2026/03/25.
 */

extern "C" {
#include <libavutil/avutil.h>
}

#include <jni_util.h>

#define className "org/hibate/media/util/AVUtils"

jint avutil_util_getVersion(JNIEnv* env, jclass clazz) {
    return (jint) avutil_version();
}

static JNINativeMethod methods[] = {
    {(char*) "getVersion", (char*) "()I", (void*) avutil_util_getVersion},
};

int register_avutil_util(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avutil_util(JNIEnv* env) {
}

MethodLoader avUtilLoader = {register_avutil_util, unregister_avutil_util};
