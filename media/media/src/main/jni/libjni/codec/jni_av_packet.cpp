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
 * jni_av_packet.cpp
 *
 * @author Hibate
 * Created on 2024/03/27.
 */

#include <cstdio>

#include <jni_util.h>
#include <exceptions.h>

#include "jni_av_packet.h"
#include "util/jni_av_rational.h"

#define className "org/hibate/media/codec/impl/AVPacketImpl"

struct fields_t {
    jfieldID context;
};

static struct fields_t fields;

void avpacket_initialize(JNIEnv* env) {
    if (nullptr == fields.context) {
        fields.context = env->GetFieldID(env->FindClass(className), "ctx", "J");
        if (nullptr == fields.context) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Can't find AVPacketImpl.ctx field");
            return;
        }
    }
}

AVPacket* avpacket_getAVPacket(JNIEnv* env, jobject thiz) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return nullptr;
    }
    return (AVPacket*) env->GetLongField(thiz, fields.context);
}

void avpacket_setAVPacket(JNIEnv* env, jobject thiz, AVPacket* packet) {
    if ((nullptr == fields.context) || (nullptr == thiz)) {
        return;
    }
    env->SetLongField(thiz, fields.context, (jlong) packet);
}

void avpacket_init(JNIEnv* env, jobject thiz, jint jCapacity) {
    AVPacket* packet = av_packet_alloc();
    if (nullptr == packet) {
        throwException(env, RUNTIME_EXCEPTION, "AVPacket allocate failed");
        return;
    }
    const int capacity = static_cast<int>(jCapacity);
    if (capacity > 0) {
        const int ret = av_new_packet(packet, static_cast<int>(jCapacity));
        if (ret < 0) {
            av_packet_free(&packet);
            throwException(env, RUNTIME_EXCEPTION, "AVPacket initialize failed");
            return;
        }
    }
    avpacket_setAVPacket(env, thiz, packet);
}

jlong avpacket_getPts(JNIEnv* env, jobject thiz) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return (jlong) AV_NOPTS_VALUE;
    }
    return (jlong) packet->pts;
}

jlong avpacket_getDts(JNIEnv* env, jobject thiz) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return (jlong) AV_NOPTS_VALUE;
    }
    return (jlong) packet->dts;
}

jint avpacket_getSize(JNIEnv* env, jobject thiz) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return 0;
    }
    return (jint) packet->size;
}

jint avpacket_getIndex(JNIEnv* env, jobject thiz) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return 0;
    }
    return (jint) packet->stream_index;
}

jint avpacket_setIndex(JNIEnv* env, jobject thiz, jint jIndex) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return AVERROR(ENOENT);
    }
    packet->stream_index = jIndex;
    return 0;
}

jint avpacket_getFlags(JNIEnv* env, jobject thiz) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return AV_PKT_FLAG_CORRUPT;
    }
    return (jint) packet->flags;
}

jlong avpacket_getDuration(JNIEnv* env, jobject thiz) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return 0;
    }
    return (jlong) packet->duration;
}

jlong avpacket_getPosition(JNIEnv* env, jobject thiz) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return -1;
    }
    return (jlong) packet->pos;
}

jint avpacket_setPosition(JNIEnv* env, jobject thiz, jlong jPosition) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return AVERROR(ENOENT);
    }
    packet->pos = jPosition;
    return 0;
}

jint avpacket_rescale(JNIEnv* env, jobject thiz, jobject jSrc, jobject jDst) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return AVERROR(ENOENT);
    }
    if ((nullptr == jSrc) || (nullptr == jDst)) {
        return AVERROR(EINVAL);
    }

    AVRational tb_src = {0, 1};
    AVRational tb_dst = {0, 1};
    avutil_rational_getAVRational(env, jSrc, &tb_src);
    avutil_rational_getAVRational(env, jDst, &tb_dst);
    av_packet_rescale_ts(packet, tb_src, tb_dst);
    return 0;
}

jint avpacket_unref(JNIEnv* env, jobject thiz) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr == packet) {
        return AVERROR(ENOENT);
    }
    av_packet_unref(packet);
    return 0;
}

void avpacket_close(JNIEnv* env, jobject thiz) {
    AVPacket* packet = avpacket_getAVPacket(env, thiz);
    if (nullptr != packet) {
        av_packet_free(&packet);
    }
    avpacket_setAVPacket(env, thiz, nullptr);
}

static JNINativeMethod methods[] = {
    {(char*) "_initialize", (char*) "(I)V", (void*) avpacket_init},
    {(char*) "getPts", (char*) "()J", (void*) avpacket_getPts},
    {(char*) "getDts", (char*) "()J", (void*) avpacket_getDts},
    {(char*) "getSize", (char*) "()I", (void*) avpacket_getSize},
    {(char*) "getIndex", (char*) "()I", (void*) avpacket_getIndex},
    {(char*) "_setIndex", (char*) "(I)I", (void*) avpacket_setIndex},
    {(char*) "getFlags", (char*) "()I", (void*) avpacket_getFlags},
    {(char*) "getDuration", (char*) "()J", (void*) avpacket_getDuration},
    {(char*) "getPosition", (char*) "()J", (void*) avpacket_getPosition},
    {(char*) "_setPosition", (char*) "(J)I", (void*) avpacket_setPosition},
    {(char*) "_rescale", (char*) "(Lorg/hibate/media/util/AVRational;Lorg/hibate/media/util/AVRational;)I", (void*) avpacket_rescale},
    {(char*) "_unref", (char*) "()I", (void*) avpacket_unref},
    {(char*) "close", (char*) "()V", (void*) avpacket_close}
};

int register_avpacket(JNIEnv* env) {
    avpacket_initialize(env);
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_avpacket(JNIEnv* env) {
    fields.context = nullptr;
}

MethodLoader avPacketLoader = {register_avpacket, unregister_avpacket};
