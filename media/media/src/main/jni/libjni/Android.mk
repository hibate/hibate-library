LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LIBS_JNI = \
    onLoad.cpp \
    codec/jni_av_codec.cpp \
    codec/jni_av_codec_context.cpp \
    codec/jni_av_codec_factory.cpp \
    codec/jni_av_codec_id.cpp \
    codec/jni_av_codec_parameters.cpp \
    codec/jni_av_decoder.cpp \
    codec/jni_av_encoder.cpp \
    codec/jni_av_packet.cpp \
    filter/jni_av_filter.cpp \
    filter/jni_av_filter_context.cpp \
    filter/jni_av_filter_factory.cpp \
    filter/jni_av_filter_graph.cpp \
    filter/jni_av_filter_in_out.cpp \
    format/jni_av_format.cpp \
    format/jni_av_format_context.cpp \
    format/jni_av_input_format.cpp \
    format/jni_av_input_stream.cpp \
    format/jni_av_output_format.cpp \
    format/jni_av_output_stream.cpp \
    format/jni_av_stream.cpp \
    util/jni_av_channel_layout.cpp \
    util/jni_av_dictionary.cpp \
    util/jni_av_frame.cpp \
    util/jni_av_log.cpp \
    util/jni_av_math.cpp \
    util/jni_av_options.cpp \
    util/jni_av_rational.cpp \
    util/jni_av_util.cpp

SRC = \
    $(LIBS_JNI)

INCLUDES = \
    $(LOCAL_PATH) \
    $(LOCAL_PATH)/../lang \
    $(LOCAL_PATH)/../utils \
    $(LOCAL_PATH)/../prebuilt/ffmpeg/include \
    $(NATIVES_PATH)/jni/logger

EXPORT_INCLUDES = $(INCLUDES)

STATIC_LIBRARIES = \
    jniutil \
    lang

SHARED_LIBRARIES = \
    logger \
    avcodec \
    avfilter \
    avformat \
    avutil \
    swscale

LOCAL_MODULE    := libmedia-jni
LOCAL_SRC_FILES := $(SRC)
LOCAL_C_INCLUDES := $(INCLUDES)
LOCAL_EXPORT_C_INCLUDES := $(EXPORT_INCLUDES)
LOCAL_STATIC_LIBRARIES := $(STATIC_LIBRARIES)
LOCAL_SHARED_LIBRARIES := $(SHARED_LIBRARIES)

LOCAL_CFLAGS += -D__STDC_CONSTANT_MACROS
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)