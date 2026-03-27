LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LIBS_JNI = \
    onLoad.cpp \
    codec/jni_codec_capabilities.cpp

SRC = \
    $(LIBS_JNI)

INCLUDES = \
    $(LOCAL_PATH) \
    $(LOCAL_PATH)/../utils \
    $(LOCAL_PATH)/../prebuilt/libyuv/include \

EXPORT_INCLUDES = $(INCLUDES)

STATIC_LIBRARIES = \
    jniutil \
    yuv

SHARED_LIBRARIES =

LOCAL_MODULE    := libyuv-jni
LOCAL_SRC_FILES := $(SRC)
LOCAL_C_INCLUDES := $(INCLUDES)
LOCAL_EXPORT_C_INCLUDES := $(EXPORT_INCLUDES)
LOCAL_STATIC_LIBRARIES := $(STATIC_LIBRARIES)
LOCAL_SHARED_LIBRARIES := $(SHARED_LIBRARIES)

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)