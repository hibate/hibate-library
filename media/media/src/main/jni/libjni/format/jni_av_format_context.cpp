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
 * jni_av_format_context.cpp
 *
 * @author Hibate
 * Created on 2024/03/25.
 */

#include <jni_util.h>
#include <exceptions.h>

#include <text/text_utils.h>

#include "jni_av_format_context.h"
#include "jni_av_stream.h"

#define className "org/hibate/media/format/impl/AVFormatContextImpl"

struct fields_t {
    jmethodID constructor;
    jfieldID context;
    jfieldID input;
    jfieldID output;
};

static struct fields_t fields;

void avformat_context_initialize(JNIEnv* env) {
    if (nullptr == fields.constructor) {
        fields.constructor = env->GetMethodID(env->FindClass(className), "<init>", "()V");
        if (nullptr == fields.constructor) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFormatContextImpl default constructor");
            return;
        }
    }
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFormatContextImpl.ctx field");
            return;
        }
    }
    if (nullptr == fields.input) {
        fields.input = env->GetFieldID(env->FindClass(className), "inputFormat",
                                       "Lorg/hibate/media/format/AVInputFormat;");
        if (nullptr == fields.input) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFormatContextImpl.inputFormat field");
            return;
        }
    }
    if (nullptr == fields.output) {
        fields.output = env->GetFieldID(env->FindClass(className), "outputFormat",
                                       "Lorg/hibate/media/format/AVOutputFormat;");
        if (nullptr == fields.output) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVFormatContextImpl.outputFormat field");
            return;
        }
    }
}

AVFormatContext* avformat_context_getAVFormatContext(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVFormatContext*) env->GetLongField(thiz, fields.context);
}

void avformat_context_setAVFormatContext(JNIEnv* env, jobject thiz, AVFormatContext* ctx) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) ctx);
}

jobject avformat_context_alloc(JNIEnv* env, AVFormatContext* ctx) {
    if (nullptr == fields.constructor) {
        return nullptr;
    }

    jobject object = env->NewObject(env->FindClass(className), fields.constructor);
    avformat_context_setAVFormatContext(env, object, ctx);
    return object;
}

jobject avformat_context_getAVInputFormat(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.input) || (nullptr == thiz)) {
        return nullptr;
    }
    return env->GetObjectField(thiz, fields.input);
}

void avformat_context_setAVInputFormat(JNIEnv* env, jobject thiz, jobject input) {
    if ((nullptr != fields.input) && (nullptr != thiz)) {
        env->SetObjectField(thiz, fields.input, input);
    }
}

void avformat_context_setAVOutputFormat(JNIEnv* env, jobject thiz, jobject output) {
    if (nullptr != fields.output) {
        env->SetObjectField(thiz, fields.output, output);
    }
}

jint avformat_context_getStreams(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_context_getAVFormatContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }

    return (jint) ctx->nb_streams;
}

jobject avformat_context_getStream(JNIEnv* env, jobject thiz, jint jIndex) {
    AVFormatContext* ctx = avformat_context_getAVFormatContext(env, thiz);
    if (nullptr == ctx) {
        return nullptr;
    }

    if ((jIndex < 0) || (static_cast<unsigned int>(jIndex) >= ctx->nb_streams)) {
        throwException(env, ILLEGAL_ARGUMENT_EXCEPTION, "Out of range of streams");
        return nullptr;
    }

    const AVStream* st = ctx->streams[jIndex];
    return avstream_alloc(env, st);
}

jobject avformat_context_addStream(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_context_getAVFormatContext(env, thiz);
    if (nullptr == ctx) {
        return nullptr;
    }

    AVStream* st = avformat_new_stream(ctx, nullptr);
    return avstream_alloc(env, st);
}

jstring avformat_context_getUrl(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_context_getAVFormatContext(env, thiz);
    if (nullptr == ctx) {
        return nullptr;
    }

    const char* url = ctx->url;
    return TextUtils::ToUTF8(env, url);
}

jlong avformat_context_getStartTime(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_context_getAVFormatContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return (jlong) (ctx->start_time / AV_TIME_BASE);
}

jlong avformat_context_getDuration(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_context_getAVFormatContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return (jlong) (ctx->duration / AV_TIME_BASE);
}

jlong avformat_context_getBitRate(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_context_getAVFormatContext(env, thiz);
    if (nullptr == ctx) {
        return 0;
    }
    return (jlong) ctx->bit_rate;
}

jint avformat_context_setAvoidNegativeTs(JNIEnv* env, jobject thiz, jint jFlags) {
    AVFormatContext* ctx = avformat_context_getAVFormatContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    ctx->avoid_negative_ts = jFlags;
    return 0;
}

jint avformat_context_print(JNIEnv* env, jobject thiz, jint jIndex) {
    AVFormatContext* ctx = avformat_context_getAVFormatContext(env, thiz);
    if (nullptr == ctx) {
        return AVERROR(ENOENT);
    }
    jobject input = avformat_context_getAVInputFormat(env, thiz);
    av_dump_format(ctx, jIndex, ctx->url, (nullptr == input) ? 1 : 0);
    return 0;
}

void avformat_context_close(JNIEnv* env, jobject thiz) {
    AVFormatContext* ctx = avformat_context_getAVFormatContext(env, thiz);
    if (nullptr != ctx) {
        jobject input = env->GetObjectField(thiz, fields.input);
        if (nullptr != input) {
            avformat_close_input(&ctx);
        } else {
            avformat_free_context(ctx);
        }
    }
    avformat_context_setAVFormatContext(env, thiz, nullptr);
}

static JNINativeMethod methods[] = {
    {(char*) "getStreams", (char*) "()I", (void*) avformat_context_getStreams},
    {(char*) "getStream", (char*) "(I)Lorg/hibate/media/format/AVStream;", (void*) avformat_context_getStream},
    {(char*) "_addStream", (char*) "()Lorg/hibate/media/format/AVStream;", (void*) avformat_context_addStream},
    {(char*) "getUrl", (char*) "()Ljava/lang/String;", (void*) avformat_context_getUrl},
    {(char*) "getStartTime", (char*) "()J", (void*) avformat_context_getStartTime},
    {(char*) "getDuration", (char*) "()J", (void*) avformat_context_getDuration},
    {(char*) "getBitRate", (char*) "()J", (void*) avformat_context_getBitRate},
    {(char*) "_setAvoidNegativeTs", (char*) "(I)I", (void*) avformat_context_setAvoidNegativeTs},
    {(char*) "_print", (char*) "(I)I", (void*) avformat_context_print},
    {(char*) "close", (char*) "()V", (void*) avformat_context_close},
};

int register_avformat_context(JNIEnv* env) {
    avformat_context_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avformat_context(JNIEnv* env) {
    fields.constructor = nullptr;
    fields.context = nullptr;
    fields.input = nullptr;
    fields.output = nullptr;
}

MethodLoader avFormatContextLoader = {register_avformat_context, unregister_avformat_context};
