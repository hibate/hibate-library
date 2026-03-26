LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LIBS_SRC = \
    jni_util.cpp

SRC = \
    $(LIBS_SRC)

INCLUDES = \
    $(LOCAL_PATH)

EXPORT_INCLUDES = \
    $(LOCAL_PATH)

LOCAL_MODULE    := libjniutil
LOCAL_SRC_FILES := $(SRC)
LOCAL_C_INCLUDES := $(INCLUDES)
LOCAL_EXPORT_C_INCLUDES := $(EXPORT_INCLUDES)

LOCAL_CFLAGS += -DANDROID

include $(BUILD_STATIC_LIBRARY)