LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LIBS_SLF_LOGGER = \
    logger_adapter.cpp \
    logger_context.cpp \
    logger_core.cpp \
    logger_layout.cpp \
    lock.cpp \
    logger.c

SRC = \
    $(LIBS_SLF_LOGGER)

INCLUDES = \
    $(LOCAL_PATH) \

EXPORT_INCLUDES = $(INCLUDES)

STATIC_LIBRARIES =

SHARED_LIBRARIES =

LOCAL_MODULE    := libslogger
LOCAL_SRC_FILES := $(SRC)
LOCAL_C_INCLUDES := $(INCLUDES)
LOCAL_EXPORT_C_INCLUDES := $(EXPORT_INCLUDES)
LOCAL_STATIC_LIBRARIES := $(STATIC_LIBRARIES)
LOCAL_SHARED_LIBRARIES := $(SHARED_LIBRARIES)

LOCAL_CFLAGS += -DLIB_IMPLEMENTATION
ifeq ($(APP_OPTIM), debug)
LOCAL_CFLAGS += -DDEBUG
endif

include $(BUILD_STATIC_LIBRARY)