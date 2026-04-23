################################################################################

# 系统: Windows、Linux、macOS、Android
if (CMAKE_SYSTEM_NAME STREQUAL "Windows")
    set(APP_SYSTEM "Windows")
elseif (CMAKE_SYSTEM_NAME STREQUAL "Linux")
    set(APP_SYSTEM "Linux")
elseif (CMAKE_SYSTEM_NAME STREQUAL "Darwin")
    set(APP_SYSTEM "macOS")
elseif (CMAKE_SYSTEM_NAME STREQUAL "Android")
    set(APP_SYSTEM "Android")
else ()
    set(APP_SYSTEM "Unknown")
endif ()

# 架构
#   Windows: arm64、x64、x86
#   Linux: arm64、x86_64、x86
#   macOS: arm64、x86_64
#   Android: arm64-v8a、armeabi-v7a、riscv64、x86、x86_64
if (APP_SYSTEM STREQUAL "Android")
    set(APP_ABI ${ANDROID_ABI})
elseif (APP_SYSTEM STREQUAL "macOS")
    if (CMAKE_OSX_ARCHITECTURES)
        set(APP_ABI ${CMAKE_OSX_ARCHITECTURES})
    else ()
        set(APP_ABI ${CMAKE_SYSTEM_PROCESSOR})
    endif ()
else ()
    string(TOLOWER "${CMAKE_SYSTEM_PROCESSOR}" _PROCESSOR_)
    if (_PROCESSOR_ MATCHES "x86_64|amd64")
        if (APP_SYSTEM STREQUAL "Windows")
            set(APP_ABI "x64")
        else ()
            set(APP_ABI "x86_64")
        endif ()
    elseif (_PROCESSOR_ MATCHES "i386|i686|x86")
        set(APP_ABI "x86")
    elseif (_PROCESSOR_ MATCHES "aarch64|arm64")
        set(APP_ABI "arm64")
    else ()
        set(APP_ABI ${_PROCESSOR_})
    endif ()
endif ()

# 分支: Release、Debug
set(APP_OPTIM ${CMAKE_BUILD_TYPE})

# Java
if(APP_SYSTEM STREQUAL "Android")
    # Android 环境
else ()
    if (DEFINED ENV{JAVA_HOME} AND NOT "$ENV{JAVA_HOME}" STREQUAL "")
        set(JAVA_HOME $ENV{JAVA_HOME})
        include_directories(${JAVA_HOME}/include)
        if (APP_SYSTEM STREQUAL "Windows")
            include_directories(${JAVA_HOME}/include/win32)
        elseif (APP_SYSTEM STREQUAL "Linux")
            include_directories(${JAVA_HOME}/include/linux)
        elseif (APP_SYSTEM STREQUAL "macOS")
            include_directories(${JAVA_HOME}/include/darwin)
        endif ()
    endif ()
endif ()
