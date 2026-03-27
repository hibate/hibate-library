LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LIBYUV_LIBS = $(LOCAL_PATH)/libyuv/libs/android/$(TARGET_ARCH_ABI)

include $(CLEAR_VARS)
LOCAL_MODULE := libyuv
LOCAL_SRC_FILES := $(LIBYUV_LIBS)/libyuv.a
include $(PREBUILT_STATIC_LIBRARY)
