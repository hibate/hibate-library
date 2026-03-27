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
 * jni_av_rational.cpp
 *
 * @author Hibate
 * Created on 2024/03/29.
 */

extern "C" {
#include <libavutil/error.h>
}

#include <jni_util.h>
#include <exceptions.h>
#include <errcode.h>

#include "jni_av_rational.h"

#define className "org/hibate/media/util/AVRational"

struct fields_t {
    jmethodID constructor;
    jfieldID numerator;
    jfieldID denominator;
};

static struct fields_t fields;

void avutil_rational_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVRational default constructor");
            return;
        }
    }
    if (nullptr == fields.numerator) {
        fields.numerator = env->GetFieldID(env->FindClass(className), "numerator", "I");
        if (nullptr == fields.numerator) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVRational.numerator field");
            return;
        }
    }
    if (nullptr == fields.denominator) {
        fields.denominator = env->GetFieldID(env->FindClass(className), "denominator", "I");
        if (nullptr == fields.denominator) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVRational.denominator field");
            return;
        }
    }
}

int avutil_rational_getAVRational(JNIEnv* env, jobject thiz, AVRational* rational) {
    if ((nullptr == fields.numerator) || (nullptr == fields.denominator)) {
        return AVERROR(ENOENT);
    }
    if ((nullptr == thiz) || (nullptr == rational)) {
        return AVERROR(EINVAL);
    }
    rational->num = env->GetIntField(thiz, fields.numerator);
    rational->den = env->GetIntField(thiz, fields.denominator);
    return RET_SUCCESS;
}

int avutil_rational_setAVRational(JNIEnv* env, jobject thiz, AVRational* rational) {
    if ((nullptr == fields.numerator) || (nullptr == fields.denominator)) {
        return AVERROR(ENOENT);
    }
    if ((nullptr == thiz) || (nullptr == rational)) {
        return AVERROR(EINVAL);
    }
    env->SetIntField(thiz, fields.numerator, rational->num);
    env->SetIntField(thiz, fields.denominator, rational->den);
    return RET_SUCCESS;
}

jobject avutil_rational_alloc(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    return env->NewObject(env->FindClass(className), fields.constructor);
}

int register_avutil_rational(JNIEnv* env) {
    avutil_rational_initialize(env);
    return 0;
}

void unregister_avutil_rational(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.numerator = nullptr;
    fields.denominator = nullptr;
}

MethodLoader avUtilRationalLoader = {register_avutil_rational, unregister_avutil_rational};
