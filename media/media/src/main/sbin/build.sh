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
PRGDIR=`dirname "$PRG"`

# 当前 shell 执行路径
CURRENT_DIR=$(pwd)

# ffmpeg 源码目录
FFMPEG_SOURCE=$1

# make 参数
MAKE_FLAGS="-j4"

# 编译路径
INSTALL_DST="${PRGDIR}/../jni/prebuilt/ffmpeg"

# 编译选项文件
OPTIONS_SOURCE="${PRGDIR}/options.sh"

# 选择编译的 abi 类型
APP_ABI=(
    "arm64-v8a"
    "armeabi-v7a"
    "riscv64"
    "x86"
    "x86_64"
    "x86-64"
)

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

file_mkdirs() {
    path_tmp=$1
    if [ ! -z "${path_tmp}" ] && [ ! -d "${path_tmp}" ]; then
        mkdir -p ${path_tmp}
    fi
    unset path_tmp
}

file_realpath() {
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

check_command_exists() {
    cmd_tmp=$1
    type ${cmd_tmp} >/dev/null 2>&1 || {
        unset cmd_tmp
        echo false
        return
    }
    unset cmd_tmp
    echo true
}

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
# 编译平台环境("darwin-x86_64" for Mac OS X)
if [ -z "${HOST_PLATFORM}" ]; then
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

# 获取 ffmpeg 源码绝对路径
FFMPEG_SOURCE=$(file_realpath ${FFMPEG_SOURCE})
# 获取安装目录绝对路径
if [ -d "${INSTALL_DST}" ] || [ -L "${INSTALL_DST}" ]; then
    rm -rf ${INSTALL_DST}
fi
file_mkdirs ${INSTALL_DST}
INSTALL_DST=$(file_realpath ${INSTALL_DST})

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

for abi in ${APP_ABI[@]}
do
    PREFIX="libs"

    ARCH=
    CPU=
    CROSS_PREFIX=
    SYSROOT=
    EXTRA_OPTIONS="${COMMON_OPTIONS}"
    EXTRA_CFLAGS="${COMMON_CFLAGS}"
    EXTRA_LDFLAGS=""
    EXTRA_LDEXEFLAGS="${COMMON_LDEXEFLAGS}"
    TOOLCHAIN_PREFIX=
    TOOLCHAIN_PLATFORM=

    case ${abi} in
        arm64-v8a)
            if [ ! -z "${NDK_PATH}" ]; then
                ARCH=aarch64
                CPU=armv8-a
                CROSS_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/aarch64-linux-android${ANDROID_ABI}-"
                SYSROOT="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/sysroot"
                TOOLCHAIN_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/llvm-"
                TOOLCHAIN_PLATFORM="android/arm64-v8a"
                EXTRA_OPTIONS="\
                    ${EXTRA_OPTIONS} \
                    --target-os=android \
                    --enable-asm \
                    "
            fi
            ;;
        armeabi-v7a)
            if [ ! -z "${NDK_PATH}" ]; then
                ARCH=arm
                CPU=armv7-a
                CROSS_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/armv7a-linux-androideabi${ANDROID_ABI}-"
                SYSROOT="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/sysroot"
                TOOLCHAIN_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/llvm-"
                TOOLCHAIN_PLATFORM="android/armeabi-v7a"
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
            fi
            ;;
        riscv64)
            if [ ! -z "${NDK_PATH}" ]; then
                ARCH=riscv64
                CPU=generic
                ANDROID_ABI="35"
                CROSS_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/riscv64-linux-android${ANDROID_ABI}-"
                SYSROOT="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/sysroot"
                TOOLCHAIN_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/llvm-"
                TOOLCHAIN_PLATFORM="android/riscv64"
                EXTRA_OPTIONS="\
                    ${EXTRA_OPTIONS} \
                    --target-os=android \
                    --enable-asm \
                    "
            fi
            ;;
        x86)
            if [ ! -z "${NDK_PATH}" ]; then
                ARCH=x86
                CPU=i686
                CROSS_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/i686-linux-android${ANDROID_ABI}-"
                SYSROOT="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/sysroot"
                TOOLCHAIN_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/llvm-"
                TOOLCHAIN_PLATFORM="android/x86"
                EXTRA_OPTIONS="\
                    ${EXTRA_OPTIONS} \
                    --target-os=android \
                    --disable-asm \
                    "
            fi
            ;;
        x86_64)
            if [ ! -z "${NDK_PATH}" ]; then
                ARCH=x86_64
                CPU=x86-64
                CROSS_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/x86_64-linux-android${ANDROID_ABI}-"
                SYSROOT="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/sysroot"
                TOOLCHAIN_PREFIX="${NDK_PATH}/toolchains/llvm/prebuilt/${HOST_PLATFORM}/bin/llvm-"
                TOOLCHAIN_PLATFORM="android/x86_64"
                EXTRA_OPTIONS="\
                    ${EXTRA_OPTIONS} \
                    --target-os=android \
                    --disable-asm \
                    "
            fi
            ;;
        x86-64)
            ARCH=x86_64
            CPU=x86-64
            TOOLCHAIN_PREFIX="${CROSS_PREFIX}"
            TOOLCHAIN_PLATFORM="linux/x64"
            if [ -f "/usr/bin/yasm" ] || [ -f "/usr/bin/nasm" ]; then
                EXTRA_OPTIONS="\
                    ${EXTRA_OPTIONS} \
                    --enable-x86asm \
                    "
            fi
            ;;
        *)
            ;;
    esac

    if [ ! -z "${ARCH}" ] && [ ! -z "${CPU}" ]; then
        TOOLCHAIN_CC="${TOOLCHAIN_PREFIX}gcc"
        if [ -f "${TOOLCHAIN_CC}" ] || [ $(check_command_exists "${TOOLCHAIN_CC}") = true ]; then
            if [ $(check_command_exists "ccache") = true ]; then
                TOOLCHAIN_CC="ccache ${TOOLCHAIN_CC}"
                EXTRA_OPTIONS="${EXTRA_OPTIONS} --cc='${TOOLCHAIN_CC}'"
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
        echo "Building ffmpeg for: ${abi}"
        echo ""
        echo "#########################################################################"
        echo ""

        if [ -d "${FFMPEG_SOURCE}/${PREFIX}" ] || [ -L "${FFMPEG_SOURCE}/${PREFIX}" ]; then
            rm -rf ${FFMPEG_SOURCE}/${PREFIX}
        fi
        file_mkdirs ${FFMPEG_SOURCE}/${PREFIX}

        cd ${FFMPEG_SOURCE} > /dev/null 2>&1
        ./configure \
            --prefix="${PREFIX}" \
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
            ${EXTRA_OPTIONS} \
            &&

        [ $PIPESTATUS == 0 ] || exit 1

        cp config.* ${PREFIX}

        make clean
        make ${MAKE_FLAGS} || exit 1
        make install || exit 1

        if [ -d "${PREFIX}/include" ]; then
            cp config.* ${PREFIX}/include
        fi
        if [ -d "${PREFIX}/lib" ]; then
            PREFIX_TMP="${PREFIX}/tmp"
            mv ${PREFIX}/lib ${PREFIX_TMP}
            file_mkdirs ${PREFIX}/libs/${TOOLCHAIN_PLATFORM}
            mv ${PREFIX_TMP}/* ${PREFIX}/libs/${TOOLCHAIN_PLATFORM}
            rm -rf ${PREFIX_TMP}
        fi
        if [ -d "${PREFIX}" ]; then
            cp -rf ${PREFIX}/* ${INSTALL_DST}
            rm -rf ${PREFIX}
        fi
    fi
done

exit 0
