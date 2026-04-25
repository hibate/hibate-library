LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

NATIVES_LIBS = $(NATIVES_PATH)/libs/$(TARGET_ARCH_ABI)
FFMPGE_LIBS = $(LOCAL_PATH)/ffmpeg/android/$(TARGET_ARCH_ABI)/lib

include $(CLEAR_VARS)
LOCAL_MODULE := liblogger
LOCAL_SRC_FILES := $(NATIVES_LIBS)/liblogger.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavcodec
LOCAL_SRC_FILES := $(FFMPGE_LIBS)/libavcodec.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavfilter
LOCAL_SRC_FILES := $(FFMPGE_LIBS)/libavfilter.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavformat
LOCAL_SRC_FILES := $(FFMPGE_LIBS)/libavformat.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavutil
LOCAL_SRC_FILES := $(FFMPGE_LIBS)/libavutil.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libswscale
LOCAL_SRC_FILES := $(FFMPGE_LIBS)/libswscale.so
include $(PREBUILT_SHARED_LIBRARY)
