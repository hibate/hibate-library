#!/bin/bash
#
# Copyright (C) 2017 Hibate <ycaia86@126.com>
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#########################################################################
#
# 编译选项
#
#########################################################################

PRG="$0"

while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`/"$link"
    fi
done
PRG_DIR=`dirname "$PRG"`

# 当前 shell 执行路径
CURRENT_DIR=$(pwd)

# ffmpeg 源码目录
FFMPEG_SOURCE=$1

# 编译线程数
MAKE_FLAGS="-j$(getconf _NPROCESSORS_ONLN)"

# 编译路径
INSTALL_PATH="${PRG_DIR}/../jni/prebuilt/ffmpeg"

# 编译选项文件
OPTIONS_SOURCE="${PRG_DIR}/options.sh"

# 编译架构
APP_SYSTEMS="Android"
# Android
ANDROID_ABIES["Android"]="arm64-v8a armeabi-v7a riscv64 x86 x86_64"

#########################################################################
#
# Environment
#
#########################################################################
SYSTEM_NAME=$(uname -s)
ARCHITECTURE=$(uname -m)

# 系统: Linux、macOS
if [ "${SYSTEM_NAME}" == "Darwin" ]; then
    APP_SYSTEM="macOS"
elif [ "${SYSTEM_NAME}" == "Linux" ]; then
    APP_SYSTEM="Linux"
else
    echo "The script does not support ${SYSTEM_NAME} system."
    exit 0
fi

# 架构
#   Linux: arm64、x86_64、x86
#   macOS: arm64、x86_64
if [ "${APP_SYSTEM}" == "macOS" ]; then
    APP_ABI=${ARCHITECTURE}
elif [ "${APP_SYSTEM}" == "Linux" ]; then
    case "${ARCHITECTURE}" in
        x86_64|amd64)
            APP_ABI="x86_64"
            ;;
        i386|i686|x86)
            APP_ABI="x86"
            ;;
        aarch64|arm64)
            APP_ABI="arm64"
            ;;
        *)
            APP_ABI=${ARCHITECTURE}
            ;;
    esac
fi

# 分支: Release、Debug
if [ -z "${APP_OPTIM}" ]; then
    APP_OPTIM="Release"
fi

# 后缀
if [ "${APP_SYSTEM}" == "macOS" ]; then
    APP_SUFFIX="*.dylib"
elif [ "${APP_SYSTEM}" == "Linux" ]; then
    APP_SUFFIX="*.so"
fi

#########################################################################
#
# Checkout
#
#########################################################################

if [ -z "${FFMPEG_SOURCE}" ]; then
    FFMPEG_SOURCE=${CURRENT_DIR}
fi

# 检查编译环境
if [ -z "${FFMPEG_SOURCE}" ] || [ ! -f "${FFMPEG_SOURCE}/configure" ]; then
    echo "Please select ffmpeg source folder!"
    exit 1
fi
if [ ! -d "${FFMPEG_SOURCE}" ]; then
    echo "The ffmpeg source folder not exists!"
    exit 1
fi

#########################################################################
#
# Functions
#
#########################################################################

# 检查命令是否存在
# $1 : 命令
# 存在返回 true
# 不存在返回 false
function check_command_exists() {
    cmd_tmp=$1
    type ${cmd_tmp} >/dev/null 2>&1 || {
        unset cmd_tmp
        echo false
        return
    }
    unset cmd_tmp
    echo true
}

# 创建文件夹
# $1 : 路径
function file_mkdirs() {
    path_tmp=$1
    if [ ! -z "${path_tmp}" ] && [ ! -d "${path_tmp}" ]; then
        mkdir -p ${path_tmp}
    fi
    unset path_tmp
}

# 获取绝对路径
# $1 : 路径
function file_realpath() {
    path_tmp=$1
    if [ ! -z "${path_tmp}" ] && [ -d "${path_tmp}" ]; then
        cd ${path_tmp} > /dev/null 2>&1
        echo $(pwd)
        cd - > /dev/null 2>&1
    else
        echo ${path_tmp}
    fi
    unset path_tmp
}

#########################################################################
#
# Version
#
#########################################################################

# 版本号(主版本和子版本号)
if [ ! -z "${VERSION}" ]; then
    VERSION_TAG="${VERSION}"
    unset VERSION
fi
if [ -z "${VERSION_TAG}" ] && [ $(check_command_exists "git") = true ] && [ -d "${FFMPEG_SOURCE}/.git" ]; then
    cd ${FFMPEG_SOURCE} > /dev/null 2>&1
    VERSION_TAG=$(git describe --tags --abbrev=0 2>/dev/null)
    cd - > /dev/null 2>&1
fi
if [ ! -z "${VERSION_TAG}" ]; then
    VERSION_TAG=$(echo "$VERSION_TAG" | sed 's/^n//' | tr '-' '.' | cut -d. -f1,2)
    if [ ! -z "${VERSION_TAG}" ]; then
        VERSION=${VERSION_TAG}
    fi
    unset VERSION_TAG
fi

#########################################################################
#
# Setup options
#
#########################################################################

if [ -z "${NDK_PATH}" ] && [ ! -z "${ANDROID_NDK}" ]; then
    NDK_PATH=${ANDROID_NDK}
fi
if [ -z "${NDK_PATH}" ] && [ ! -z "${ANDROID_NDK_HOME}" ]; then
    NDK_PATH=${ANDROID_NDK_HOME}
fi
if [ -z "${NDK_PATH}" ] || [ ! -f "${NDK_PATH}/ndk-build" ]; then
    NDK_PATH=""
fi
# 编译平台环境
if [ "${APP_SYSTEM}" == "macOS" ]; then
    HOST_PLATFORM="darwin-x86_64"
elif [ "${APP_SYSTEM}" == "Linux" ]; then
    HOST_PLATFORM="linux-x86_64"
fi
if [ -z "${ANDROID_ABI}" ]; then
    ANDROID_ABI=21
fi
ANDROID_ABI_64BIT="$ANDROID_ABI"
if [ "${ANDROID_ABI_64BIT}" -lt 21 ]; then
    echo "Using ANDROID_ABI 21 for 64-bit architectures"
    ANDROID_ABI_64BIT=21
fi
ANDROID_ABI_RISCV64BIT="$ANDROID_ABI"
if [ "${ANDROID_ABI_RISCV64BIT}" -lt 35 ]; then
    ANDROID_ABI_RISCV64BIT=35
fi

# 获取 ffmpeg 源码绝对路径
FFMPEG_SOURCE=$(file_realpath ${FFMPEG_SOURCE})
# 获取安装目录绝对路径
if [ ! -z "${BUILD_PATH}" ]; then
    INSTALL_PATH=${BUILD_PATH}
fi
file_mkdirs ${INSTALL_PATH}
INSTALL_PATH=$(file_realpath ${INSTALL_PATH})

if [ -f "${OPTIONS_SOURCE}" ]; then
    . ${OPTIONS_SOURCE}
fi

COMMON_OPTIONS="
    --disable-static \
    --enable-shared \
    ${COMMON_OPTIONS} \
    " \

COMMON_CFLAGS="\
    -O3 -Wall -pipe \
    -std=c99 \
    -ffast-math \
    -fstrict-aliasing -Werror=strict-aliasing \
    -Wno-psabi -Wa,--noexecstack \
    -DNDEBUG \
    " \

COMMON_LDEXEFLAGS="\
    -pie \
    " \

#########################################################################
#
# Build
#
#########################################################################

# 默认架构
APP_SYSTEMS="${APP_SYSTEMS} ${APP_SYSTEM}"
if [ "${APP_SYSTEM}" == "Linux" ]; then
    LINUX_ABIES="${APP_ABI}"
elif [ "${APP_SYSTEM}" == "macOS" ]; then
    MACOS_ABIES="${APP_ABI}"
fi

# Android: all abies
# Linux: arm64、x86_64、x86
# macOS: arm64、x86_64
for name in ${APP_SYSTEMS[@]}; do
    APP_SYSTEM=$name
    eval APP_ABIES=\$$(echo "${APP_SYSTEM}" | tr '[:lower:]' '[:upper:]')_ABIES
    if [ -z "${APP_ABIES}" ]; then
        continue
    fi

    PREFIX="$(echo "${APP_SYSTEM}" | tr '[:upper:]' '[:lower:]')"
    for abi in ${APP_ABIES}; do
        APP_ABI=$abi
        TARGET_PATH="${PREFIX}/${APP_ABI}"

        ARCH=
        CPU=
        CROSS_PREFIX=
        SYSROOT=
        EXTRA_OPTIONS="${COMMON_OPTIONS}"
        EXTRA_CFLAGS="${COMMON_CFLAGS}"
        EXTRA_LDFLAGS=""
        EXTRA_LDEXEFLAGS="${COMMON_LDEXEFLAGS}"
        TOOLCHAIN_PREFIX=

        case ${abi} in
            arm64-v8a)
                if [ "${APP_SYSTEM}" == "Android" ] && [ ! -z "${NDK_PATH}" ]; then
                    ARCH=aarch64
                    CPU=armv8-a
                    CROSS_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/aarch64-linux-android${ANDROID_ABI_64BIT}-"
                    SYSROOT="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/sysroot"
                    TOOLCHAIN_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/llvm-"
                    EXTRA_OPTIONS="\
                        ${EXTRA_OPTIONS} \
                        --target-os=android \
                        --enable-asm \
                        "
                fi
                ;;
            armeabi-v7a)
                if [ "${APP_SYSTEM}" == "Android" ] && [ ! -z "${NDK_PATH}" ]; then
                    ARCH=arm
                    CPU=armv7-a
                    CROSS_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/armv7a-linux-androideabi${ANDROID_ABI}-"
                    SYSROOT="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/sysroot"
                    TOOLCHAIN_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/llvm-"
                    EXTRA_OPTIONS="\
                        ${EXTRA_OPTIONS} \
                        --target-os=android \
                        --enable-asm \
                        " \
                    EXTRA_CFLAGS="\
                        ${EXTRA_CFLAGS} \
                        -march=armv7-a \
                        -mfloat-abi=softfp \
                        " \
                    EXTRA_LDFLAGS="\
                        -Wl,--fix-cortex-a8 \
                        "
                    if [ ! -z "${VERSION}" ] && awk "BEGIN {exit !(${VERSION} <= 6.1)}"; then
                        EXTRA_OPTIONS="\
                            ${EXTRA_OPTIONS} \
                            --disable-vulkan \
                            "
                    fi
                fi
                ;;
            riscv64)
                if [ "${APP_SYSTEM}" == "Android" ] && [ ! -z "${NDK_PATH}" ]; then
                    ARCH=riscv64
                    CPU=generic
                    ANDROID_ABI="35"
                    CROSS_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/riscv64-linux-android${ANDROID_ABI_RISCV64BIT}-"
                    SYSROOT="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/sysroot"
                    TOOLCHAIN_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/llvm-"
                    EXTRA_OPTIONS="\
                        ${EXTRA_OPTIONS} \
                        --target-os=android \
                        "
                    if [ ! -z "${VERSION}" ] && awk "BEGIN {exit !(${VERSION} >= 8.0)}"; then
                        EXTRA_OPTIONS="\
                            ${EXTRA_OPTIONS} \
                            --enable-asm \
                            "
                    else
                        EXTRA_OPTIONS="\
                            ${EXTRA_OPTIONS} \
                            --disable-asm \
                            "
                    fi
                fi
                ;;
            x86)
                if [ "${APP_SYSTEM}" == "Android" ] && [ ! -z "${NDK_PATH}" ]; then
                    ARCH=x86
                    CPU=i686
                    CROSS_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/i686-linux-android${ANDROID_ABI}-"
                    SYSROOT="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/sysroot"
                    TOOLCHAIN_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/llvm-"
                    EXTRA_OPTIONS="\
                        ${EXTRA_OPTIONS} \
                        --target-os=android \
                        --disable-asm
                        "
                    if [ ! -z "${VERSION}" ] && awk "BEGIN {exit !(${VERSION} <= 6.1)}"; then
                        EXTRA_OPTIONS="\
                            ${EXTRA_OPTIONS} \
                            --disable-vulkan \
                            "
                    fi
                elif [ "${APP_SYSTEM}" != "Android" ]; then
                    ARCH=x86
                    CPU=i686
                    TOOLCHAIN_PREFIX="${CROSS_PREFIX}"
                    if [ $(check_command_exists "yasm") = true ] || [ $(check_command_exists "nasm") = true ]; then
                        EXTRA_OPTIONS="\
                            ${EXTRA_OPTIONS} \
                            --enable-asm \
                            "
                    else
                        EXTRA_OPTIONS="\
                            ${EXTRA_OPTIONS} \
                            --disable-asm
                            "
                    fi
                fi
                ;;
            x86_64)
                if [ "${APP_SYSTEM}" == "Android" ] && [ ! -z "${NDK_PATH}" ]; then
                    ARCH=x86_64
                    CPU=x86-64
                    CROSS_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/x86_64-linux-android${ANDROID_ABI_64BIT}-"
                    SYSROOT="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/sysroot"
                    TOOLCHAIN_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/llvm-"
                    EXTRA_OPTIONS="\
                        ${EXTRA_OPTIONS} \
                        --target-os=android \
                        "
                    if [ $(check_command_exists "yasm") = true ] || [ $(check_command_exists "nasm") = true ]; then
                        EXTRA_OPTIONS="\
                            ${EXTRA_OPTIONS} \
                            --enable-asm
                            "
                    else
                        EXTRA_OPTIONS="\
                            ${EXTRA_OPTIONS} \
                            --disable-asm
                            "
                    fi
                elif [ "${APP_SYSTEM}" != "Android" ]; then
                    ARCH=x86_64
                    CPU=x86-64
                    if [ $(check_command_exists "yasm") = true ] || [ $(check_command_exists "nasm") = true ]; then
                        EXTRA_OPTIONS="\
                            ${EXTRA_OPTIONS} \
                            --enable-asm \
                            "
                    else
                        EXTRA_OPTIONS="\
                            ${EXTRA_OPTIONS} \
                            --disable-asm
                            "
                    fi
                fi
                ;;
            arm64)
                if [ "${APP_SYSTEM}" != "Android" ]; then
                    ARCH=aarch64
                    CPU=armv8-a
                    EXTRA_OPTIONS="\
                        ${EXTRA_OPTIONS} \
                        --enable-asm \
                        "
                fi
                ;;
            *)
                ;;
        esac

        if [ ! -z "${ARCH}" ] && [ ! -z "${CPU}" ]; then
            if [ "${APP_SYSTEM}" == "Android" ]; then
                TOOLCHAIN_CC="${CROSS_PREFIX}clang"
            else
                TOOLCHAIN_CC="${TOOLCHAIN_PREFIX}gcc"
            fi
            if [ -f "${TOOLCHAIN_CC}" ] || [ $(check_command_exists "${TOOLCHAIN_CC}") = true ]; then
                if [ $(check_command_exists "ccache") = true ]; then
                    EXTRA_OPTIONS="${EXTRA_OPTIONS} --cc='ccache ${TOOLCHAIN_CC}'"
                else
                    EXTRA_OPTIONS="${EXTRA_OPTIONS} --cc=${TOOLCHAIN_CC}"
                fi
            fi
            if [ ! -z "${TOOLCHAIN_PREFIX}" ]; then
                TOOLCHAIN_NM="${TOOLCHAIN_PREFIX}nm"
                TOOLCHAIN_AR="${TOOLCHAIN_PREFIX}ar"
                TOOLCHAIN_RANLIB="${TOOLCHAIN_PREFIX}ranlib"
                TOOLCHAIN_STRIP="${TOOLCHAIN_PREFIX}strip"
            fi

            echo "#########################################################################"
            echo ""
            echo "Building ffmpeg for: ${APP_SYSTEM} ${APP_ABI}"
            echo ""
            echo "#########################################################################"
            echo ""

            if [ -d "${FFMPEG_SOURCE}/${TARGET_PATH}" ] || [ -L "${FFMPEG_SOURCE}/${TARGET_PATH}" ]; then
                rm -rf ${FFMPEG_SOURCE}/${TARGET_PATH}
            fi
            file_mkdirs ${FFMPEG_SOURCE}/${TARGET_PATH}

            cd ${FFMPEG_SOURCE} > /dev/null 2>&1
            ./configure \
                --prefix="${TARGET_PATH}" \
                --arch=${ARCH} \
                --cpu=${CPU} \
                --cross-prefix="${CROSS_PREFIX}" \
                --nm="${TOOLCHAIN_NM}" \
                --ar="${TOOLCHAIN_AR}" \
                --ranlib="${TOOLCHAIN_RANLIB}" \
                --strip="${TOOLCHAIN_STRIP}" \
                --sysroot="${SYSROOT}" \
                --extra-cflags="${EXTRA_CFLAGS}" \
                --extra-ldflags="${EXTRA_LDFLAGS}" \
                --extra-ldexeflags="${EXTRA_LDEXEFLAGS}" \
                ${EXTRA_OPTIONS}

            [ $PIPESTATUS == 0 ] || exit 1

            cp config.* ${TARGET_PATH}

            make clean
            make ${MAKE_FLAGS} || exit 1
            make install || exit 1

            if [ -d "${TARGET_PATH}/include" ]; then
                cp config.* ${TARGET_PATH}/include
            fi
            if [ -d "${TARGET_PATH}" ]; then
                if [ -d "${INSTALL_PATH}/${TARGET_PATH}" ]; then
                    rm -rf ${INSTALL_PATH}/${TARGET_PATH}
                fi
                file_mkdirs ${INSTALL_PATH}/${TARGET_PATH}
                cp -rf ${TARGET_PATH}/* ${INSTALL_PATH}/${TARGET_PATH}
                rm -rf ${TARGET_PATH}
            fi
        fi
    done

    if [ -d "${PREFIX}" ]; then
        rm -rf ${PREFIX}
    fi
done

exit 0
