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
 * jni_av_dictionary.h
 *
 * @author Hibate
 * Created on 2024/03/26.
 */

#ifndef MEDIA_JNI_AV_DICTIONARY_H
#define MEDIA_JNI_AV_DICTIONARY_H

#include <jni.h>

extern "C" {
#include <libavutil/dict.h>
}

void avutil_dictionary_getAVDictionary(JNIEnv* env, jobject thiz, AVDictionary** dictionary);

void avutil_dictionary_setAVDictionary(JNIEnv* env, jobject thiz, AVDictionary* dictionary);

jobject avutil_dictionary_alloc(JNIEnv* env, AVDictionary* dictionary);

#endif //MEDIA_JNI_AV_DICTIONARY_H
