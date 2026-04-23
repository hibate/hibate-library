################################################################################

# 工程目录
set(MODULE_HOME ${CMAKE_CURRENT_SOURCE_DIR})

# 模块
set(LOCAL_MODULE util)
# 模块目录
set(LOCAL_PATH ${MODULE_HOME})
# 头文件
set(INCLUDES
        ${LOCAL_PATH}
)
set(EXPORT_INCLUDES ${INCLUDES})
# 源码
set(LIB_SRC
        jni_util.cpp
)
set(LOCAL_SRC_FILES ${LIB_SRC})
# 库路径
set(LINK_LIBRARIES)
# 静态库
set(STATIC_LIBRARIES)
# 动态库
set(SHARED_LIBRARIES)
# 宏
set(LOCAL_CFLAGS)
if (APP_SYSTEM STREQUAL "Windows")
    list(APPEND LOCAL_CFLAGS -DUNICODE)
endif ()
if (APP_SYSTEM STREQUAL "Android")
    list(APPEND LOCAL_CFLAGS -DANDROID)
endif ()
# 链接
set(LOCAL_LDFLAGS)
# 选项
set(LOCAL_OPTIONS)
if (APP_SYSTEM STREQUAL "Windows" AND MSVC)
    list(APPEND LOCAL_OPTIONS /utf-8)
endif ()

# 配置

add_library(${LOCAL_MODULE} STATIC ${LOCAL_SRC_FILES})
target_include_directories(${LOCAL_MODULE} PRIVATE ${INCLUDES})
target_include_directories(${LOCAL_MODULE} PUBLIC ${EXPORT_INCLUDES})
target_compile_definitions(${LOCAL_MODULE} PRIVATE ${LOCAL_CFLAGS})
target_compile_options(${LOCAL_MODULE} PRIVATE ${LOCAL_OPTIONS})
target_link_directories(${LOCAL_MODULE} PRIVATE ${LINK_LIBRARIES})
target_link_libraries(${LOCAL_MODULE} PRIVATE ${STATIC_LIBRARIES} ${SHARED_LIBRARIES})
target_link_options(${LOCAL_MODULE} PRIVATE ${LOCAL_LDFLAGS})
