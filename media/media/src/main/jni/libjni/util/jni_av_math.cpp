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
 * jni_av_math.cpp
 *
 * @author Hibate
 * Created on 2026/03/23.
 */

extern "C" {
#include <libavutil/mathematics.h>
}

#include <jni_util.h>

#include "jni_av_rational.h"

#define className "org/hibate/media/util/AVMath"

jlong avutil_math_rescale_q(JNIEnv* env, jclass clazz, jlong jA, jobject jBq, jobject jCq) {
    AVRational bq = {0, 1};
    AVRational cq = {0, 1};
    avutil_rational_getAVRational(env, jBq, &bq);
    avutil_rational_getAVRational(env, jCq, &cq);
    return (jlong) av_rescale_q((int64_t) jA, bq, cq);
}

static JNINativeMethod methods[] = {
    {(char*) "rescale", (char*) "(JLorg/hibate/media/util/AVRational;Lorg/hibate/media/util/AVRational;)J", (void*) avutil_math_rescale_q},
};

int register_avutil_math(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avutil_math(JNIEnv* env) {
}

MethodLoader avUtilMathLoader = {register_avutil_math, unregister_avutil_math};
