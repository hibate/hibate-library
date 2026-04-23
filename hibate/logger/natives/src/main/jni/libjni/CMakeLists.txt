################################################################################

# 工程目录
set(MODULE_HOME ${CMAKE_CURRENT_SOURCE_DIR})

# 模块
set(LOCAL_MODULE logger)
# 模块目录
set(LOCAL_PATH ${MODULE_HOME})
# 头文件
set(INCLUDES
        ${LOCAL_PATH}
        ${PROJECT_HOME}/logger
        ${PROJECT_HOME}/utils
)
set(EXPORT_INCLUDES ${INCLUDES})
# 源码
set(LIB_SRC
        onLoad.cpp
        jni_logger_context.cpp
        jni_logger_handler.cpp
)
set(LOCAL_SRC_FILES ${LIB_SRC})
# 库路径
set(LINK_LIBRARIES)
# 静态库
set(STATIC_LIBRARIES util)
if (APP_SYSTEM STREQUAL "Android")
    list(APPEND STATIC_LIBRARIES "-Wl,--whole-archive" slogger "-Wl,--no-whole-archive")
elseif (APP_SYSTEM STREQUAL "macOS")
    list(APPEND STATIC_LIBRARIES "-Wl,-force_load,$<TARGET_FILE:slogger>")
else ()
    list(APPEND STATIC_LIBRARIES slogger)
endif ()
# 动态库
set(SHARED_LIBRARIES)
if (APP_SYSTEM STREQUAL "Android")
    list(APPEND SHARED_LIBRARIES log)
endif ()
# 宏
set(LOCAL_CFLAGS)
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
