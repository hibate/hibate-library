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
 * jni_av_options.cpp
 *
 * @author Hibate
 * Created on 2026/03/22.
 */

extern "C" {
#include <libavutil/opt.h>
#include <libavutil/pixdesc.h>
}

#include <jni_util.h>
#include <exceptions.h>
#include <errcode.h>

#include "jni_av_channel_layout.h"
#include "jni_av_dictionary.h"
#include "jni_av_options.h"
#include "jni_av_rational.h"

#define className "org/hibate/media/util/impl/AVOptionsImpl"

struct fields_t {
    jfieldID context;
};

static struct fields_t fields;

void avutil_options_initialize(JNIEnv* env) {
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVOptionsImpl.ctx field");
            return;
        }
    }
}

void* avutil_options_getAVClass(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (void*) env->GetLongField(thiz, fields.context);
}

void avutil_options_setAVClass(JNIEnv* env, jobject thiz, void* ctx) {
    if ((nullptr == fields.context) || (nullptr == ctx)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) ctx);
}

void avutil_options_init(JNIEnv* env, jobject thiz, jobject jObject) {
    if (nullptr == jObject) {
        throwException(env, ILLEGAL_ARGUMENT_EXCEPTION, "Argument cannot be null");
        return;
    }
    jclass clazz = env->GetObjectClass(jObject);
    jfieldID context = (nullptr == clazz) ? nullptr : env->GetFieldID(clazz, "ctx", "J");
    if (nullptr == context) {
        throwException(env, ILLEGAL_ARGUMENT_EXCEPTION, "Can't find argument ctx field");
        return;
    }
    void* object = reinterpret_cast<void *>(env->GetLongField(jObject, context));
    if (nullptr == object) {
        throwException(env, ILLEGAL_STATE_EXCEPTION, "The object may have been released");
    }
    // ReSharper disable once CppDFANullDereference
    const AVClass *c = *(AVClass **)object;
    if (!c) {
        throwException(env, ILLEGAL_ARGUMENT_EXCEPTION, "The object is not a struct whose first element is a pointer to an AVClass");
        return;
    }
    avutil_options_setAVClass(env, thiz, object);
}

jint avutil_options_put(JNIEnv* env, jobject thiz, jstring jName, jstring jValue, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(ENOENT);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = nullptr;
    const char* value = nullptr;
    name = env->GetStringUTFChars(jName, JNI_FALSE);
    if (nullptr != jValue) {
        value = env->GetStringUTFChars(jValue, JNI_FALSE);
    }
    const int ret = av_opt_set(object, name, value, jFlags);
    env->ReleaseStringUTFChars(jName, name);
    if (nullptr != jValue) {
        env->ReleaseStringUTFChars(jValue, value);
    }
    return ret;
}

jint avutil_options_put_int(JNIEnv* env, jobject thiz, jstring jName, jint jValue, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(EINVAL);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    const int ret = av_opt_set_int(object, name, jValue, jFlags);
    env->ReleaseStringUTFChars(jName, name);
    return ret;
}

jint avutil_options_put_double(JNIEnv* env, jobject thiz, jstring jName, jdouble jValue, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(EINVAL);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    const int ret = av_opt_set_double(object, name, jValue, jFlags);
    env->ReleaseStringUTFChars(jName, name);
    return ret;
}

jint avutil_options_put_q(JNIEnv* env, jobject thiz, jstring jName, jobject jValue, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(EINVAL);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    AVRational rational = {0, 1};
    int ret = avutil_rational_getAVRational(env, jValue, &rational);
    if (RET_SUCCESS == ret) {
        ret = av_opt_set_q(object, name, rational, jFlags);
    }
    env->ReleaseStringUTFChars(jName, name);
    return ret;
}

jint avutil_options_put_bin(JNIEnv* env, jobject thiz, jstring jName, jbyteArray jValue, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(EINVAL);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    const jsize size = env->GetArrayLength(jValue);
    jbyte* value = env->GetByteArrayElements(jValue, JNI_FALSE);
    const int ret = av_opt_set_bin(object, name, (const uint8_t *)value, (int) size, jFlags);
    env->ReleaseByteArrayElements(jValue, value, JNI_ABORT);
    env->ReleaseStringUTFChars(jName, name);
    return ret;
}

jint avutil_options_putImageSize(JNIEnv* env, jobject thiz, jstring jName, jint jWidth, jint jHeight, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(EINVAL);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    int ret = av_opt_set_image_size(object, name, jWidth, jHeight, jFlags);
    env->ReleaseStringUTFChars(jName, name);
    return ret;
}

