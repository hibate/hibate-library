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
 * jni_av_log.cpp
 *
 * @author Hibate
 * Created on 2024/03/26.
 */

#define TAG "org.hibate.media.ffmpeg"

#include <cstdio>
#include <cstring>

#include <jni_util.h>
#include <logger.h>

#include <text/text_utils.h>

extern "C" {
#include <libavutil/log.h>
}

#define className "org/hibate/media/util/impl/AVLoggerImpl"

void avutil_log_callback(void* ctx, int level, const char* _format, va_list args) {
    LOG_PRIORITY priority;
    switch (level) {
        case AV_LOG_TRACE:
            priority = LOG_VERBOSE;
            break;
        case AV_LOG_DEBUG:
        case AV_LOG_VERBOSE:
            priority = LOG_DEBUG;
            break;
        case AV_LOG_INFO:
            priority = LOG_INFO;
            break;
        case AV_LOG_WARNING:
            priority = LOG_WARN;
            break;
        case AV_LOG_ERROR:
            priority = LOG_ERROR;
            break;
        case AV_LOG_FATAL:
            priority = LOG_FATAL;
            break;
        case AV_LOG_PANIC:
            priority = LOG_FATAL;
            break;
        case AV_LOG_QUIET:
            priority = LOG_SILENT;
            break;
        default:
            priority = NUM_PRIORITY;
            break;
    }

    if (0 == Logger.isLoggable(priority)) {
        return;
    }

    char buf[LOGGER_BUFFER_MAX_LENGTH];
    vsnprintf(buf, LOGGER_BUFFER_MAX_LENGTH, _format, args);
    const size_t size = strlen(buf);
    if (size > 0) {
        buf[size - 1] = '\0';
    }

    LPRINT(priority, buf);
}

void avutil_log_initialize(JNIEnv* env) {
    av_log_set_callback(avutil_log_callback);
}

jint avutil_log_getPriority(JNIEnv* env, jobject thiz) {
    return (jint) av_log_get_level();
}

jint avutil_log_setPriority(JNIEnv* env, jobject thiz, jint jPriority) {
    av_log_set_level(jPriority);
    return 0;
}

jint avutil_log_getFlags(JNIEnv* env, jobject thiz) {
    return (jint) av_log_get_flags();
}

jint avutil_log_setFlags(JNIEnv* env, jobject thiz, jint jFlags) {
    av_log_set_flags(jFlags);
    return 0;
}

static JNINativeMethod methods[] = {
    {(char*) "getPriority", (char*) "()I", (void*) avutil_log_getPriority},
    {(char*) "_setPriority", (char*) "(I)I", (void*) avutil_log_setPriority},
    {(char*) "getFlags", (char*) "()I", (void*) avutil_log_getFlags},
    {(char*) "_setFlags", (char*) "(I)I", (void*) avutil_log_setFlags},
};

int register_avutil_log(JNIEnv* env) {
    avutil_log_initialize(env);
    TextUtils::OnLoad(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avutil_log(JNIEnv* env) {
    TextUtils::OnUnload(env);
}

MethodLoader avUtilLogLoader = {register_avutil_log, unregister_avutil_log};
