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
 * jni_av_format.cpp
 *
 * @author Hibate
 * Created on 2024/03/25.
 */

#include <cstdio>

#include <jni_util.h>
#include <exceptions.h>

#include "jni_av_format_context.h"
#include "jni_av_input_format.h"
#include "jni_av_output_format.h"
#include "util/jni_av_config.h"
#include "util/jni_av_dictionary.h"

#define className "org/hibate/media/format/impl/AVFormatImpl"

jobject avformat_open_inputs(JNIEnv* env, jobject thiz, jstring jUrl, jobject jDictionary) {
    const char* url = nullptr;
    int ret = 0;

    if (nullptr == jUrl) {
        throwException(env, ILLEGAL_ARGUMENT_EXCEPTION, "Media url cannot be null");
        return nullptr;
    }

    url = env->GetStringUTFChars(jUrl, JNI_FALSE);
    if (nullptr == url) {
        throwException(env, ILLEGAL_ARGUMENT_EXCEPTION,
                       "GetStringUTFChars from url failed");
        return nullptr;
    }

    AVFormatContext* ctx = nullptr;
    AVDictionary* dictionary = nullptr;
    avutil_dictionary_getAVDictionary(env, jDictionary, &dictionary);
    ret = avformat_open_input(&ctx, url, nullptr, &dictionary);
    av_dict_free(&dictionary);
    if (ret < 0) {
        char buffer[MEDIA_ERROR_BUFFER_MAX_LENGTH] = {0};
        char reason[MEDIA_ERROR_MAX_STRING_SIZE] = {0};
        av_make_error_string(reason, MEDIA_ERROR_MAX_STRING_SIZE, ret);
        snprintf(buffer, MEDIA_ERROR_BUFFER_MAX_LENGTH, "Open media failed. url: %s, ret: %d, msg: %s", url, ret, reason);

        env->ReleaseStringUTFChars(jUrl, url);
        throwException(env, IO_EXCEPTION, buffer);
        return nullptr;
    }

    ret = avformat_find_stream_info(ctx, nullptr);
    if (ret < 0) {
        char buffer[MEDIA_ERROR_BUFFER_MAX_LENGTH] = {0};
        char reason[MEDIA_ERROR_MAX_STRING_SIZE] = {0};
        av_make_error_string(reason, MEDIA_ERROR_MAX_STRING_SIZE, ret);
        snprintf(buffer, MEDIA_ERROR_BUFFER_MAX_LENGTH, "Find media stream info failed. url: %s, ret: %d, msg: %s", url, ret, reason);

        avformat_close_input(&ctx);
        ctx = nullptr;
        env->ReleaseStringUTFChars(jUrl, url);
        throwException(env, IO_EXCEPTION, buffer);
        return nullptr;
    }

    env->ReleaseStringUTFChars(jUrl, url);

    jobject context = avformat_context_alloc(env, ctx);
    // AVInputFormat
    jobject input = avformat_input_format_alloc(env, ctx);
    avformat_context_setAVInputFormat(env, context, input);
    return context;
}

jobject avformat_open_outputs(JNIEnv* env, jobject thiz, jstring jUrl, jobject jDictionary) {
    const char* url = nullptr;
    const char* format = nullptr;
    int ret = 0;

    if (nullptr == jUrl) {
        throwException(env, ILLEGAL_ARGUMENT_EXCEPTION, "Media url cannot be null");
        return nullptr;
    }

    url = env->GetStringUTFChars(jUrl, JNI_FALSE);
    if (nullptr == url) {
        throwException(env, ILLEGAL_ARGUMENT_EXCEPTION,
                       "GetStringUTFChars from url failed");
        return nullptr;
    }

    AVFormatContext* ctx = nullptr;
    AVDictionary* dictionary = nullptr;
    avutil_dictionary_getAVDictionary(env, jDictionary, &dictionary);
    if (nullptr != dictionary) {
        {
            AVDictionaryEntry* entry = av_dict_get(dictionary, "format", nullptr, 0);
            format = (nullptr == entry) ? nullptr : (const char*) entry->value;
        }
    }
    av_dict_free(&dictionary);

    const AVOutputFormat* oformat = nullptr;
    if (nullptr != format) {
        oformat = av_guess_format(format, nullptr, nullptr);
        if (!oformat) {
            char buffer[MEDIA_ERROR_BUFFER_MAX_LENGTH] = {0};
            char reason[MEDIA_ERROR_MAX_STRING_SIZE] = {0};
            av_make_error_string(reason, MEDIA_ERROR_MAX_STRING_SIZE, ret);
            snprintf(buffer, MEDIA_ERROR_BUFFER_MAX_LENGTH, "Requested output format '%s' is not known.", format);

            env->ReleaseStringUTFChars(jUrl, url);
            throwException(env, ILLEGAL_ARGUMENT_EXCEPTION, buffer);
            return nullptr;
        }
    } else {
        oformat = av_guess_format(nullptr, url, nullptr);
        if (!oformat) {
            char buffer[MEDIA_ERROR_BUFFER_MAX_LENGTH] = {0};
            char reason[MEDIA_ERROR_MAX_STRING_SIZE] = {0};
            av_make_error_string(reason, MEDIA_ERROR_MAX_STRING_SIZE, ret);
            snprintf(buffer, MEDIA_ERROR_BUFFER_MAX_LENGTH, "Unable to choose an output format for '%s'; "
                                                            "use a standard extension for the filename or specify "
                                                            "the format manually.", url);

            env->ReleaseStringUTFChars(jUrl, url);
            throwException(env, ILLEGAL_ARGUMENT_EXCEPTION, buffer);
            return nullptr;
        }
    }

    ret = avformat_alloc_output_context2(&ctx, oformat, format, url);
    if (ret < 0) {
        char buffer[MEDIA_ERROR_BUFFER_MAX_LENGTH] = {0};
        char reason[MEDIA_ERROR_MAX_STRING_SIZE] = {0};
        av_make_error_string(reason, MEDIA_ERROR_MAX_STRING_SIZE, ret);
        snprintf(buffer, MEDIA_ERROR_BUFFER_MAX_LENGTH, "Open media failed. url: %s, ret: %d, msg: %s", url, ret, reason);

        env->ReleaseStringUTFChars(jUrl, url);
        throwException(env, IO_EXCEPTION, buffer);
        return nullptr;
    }

    env->ReleaseStringUTFChars(jUrl, url);

    jobject context = avformat_context_alloc(env, ctx);
    // AVOutputFormat
    jobject output = avformat_output_format_alloc(env, ctx);
    avformat_context_setAVOutputFormat(env, context, output);
    return context;
}

jobject avformat_open(JNIEnv* env, jobject thiz, jstring jUrl, jobject jDictionary, jint mode) {
    return (0 == mode) ? avformat_open_inputs(env, thiz, jUrl, jDictionary) : avformat_open_outputs(env, thiz, jUrl, jDictionary);
}

static JNINativeMethod methods[] = {
    {(char*) "_open", (char*) "(Ljava/lang/String;Lorg/hibate/media/util/AVDictionary;I)Lorg/hibate/media/format/AVFormatContext;", (void*) avformat_open},
};

int register_avformat(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avformat(JNIEnv* env) {
}

MethodLoader avFormatLoader = {register_avformat, unregister_avformat};
