#!/bin/bash

#########################################################################
#
# 编译选项
#
#########################################################################

PRG="$0"

# 保证文件路径不是链接符号，循环查找直到找到原件原地址
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

# 设置 APPLICATION_HOME 变量
[ -z "${APPLICATION_HOME}" ] && APPLICATION_HOME=`cd "$PRG_DIR" > /dev/null; pwd`

# 当前 shell 执行路径
CURRENT_DIR=$(pwd)

MAKE_FLAGS="-j$(getconf _NPROCESSORS_ONLN)"

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

# 判断文件/文件夹/某类型文件是否存在
# $1 : 路径
# 符合条件返回 true
# 不符合条件返回 false
# 例如: ~/*.sh
function file_exists() {
    filename=$1
    if [ -z "${filename}" ]; then
        echo false
    elif [ -f "${filename}" ]; then
        echo true
    elif [ -d "${filename}" ]; then
        echo true
    elif [ `ls -1 ${filename} 2>/dev/null | wc -l` != 0 ]; then
        echo true
    else
        echo false
    fi

    unset filename
}

#########################################################################
#
# Build
#
#########################################################################

TARGET_PATH="$(echo "${APP_SYSTEM}" | tr '[:upper:]' '[:lower:]')/${APP_ABI}"
# 编译目录
CMAKE_PATH="${CURRENT_DIR}/cmake-build-$(echo "${APP_OPTIM}" | tr '[:upper:]' '[:lower:]')"
# 库目录
LIBS_PATH="${CURRENT_DIR}/libs"
# 源码目录
SOURCE_PATH="${APPLICATION_HOME}/jni"

if [ $(check_command_exists "cmake") = true ]; then
    echo "#########################################################################"
    echo ""
    echo "Building library for: ${APP_SYSTEM} ${APP_ABI}"
    echo ""
    echo "#########################################################################"
    echo ""

    file_mkdirs ${CMAKE_PATH}/${TARGET_PATH}
    cd ${CMAKE_PATH}/${TARGET_PATH} > /dev/null 2>&1
    cmake ${SOURCE_PATH} -DCMAKE_BUILD_TYPE=${APP_OPTIM}
    make ${MAKE_FLAGS}

    status=$PIPESTATUS

    if [ $(file_exists "${CMAKE_PATH}/${TARGET_PATH}/${APP_SUFFIX}") = true ]; then
        file_mkdirs ${LIBS_PATH}/${TARGET_PATH}
        cp ${CMAKE_PATH}/${TARGET_PATH}/${APP_SUFFIX} ${LIBS_PATH}/${TARGET_PATH}
    fi

    cd - > /dev/null 2>&1
    [ $status == 0 ] || exit 1
fi

exit 0