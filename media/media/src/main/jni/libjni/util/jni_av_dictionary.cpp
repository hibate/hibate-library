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
 * jni_av_dictionary.cpp
 *
 * @author Hibate
 * Created on 2024/03/26.
 */

#include <cstdio>

#include <jni_util.h>
#include <exceptions.h>

#include <text/text_utils.h>

#include "jni_av_dictionary.h"

#define className "org/hibate/media/util/impl/AVDictionaryImpl"

struct fields_t {
    jclass string;
    jclass number;
    jmethodID constructor;
    jfieldID map;
    jmethodID keySet;
    jmethodID get;
    jmethodID put;
    jmethodID iterator;
    jmethodID hasNext;
    jmethodID next;
    jmethodID longValue;
};

static struct fields_t fields;

void avutil_dictionary_initialize(JNIEnv* env) {
    if (nullptr == fields.string) {
        jclass clazz = env->FindClass("java/lang/String");
        fields.string = (jclass) env->NewGlobalRef(clazz);
        env->DeleteLocalRef(clazz);
    }
    if (nullptr == fields.number) {
        jclass clazz = env->FindClass("java/lang/Number");
        fields.number = (jclass) env->NewGlobalRef(clazz);
        env->DeleteLocalRef(clazz);
    }
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVDictionaryImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.map) {
        fields.map = env->GetFieldID(env->FindClass(className), "map",
                                       "Ljava/util/Map;");
        if (nullptr == fields.map) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVDictionaryImpl.map field");
            return;
        }
    }
    if (nullptr == fields.keySet) {
        fields.keySet = env->GetMethodID(env->FindClass("java/util/Map"), "keySet", "()Ljava/util/Set;");
    }
    if (nullptr == fields.get) {
        fields.get = env->GetMethodID(env->FindClass("java/util/Map"), "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
    }
    if (nullptr == fields.put) {
        fields.put = env->GetMethodID(env->FindClass("java/util/Map"), "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    }
    if (nullptr == fields.iterator) {
        fields.iterator = env->GetMethodID(env->FindClass("java/util/Set"), "iterator", "()Ljava/util/Iterator;");
    }
    if (nullptr == fields.hasNext) {
        fields.hasNext = env->GetMethodID(env->FindClass("java/util/Iterator"), "hasNext", "()Z");
    }
    if (nullptr == fields.next) {
        fields.next = env->GetMethodID(env->FindClass("java/util/Iterator"), "next", "()Ljava/lang/Object;");
    }
    if (nullptr == fields.longValue) {
        fields.longValue = env->GetMethodID(env->FindClass("java/lang/Number"), "longValue", "()J");
    }
}

void avutil_dictionary_getAVDictionary(JNIEnv* env, jobject thiz, AVDictionary** dictionary) {
    if ((nullptr == fields.map) || (nullptr == thiz)) {
        return;
    }

    auto jMap = (jobject) env->GetObjectField(thiz, fields.map);
    if (nullptr == jMap) {
        return;
    }

    auto jKeySet = (jobject) env->CallObjectMethod(jMap, fields.keySet);
    auto jIterator = (jobject) env->CallObjectMethod(jKeySet, fields.iterator);
    while (env->CallBooleanMethod(jIterator, fields.hasNext)) {
        jobject jKey = env->CallObjectMethod(jIterator, fields.next);
        if (nullptr == jKey) {
            continue;
        }
        jobject jValue = env->CallObjectMethod(jMap, fields.get, jKey);
        if (nullptr == jValue) {
            env->DeleteLocalRef(jKey);
            continue;
        }

        const char* key = env->GetStringUTFChars((jstring) jKey, JNI_FALSE);
        if (nullptr == key) {
            env->DeleteLocalRef(jKey);
            env->DeleteLocalRef(jValue);
            continue;
        }

        if (env->IsInstanceOf(jValue, fields.string)) {
            const char* value = env->GetStringUTFChars((jstring) jValue, JNI_FALSE);
            if (nullptr != value) {
                av_dict_set(dictionary, key, value, 0);
                env->ReleaseStringUTFChars((jstring) jValue, value);
            }
        } else if (env->IsInstanceOf(jValue, fields.number)) {
            jlong value = env->CallLongMethod(jValue, fields.longValue);
            av_dict_set_int(dictionary, key, (int64_t) value, 0);
        }

        env->ReleaseStringUTFChars((jstring) jKey, key);
        env->DeleteLocalRef(jKey);
        env->DeleteLocalRef(jValue);
    }
    env->DeleteLocalRef(jIterator);
    env->DeleteLocalRef(jKeySet);
    env->DeleteLocalRef(jMap);
}

void avutil_dictionary_setAVDictionary(JNIEnv* env, jobject thiz, AVDictionary* dictionary) {
    if ((nullptr == thiz) || (nullptr == dictionary)) {
        return;
    }

    auto jMap = (jobject) env->GetObjectField(thiz, fields.map);
    if (nullptr == jMap) {
        return;
    }

    AVDictionaryEntry* entry = nullptr;
    while (nullptr != (entry = av_dict_get(dictionary, "", entry, AV_DICT_IGNORE_SUFFIX))) {
        if (nullptr == entry->key) {
            continue;
        }
        env->CallObjectMethod(jMap, fields.put, TextUtils::ToUTF8(env, entry->key), TextUtils::ToUTF8(env, entry->value));
    }
}

jobject avutil_dictionary_alloc(JNIEnv* env, AVDictionary* dictionary) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    jobject object = env->NewObject(env->FindClass(className), fields.constructor);
    avutil_dictionary_setAVDictionary(env, object, dictionary);
    return object;
}

jobject avutil_dictionary_clone(JNIEnv* env, jobject thiz) {
    AVDictionary* dictionary = nullptr;
    avutil_dictionary_getAVDictionary(env, thiz, &dictionary);
    return avutil_dictionary_alloc(env, dictionary);
}

static JNINativeMethod methods[] = {
    {(char*) "clone", (char*) "()Lorg/hibate/media/util/AVDictionary;", (void*) avutil_dictionary_clone},
};

int register_avutil_dictionary(JNIEnv* env) {
    avutil_dictionary_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avutil_dictionary(JNIEnv* env) {
    if (nullptr != fields.string) {
        env->DeleteGlobalRef(fields.string);
    }
    if (nullptr != fields.number) {
        env->DeleteGlobalRef(fields.number);
    }
    fields.string = nullptr;
    fields.number = nullptr;
    fields.constructor = nullptr;
    fields.map = nullptr;
    fields.keySet = nullptr;
    fields.get = nullptr;
    fields.put = nullptr;
    fields.iterator = nullptr;
    fields.hasNext = nullptr;
    fields.next = nullptr;
    fields.longValue = nullptr;
}

MethodLoader avUtilDictionaryLoader = {register_avutil_dictionary, unregister_avutil_dictionary};
