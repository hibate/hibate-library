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
 * jni_logger_handler.cpp
 *
 * @author Hibate
 * Created on 2018/04/24.
 */

#include <cstring>

#include "jni_util.h"
#include "exceptions.h"
#include "jni_logger_handler.h"

LoggerHandler::LoggerHandler(JNIEnv *env, jobject jReference) {
    this->handler = (nullptr == jReference) ? nullptr : env->NewGlobalRef(jReference);
    if (nullptr != this->handler) {
        const jclass clazz = env->GetObjectClass(this->handler);
        this->fields.onLogger = env->GetMethodID(clazz, "onLogger", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V");
        if (nullptr == this->fields.onLogger) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Cannot find onLogger method in LoggerHandler class");
            return;
        }
    }
}

LoggerHandler::~LoggerHandler() {
    JavaVM* vm = nullptr;
    JNIEnv* env = nullptr;

    if (nullptr != this->handler) {
        env = getJniEnv();
        if (nullptr == env) {
            vm = getVM();
            if (!vm) {
                goto END;
            }
#ifdef ANDROID
            vm->AttachCurrentThread(&env, nullptr);
#else
            vm->AttachCurrentThread(reinterpret_cast<void **>(&env), nullptr);
#endif
            if (!env) {
                goto END;
            }
            env->DeleteGlobalRef(this->handler);
            vm->DetachCurrentThread();
        } else {
            env->DeleteGlobalRef(this->handler);
        }
    }

END:
    this->handler = nullptr;
    this->fields.onLogger = nullptr;
}

int LoggerHandler::onLogging(JNIEnv *env, LOG_PRIORITY priority, const char *tag,
                             const char *location, const char *message) const {
    jstring jTag = toString(env, tag);
    jstring jLocation = toString(env, location);
    jstring jMessage = toString(env, message);

    env->CallVoidMethod(this->handler, this->fields.onLogger,
                        jTag, static_cast<jint>(priority), jLocation, jMessage);

    if (nullptr != jTag) {
        env->DeleteLocalRef(jTag);
    }
    if (nullptr != jLocation) {
        env->DeleteLocalRef(jLocation);
    }
    if (nullptr != jMessage) {
        env->DeleteLocalRef(jMessage);
    }
    return 0;
}

int LoggerHandler::onLogging(LOG_PRIORITY priority, const char *tag, const char *location,
                             const char *message) {
    JavaVM* vm = nullptr;
    JNIEnv* env = nullptr;

    if ((nullptr != this->handler) &&
            (nullptr != this->fields.onLogger)) {
        env = getJniEnv();
        if (nullptr == env) {
            vm = getVM();
            if (!vm) {
                return 0;
            }
#ifdef ANDROID
            vm->AttachCurrentThread(&env, nullptr);
#else
            vm->AttachCurrentThread(reinterpret_cast<void **>(&env), nullptr);
#endif
            if (!env) {
                return 0;
            }
            this->onLogging(env, priority, tag, location, message);
            vm->DetachCurrentThread();
        } else {
            this->onLogging(env, priority, tag, location, message);
        }
    }

    return 0;
}

jstring LoggerHandler::toString(JNIEnv *env, const char *buffer) {
    if (nullptr == buffer) {
        return nullptr;
    }
    const jsize size = static_cast<jsize>(strlen(buffer));
    const jclass clazz = env->FindClass("java/lang/String");
    const jstring charsets = env->NewStringUTF("UTF-8");
    const jmethodID constructor = env->GetMethodID(clazz, "<init>", "([BLjava/lang/String;)V");
    const jbyteArray array = env->NewByteArray(size);
    env->SetByteArrayRegion(array, 0, size, (jbyte *) buffer);
    return static_cast<jstring>(env->NewObject(clazz, constructor, array, charsets));
}
