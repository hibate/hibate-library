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
 * jni_av_codec_factory.cpp
 *
 * @author Hibate
 * Created on 2024/03/25.
 */

#include <jni_util.h>

#include "jni_av_codec.h"
#include "jni_av_codec_id.h"

#define className "org/hibate/media/codec/impl/AVCodecFactoryImpl"

jobject avcodec_factory_getEncoder(JNIEnv* env, jobject thiz, jstring jName) {
    if (nullptr == jName) {
        return nullptr;
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    if (nullptr == name) {
        return nullptr;
    }
    const AVCodecDescriptor* descriptor = avcodec_id_getAVCodecDescriptor(name);
    env->ReleaseStringUTFChars(jName, name);
    if (nullptr == descriptor) {
        return nullptr;
    }
    const AVCodec* codec = avcodec_find_encoder(descriptor->id);
    if (nullptr == codec) {
        return nullptr;
    }
    return avcodec_codec_alloc(env, codec);
}

jobject avcodec_factory_getDecoder(JNIEnv* env, jobject thiz, jstring jName) {
    if (nullptr == jName) {
        return nullptr;
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    if (nullptr == name) {
        return nullptr;
    }
    const AVCodecDescriptor* descriptor = avcodec_id_getAVCodecDescriptor(name);
    env->ReleaseStringUTFChars(jName, name);
    if (nullptr == descriptor) {
        return nullptr;
    }
    const AVCodec* codec = avcodec_find_decoder(descriptor->id);
    if (nullptr == codec) {
        return nullptr;
    }
    return avcodec_codec_alloc(env, codec);
}

static JNINativeMethod methods[] = {
    {(char*) "_getEncoder", (char*) "(Ljava/lang/String;)Lorg/hibate/media/codec/AVCodec;", (void*) avcodec_factory_getEncoder},
    {(char*) "_getDecoder", (char*) "(Ljava/lang/String;)Lorg/hibate/media/codec/AVCodec;", (void*) avcodec_factory_getDecoder},
};

int register_avcodec_factory(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avcodec_factory(JNIEnv* env) {
}

MethodLoader avCodecFactoryLoader = {register_avcodec_factory, unregister_avcodec_factory};
