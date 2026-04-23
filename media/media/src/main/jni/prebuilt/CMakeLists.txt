################################################################################

# 工程目录
set(MODULE_HOME ${CMAKE_CURRENT_SOURCE_DIR})

if (APP_SYSTEM STREQUAL "Android")
    set(TARGET_PATH ${ANDROID_ABI})
else ()
    set(TARGET_PATH "${APP_SYSTEM}/${APP_ABI}")
    string(TOLOWER "${TARGET_PATH}" TARGET_PATH)
endif ()

# 日志库
find_library(LOGGER_PATH
        NAMES logger
        PATHS "${LOGGER_HOME}/libs/${TARGET_PATH}"
        NO_DEFAULT_PATH # 指定目录查找
)
add_library(logger SHARED IMPORTED GLOBAL)
if (APP_SYSTEM STREQUAL "Windows")
    string(REPLACE ".lib" ".dll" LOGGER_DLL_PATH "${LOGGER_PATH}")
    set_target_properties(logger PROPERTIES
            IMPORTED_IMPLIB "${LOGGER_PATH}"
            IMPORTED_LOCATION "${LOGGER_DLL_PATH}"
    )
else ()
    set_target_properties(logger PROPERTIES
            IMPORTED_LOCATION "${LOGGER_PATH}"
    )
endif ()

# FFMPEG
set(FFMPEG_LIBRARIES avcodec avfilter avformat avutil swscale)
foreach (MODULE ${FFMPEG_LIBRARIES})
    find_library(${MODULE}_PATH
            NAMES ${MODULE}
            PATHS "${FFMPEG_HOME}/libs/${TARGET_PATH}"
            NO_DEFAULT_PATH
    )
    add_library(${MODULE} SHARED IMPORTED GLOBAL)
    if (APP_SYSTEM STREQUAL "Windows")
        file(GLOB ${MODULE}_DLL_PATH "${FFMPEG_HOME}/libs/${TARGET_PATH}/${MODULE}-*.dll")
        list(GET ${MODULE}_DLL_PATH 0 ${MODULE}_DLL_PATH)
        set_target_properties(${MODULE} PROPERTIES
                IMPORTED_IMPLIB "${${MODULE}_PATH}"
                IMPORTED_LOCATION "${${MODULE}_DLL_PATH}"
        )
    else ()
        set_target_properties(${MODULE} PROPERTIES
                IMPORTED_LOCATION "${${MODULE}_PATH}"
        )
    endif ()
endforeach ()