jint avutil_options_putPixelFormat(JNIEnv* env, jobject thiz, jstring jName, jstring jFmt, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(EINVAL);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    const char* fmt = env->GetStringUTFChars(jFmt, JNI_FALSE);
    int ret = av_opt_set_pixel_fmt(object, name, av_get_pix_fmt(fmt), jFlags);
    env->ReleaseStringUTFChars(jName, name);
    env->ReleaseStringUTFChars(jFmt, fmt);
    return ret;
}

jint avutil_options_putSampleFormat(JNIEnv* env, jobject thiz, jstring jName, jstring jFmt, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(EINVAL);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    const char* fmt = env->GetStringUTFChars(jFmt, JNI_FALSE);
    int ret = av_opt_set_sample_fmt(object, name, av_get_sample_fmt(fmt), jFlags);
    env->ReleaseStringUTFChars(jName, name);
    env->ReleaseStringUTFChars(jFmt, fmt);
    return ret;
}

jint avutil_options_putChannelLayout(JNIEnv* env, jobject thiz, jstring jName, jobject jValue, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(EINVAL);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    AVChannelLayout* layout = avutil_channel_layout_getAVChannelLayout(env, jValue);
    int ret = av_opt_set_chlayout(object, name, layout, jFlags);
    env->ReleaseStringUTFChars(jName, name);
    return ret;
}

jint avutil_options_putVideoRate(JNIEnv* env, jobject thiz, jstring jName, jobject jValue, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(EINVAL);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    AVRational rational = {0, 1};
    int ret = avutil_rational_getAVRational(env, jValue, &rational);
    if (RET_SUCCESS == ret) {
        ret = av_opt_set_video_rate(object, name, rational, jFlags);
    }
    env->ReleaseStringUTFChars(jName, name);
    return ret;
}

jint avutil_options_put_dict_val(JNIEnv* env, jobject thiz, jstring jName, jobject jValue, jint jFlags) {
    void* object = avutil_options_getAVClass(env, thiz);
    if (nullptr == object) {
        return AVERROR(EINVAL);
    }
    if (nullptr == jName) {
        return AVERROR(EINVAL);
    }
    const char* name = env->GetStringUTFChars(jName, JNI_FALSE);
    AVDictionary* dictionary = nullptr;
    avutil_dictionary_getAVDictionary(env, jValue, &dictionary);
    const int ret = av_opt_set_dict_val(object, name, dictionary, jFlags);
    av_dict_free(&dictionary);
    env->ReleaseStringUTFChars(jName, name);
    return ret;
}

static JNINativeMethod methods[] = {
    {(char*) "_initialize", (char*) "(Ljava/lang/Object;)V", (void*) avutil_options_init},
    {(char*) "_put", (char*) "(Ljava/lang/String;Ljava/lang/String;I)I", (void*) avutil_options_put},
    {(char*) "_put", (char*) "(Ljava/lang/String;JI)I", (void*) avutil_options_put_int},
    {(char*) "_put", (char*) "(Ljava/lang/String;DI)I", (void*) avutil_options_put_double},
    {(char*) "_put", (char*) "(Ljava/lang/String;Lorg/hibate/media/util/AVRational;I)I", (void*) avutil_options_put_q},
    {(char*) "_put", (char*) "(Ljava/lang/String;[BI)I", (void*) avutil_options_put_bin},
    {(char*) "_putImageSize", (char*) "(Ljava/lang/String;III)I", (void*) avutil_options_putImageSize},
    {(char*) "_putPixelFormat", (char*) "(Ljava/lang/String;Ljava/lang/String;I)I", (void*) avutil_options_putPixelFormat},
    {(char*) "_putSampleFormat", (char*) "(Ljava/lang/String;Ljava/lang/String;I)I", (void*) avutil_options_putSampleFormat},
    {(char*) "_putVideoRate", (char*) "(Ljava/lang/String;Lorg/hibate/media/util/AVRational;I)I", (void*) avutil_options_putVideoRate},
    {(char*) "_putChannelLayout", (char*) "(Ljava/lang/String;Lorg/hibate/media/util/AVChannelLayout;I)I", (void*) avutil_options_putChannelLayout},
    {(char*) "_put", (char*) "(Ljava/lang/String;Lorg/hibate/media/util/AVDictionary;I)I", (void*) avutil_options_put_dict_val},
};

int register_avutil_options(JNIEnv* env) {
    avutil_options_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avutil_options(JNIEnv* env) {
    fields.context = nullptr;
}

MethodLoader avUtilOptionsLoader = {register_avutil_options, unregister_avutil_options};
