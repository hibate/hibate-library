################################################################################

# 工程目录
set(MODULE_HOME ${CMAKE_CURRENT_SOURCE_DIR})

# 模块
set(LOCAL_MODULE media-jni)
# 模块目录
set(LOCAL_PATH ${MODULE_HOME})
# 头文件
set(INCLUDES
        ${LOCAL_PATH}
        ${PROJECT_HOME}/lang
        ${PROJECT_HOME}/utils
        ${FFMPEG_HOME}/include
        ${LOGGER_HOME}/jni/logger
)
set(EXPORT_INCLUDES ${INCLUDES})
# 源码
set(LIB_SRC
        onLoad.cpp
        codec/jni_av_codec.cpp
        codec/jni_av_codec_context.cpp
        codec/jni_av_codec_factory.cpp
        codec/jni_av_codec_id.cpp
        codec/jni_av_codec_parameters.cpp
        codec/jni_av_decoder.cpp
        codec/jni_av_encoder.cpp
        codec/jni_av_packet.cpp
        filter/jni_av_filter.cpp
        filter/jni_av_filter_context.cpp
        filter/jni_av_filter_factory.cpp
        filter/jni_av_filter_graph.cpp
        filter/jni_av_filter_in_out.cpp
        format/jni_av_format.cpp
        format/jni_av_format_context.cpp
        format/jni_av_input_format.cpp
        format/jni_av_input_stream.cpp
        format/jni_av_output_format.cpp
        format/jni_av_output_stream.cpp
        format/jni_av_stream.cpp
        util/jni_av_channel_layout.cpp
        util/jni_av_dictionary.cpp
        util/jni_av_frame.cpp
        util/jni_av_log.cpp
        util/jni_av_math.cpp
        util/jni_av_options.cpp
        util/jni_av_rational.cpp
        util/jni_av_util.cpp
)
set(LOCAL_SRC_FILES ${LIB_SRC})
# 库路径
set(LINK_LIBRARIES)
# 静态库
set(STATIC_LIBRARIES util lang)
# 动态库
set(SHARED_LIBRARIES logger avcodec avfilter avformat avutil swscale)
if (APP_SYSTEM STREQUAL "Android")
    list(APPEND SHARED_LIBRARIES log)
endif ()
# 宏
set(LOCAL_CFLAGS -D__STDC_CONSTANT_MACROS)
if (APP_SYSTEM STREQUAL "Windows")
    list(APPEND LOCAL_CFLAGS -DUNICODE)
endif ()
# 链接
set(LOCAL_LDFLAGS)
# 选项
set(LOCAL_OPTIONS)
if (APP_SYSTEM STREQUAL "Windows" AND MSVC)
    list(APPEND LOCAL_OPTIONS /utf-8)
endif ()

# 配置

add_library(${LOCAL_MODULE} SHARED ${LOCAL_SRC_FILES})
target_include_directories(${LOCAL_MODULE} PRIVATE ${INCLUDES})
target_include_directories(${LOCAL_MODULE} PUBLIC ${EXPORT_INCLUDES})
target_compile_definitions(${LOCAL_MODULE} PRIVATE ${LOCAL_CFLAGS})
target_compile_options(${LOCAL_MODULE} PRIVATE ${LOCAL_OPTIONS})
target_link_directories(${LOCAL_MODULE} PRIVATE ${LINK_LIBRARIES})
target_link_libraries(${LOCAL_MODULE} PRIVATE ${STATIC_LIBRARIES} ${SHARED_LIBRARIES})
target_link_options(${LOCAL_MODULE} PRIVATE ${LOCAL_LDFLAGS})

# 拷贝
add_custom_command(TARGET ${LOCAL_MODULE} POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy
        $<TARGET_FILE:${LOCAL_MODULE}>
        ${CMAKE_BINARY_DIR}
)
if (APP_SYSTEM STREQUAL "Linux" OR APP_SYSTEM STREQUAL "macOS")
    set(TARGET_OPTIONS "")
    if (APP_SYSTEM STREQUAL "macOS")
        set(TARGET_OPTIONS "-x")
    endif ()
    add_custom_command(TARGET ${LOCAL_MODULE} POST_BUILD
            COMMAND $<$<CONFIG:Release>:${CMAKE_STRIP}>
            $<$<CONFIG:Release>:${TARGET_OPTIONS}>
            $<$<CONFIG:Release>:${CMAKE_BINARY_DIR}/$<TARGET_FILE_NAME:${LOCAL_MODULE}>>
    )
endif ()
