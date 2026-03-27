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
 * jni_codec_capabilities.cpp
 *
 * @author Hibate
 * Created on 2018/04/02.
 */

#include <cstdio>
#include <cstring>

#include <libyuv.h>

#include <jni_util.h>
#include <errcode.h>

#define className "org/hibate/graphics/codec/CodecCapabilitiesImpl"

int codec_yuv_ConvertToI420(uint8_t* src, int width, int height, int rotation,
                            uint32_t fourcc, uint8_t** dst) {
    int src_y_size = width * height;

    uint8_t* dst_y = *dst;
    uint8_t* dst_u = dst_y + src_y_size;
    uint8_t* dst_v = dst_u + src_y_size / 4;

    int crop_width = 0;
    int crop_height = 0;

    if (rotation % 180 == 0) {
        crop_width = width;
        crop_height = height;
    } else {
        crop_width = crop_height;
        crop_height = crop_width;
    }

    return libyuv::ConvertToI420(src, src_y_size,
                                 dst_y, crop_width,
                                 dst_u, crop_width / 2,
                                 dst_v, crop_width / 2,
                                 0, 0,
                                 width, height,
                                 crop_width, crop_height,
                                 (libyuv::RotationMode) rotation,
                                 fourcc);
}

int codec_yuv_ConvertFromI420(uint8_t* src, int width, int height, int rotation,
                              uint32_t fourcc, uint8_t** dst) {
    int src_y_size = width * height;

    uint8_t* src_y = src;
    uint8_t* src_u = src_y + src_y_size;
    uint8_t* src_v = src_u + src_y_size / 4;

    int crop_width = 0;
    int crop_height = 0;

    if (rotation % 180 == 0) {
        crop_width = width;
        crop_height = height;
    } else {
        crop_width = crop_height;
        crop_height = crop_width;
    }

    return libyuv::ConvertFromI420(src_y, crop_width,
                                   src_u, crop_width / 2,
                                   src_v, crop_width / 2,
                                   *dst, crop_width,
                                   crop_width, crop_height,
                                   fourcc);
}

int codec_yuv_I420Mirror(uint8_t* src, int width, int height, uint8_t** dst) {
    int src_y_size = width * height;

    uint8_t* src_y = src;
    uint8_t* src_u = src_y + src_y_size;
    uint8_t* src_v = src_u + src_y_size / 4;

    uint8_t* dst_y = *dst;
    uint8_t* dst_u = dst_y + src_y_size;
    uint8_t* dst_v = dst_u + src_y_size / 4;

    int crop_width = width;
    int crop_height = height;

    return libyuv::I420Mirror(src_y, crop_width,
                              src_u, crop_width / 2,
                              src_v, crop_width / 2,
                              dst_y, crop_width,
                              dst_u, crop_width / 2,
                              dst_v, crop_width / 2,
                              crop_width, crop_height);
}

jbyteArray codec_capabilities_toPixelFormat(JNIEnv* env, jobject thiz,
        jbyteArray jSrc, jint jWidth, jint jHeight,
        jint from, jint to,
        jint jRotation, jboolean jMirror) {
    if ((nullptr == jSrc) ||
            (jWidth <= 0) ||
            (jHeight <= 0)) {
        return jSrc;
    }

    int ret = 0;
    jbyte *bytes = nullptr;
    bytes = env->GetByteArrayElements(jSrc, JNI_FALSE);

    auto* dst = (uint8_t*) malloc(jWidth * jHeight * 3 / 2);
    ret = codec_yuv_ConvertToI420((uint8_t*) bytes, (int) jWidth, (int) jHeight,
                                  (int) jRotation, (uint32_t) from,
                                  &dst);
    if (ret < 0) {
        env->ReleaseByteArrayElements(jSrc, bytes, 0);
        free((void*) dst);
        dst = nullptr;
        return jSrc;
    }

    if (JNI_TRUE == jMirror) {
        ret = codec_yuv_I420Mirror(dst, (int) jWidth, (int) jHeight, &dst);
        if (ret < 0) {
            env->ReleaseByteArrayElements(jSrc, bytes, 0);
            free((void*) dst);
            dst = nullptr;
            return jSrc;
        }
    }

    codec_yuv_ConvertFromI420(dst, (int) jWidth, (int) jHeight,
                              (int) jRotation, (uint32_t) to,
                              (uint8_t**) &bytes);
    env->ReleaseByteArrayElements(jSrc, bytes, 0);
    free((void*) dst);
    dst = nullptr;
    return jSrc;
}

jbyteArray codec_capabilities_toAndroidPixelFormat(JNIEnv* env, jobject thiz,
        jobject yBuffer, jint yStride, jobject uBuffer, jobject vBuffer, jint uvRowStride,
        jint uvPixelStride, jint width, jint height, jint to, jint jRotation, jboolean jMirror) {
    auto* const y_src =
            reinterpret_cast<uint8_t*>(env->GetDirectBufferAddress(yBuffer));
    auto* const u_src =
            reinterpret_cast<uint8_t*>(env->GetDirectBufferAddress(uBuffer));
    auto* const v_src =
            reinterpret_cast<uint8_t*>(env->GetDirectBufferAddress(vBuffer));

    const int y_plane_length = width * height;
    const int uv_plane_length = y_plane_length / 4;
    const int buffer_length = y_plane_length + uv_plane_length * 2;

    int ret = 0;

    auto* dst = (uint8_t*) malloc(buffer_length);
    ret = libyuv::Android420ToI420(y_src, yStride, u_src, uvRowStride, v_src,
                             uvRowStride, uvPixelStride, dst, width,
                             dst + y_plane_length, width / 2,
                             dst + y_plane_length + uv_plane_length,
                             width / 2, width, height);
    if (ret < 0) {
        free((void*) dst);
        dst = nullptr;
        return nullptr;
    }

    if (JNI_TRUE == jMirror) {
        ret = codec_yuv_I420Mirror(dst, (int) width, (int) height, &dst);
        if (ret < 0) {
            free((void*) dst);
            dst = nullptr;
            return nullptr;
        }
    }

    codec_yuv_ConvertFromI420(dst, (int) width, (int) height,
                              (int) jRotation, (uint32_t) to,
                              (uint8_t**) &dst);
    jbyteArray bytes = env->NewByteArray(buffer_length);
    env->SetByteArrayRegion(bytes, 0, buffer_length, (jbyte*) dst);
    free((void*) dst);
    dst = nullptr;
    return bytes;
}

static JNINativeMethod methods[] = {
        {"_toPixelFormat", "([BIIIIIZ)[B", (void*) codec_capabilities_toPixelFormat},
        {"_toPixelFormat", "(Ljava/nio/ByteBuffer;ILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;IIIIIIZ)[B", (void*) codec_capabilities_toAndroidPixelFormat},
};

int register_codec_capabilities(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_codec_capabilities(JNIEnv* env) {
}

MethodLoader codec_capabilities_loader = {register_codec_capabilities, unregister_codec_capabilities};
