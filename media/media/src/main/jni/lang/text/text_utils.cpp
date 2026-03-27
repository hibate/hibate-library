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
 * text_utils.cpp
 *
 * @author Hibate
 * Created on 2026/03/21.
 */

#include <cstring>

#include "text/text_utils.h"

struct fields_t {
    jclass clazz;
    jmethodID constructor;
};

static struct fields_t fields;

TextUtils::TextUtils() = default;

TextUtils::~TextUtils() = default;

int TextUtils::OnLoad(JNIEnv *env) {
    if (nullptr == fields.clazz) {
        jclass clazz = env->FindClass("java/lang/String");
        fields.clazz = (jclass) env->NewGlobalRef(clazz);
        env->DeleteLocalRef(clazz);
    }
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(fields.clazz, "<init>", "([BLjava/lang/String;)V");
    }
    return 0;
}

void TextUtils::OnUnload(JNIEnv *env) {
    if (nullptr != fields.clazz) {
        env->DeleteGlobalRef(fields.clazz);
    }
    fields.constructor = nullptr;
    fields.clazz = nullptr;
}

jstring TextUtils::ToUTF8(JNIEnv *env, const char *text) {
    if (nullptr == text) {
        return nullptr;
    }
    const auto size = static_cast<jsize>(strlen(text));
    const auto charsets = env->NewStringUTF("UTF-8");
    const auto array = env->NewByteArray(size);
    env->SetByteArrayRegion(array, 0, size, (jbyte *) text);
    const auto object = (jstring) env->NewObject(fields.clazz, fields.constructor, array, charsets);
    env->DeleteLocalRef(array);
    env->DeleteLocalRef(charsets);
    return object;
}
