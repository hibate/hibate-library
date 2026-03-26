/*
 * Copyright (C) 2018 Hibate <ycaia86@126.com>
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
 * jni_util.cpp
 *
 * @author Hibate
 * Created on 2017/06/19.
 */

#ifdef ANDROID
#include <android/log.h>
#endif
#include "jni_util.h"
#include "onload.h"

#define HB_TAG "OnLoad"
#ifdef ANDROID
#define HB_ERROR(format, ...) __android_log_print(ANDROID_LOG_ERROR, HB_TAG, format, ##__VA_ARGS__)
#define HB_INFO(format, ...) __android_log_print(ANDROID_LOG_INFO, HB_TAG, format, ##__VA_ARGS__)
#else
#define HB_ERROR(format, ...) fprintf(stdout, "[OnLoad] " format "\n", ##__VA_ARGS__)
#define HB_INFO(format, ...) fprintf(stdout, "[OnLoad] " format "\n", ##__VA_ARGS__)
#endif

#ifndef JNI_VERSION
#define JNI_VERSION JNI_VERSION_1_6
#endif

static JavaVM *sVm;

int throwException(JNIEnv* env, const char* className, const char* message) {
    jclass clazz = env->FindClass(className);
    if (clazz == nullptr) {
        HB_ERROR("Unable to find exception class %s", className);
        return -1;
    }

    if (env->ThrowNew(clazz, message) != JNI_OK) {
        HB_ERROR("Failed throwing '%s' '%s'", className, message);
    }

    env->DeleteLocalRef(clazz);
    return 0;
}

int throwUserException(JNIEnv* env, jclass clazz, const char* msg) {
    if (clazz == nullptr) {
        HB_ERROR("Exception class can't be null");
        return -1;
    }

    if (env->ThrowNew(clazz, msg) != JNI_OK) {
        HB_ERROR("Failed throwing '%s'", msg);
    }

    env->DeleteLocalRef(clazz);
    return 0;
}

JavaVM* getVM() {
    if (nullptr != sVm) {
        return sVm;
    }
    return nullptr;
}

JNIEnv* getJniEnv() {
    JNIEnv* env = nullptr;
    if (JNI_OK != sVm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION)) {
        return nullptr;
    }

    return env;
}

int registerNativeMethods(JNIEnv* env, const char* className,
        const JNINativeMethod* methods, const int numMethods) {
    HB_INFO("Registering '%s' natives", className);

    jclass clazz = env->FindClass(className);
    if (clazz == nullptr) {
        HB_ERROR("Native registration unable to find class '%s'", className);
        return -1;
    }
    if (env->RegisterNatives(clazz, methods, numMethods) < 0) {
        HB_ERROR("Registration of native methods for class '%s' failed", className);
        return -1;
    }

    return 0;
}

int registerMethod(JNIEnv* env, const MethodLoader* methodLoader) {
    if (nullptr == methodLoader) {
        return JNI_OK;
    }
    if (methodLoader->registerMethod) {
        if (JNI_OK != methodLoader->registerMethod(env)) {
            return JNI_ERR;
        }
    }
    return JNI_OK;
}

void unregisterMethod(JNIEnv* env, const MethodLoader* methodLoader) {
    if (nullptr == methodLoader) {
        return;
    }
    if (methodLoader->unregisterMethod) {
        methodLoader->unregisterMethod(env);
    }
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = nullptr;
    jint result = JNI_ERR;
    sVm = vm;

    if (JNI_OK != vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION)) {
        HB_ERROR("GetEnv failed!");
        return result;
    }

    HB_INFO("Loading ...");

    MethodLoader* loaders = nullptr;
    int size = 0;
    if ((0 != getMethodLoaders(&loaders, &size)) || (nullptr == loaders)) {
        HB_ERROR("Initialize native methods failed!");
        return result;
    }
    while (size > 0) {
        MethodLoader methodLoader = *loaders++;
        if (JNI_OK != registerMethod(env, &methodLoader)) {
            return result;
        }
        size--;
    }

    HB_INFO("Loaded ...");

    result = JNI_VERSION;
    env = nullptr;

    return result;
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {
    JNIEnv* env = nullptr;

    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION) != JNI_OK) {
        HB_ERROR("GetEnv failed!");
        sVm = nullptr;
        return;
    }

    HB_INFO("UnLoading ...");

    MethodLoader* loaders = nullptr;
    int size = 0;
    if ((0 != getMethodLoaders(&loaders, &size)) || (nullptr == loaders)) {
        HB_ERROR("Initialize native methods failed!");
    }
    while (size > 0) {
        MethodLoader methodLoader = *loaders++;
        unregisterMethod(env, &methodLoader);
        size--;
    }

    HB_INFO("UnLoaded");

    env = nullptr;
    sVm = nullptr;
}
