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
 * jni_logger_context.cpp
 *
 * @author Hibate
 * Created on 2018/04/24.
 */

#include "jni_util.h"
#include "errcode.h"
#include "logger.h"
#include "jni_logger_handler.h"

#define className "org/hibate/logger/natives/LoggerImpl"

void logger_context_initialize(JNIEnv* env, jobject thiz, jobject jHandler) {
    if (nullptr != jHandler) {
        LoggerContext::addLoggerListener(new LoggerHandler(env, jHandler));
    }
}

jboolean logger_context_isEnabled(JNIEnv* env, jobject thiz, jint jPriority) {
    const auto priority = static_cast<LOG_PRIORITY>(jPriority);
    const auto ctx = LoggerContext::getInstance();
    return ctx->isLoggable(priority);
}

jboolean logger_context_setPriority(JNIEnv* env, jobject thiz, jint jPriority) {
    const auto priority = static_cast<LOG_PRIORITY>(jPriority);
    const auto ctx = LoggerContext::getInstance();
    return (RET_NO_ERROR == ctx->setPriority(priority)) ? JNI_TRUE : JNI_FALSE;
}

jboolean logger_context_isLineEnabled(JNIEnv* env, jobject thiz) {
    const auto ctx = LoggerContext::getInstance();
    return ctx->isLineEnabled();
}

jboolean logger_context_setLineEnabled(JNIEnv* env, jobject thiz, jboolean jLoggable) {
    const auto ctx = LoggerContext::getInstance();
    return (RET_NO_ERROR == ctx->setLineEnabled((JNI_TRUE == jLoggable) ? 1 : 0)) ?
           JNI_TRUE : JNI_FALSE;
}

static JNINativeMethod methods[] = {
        {(char*) "_initialize", (char*) "(Lorg/hibate/logger/natives/LoggerHandler;)V", (void*) logger_context_initialize},
        {(char*) "_isEnabled", (char*) "(I)Z", (void*) logger_context_isEnabled},
        {(char*) "_setPriority", (char*) "(I)Z", (void*) logger_context_setPriority},
        {(char*) "_isLineEnabled", (char*) "()Z", (void*) logger_context_isLineEnabled},
        {(char*) "_setLineEnabled", (char*) "(Z)Z", (void*) logger_context_setLineEnabled},
};

int register_logger_context(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_logger_context(JNIEnv* env) {
}

MethodLoader logger_context_loader = {register_logger_context, unregister_logger_context};