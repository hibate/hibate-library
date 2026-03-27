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
PRGDIR=`dirname "$PRG"`

# 设置 APPLICATION_HOME 变量
[ -z "${APPLICATION_HOME}" ] && APPLICATION_HOME=`cd "$PRGDIR" >/dev/null; pwd`

# 当前 shell 执行路径
CURRENT_DIR=$(pwd)

# 选择编译的 abi 类型
APP_ABI=(
    "x86_64"
    "win64"
)

#########################################################################
#
# Checkout
#
#########################################################################

[ -z "${PROJECT_PATH}" ] && PROJECT_PATH=`cd "$PRGDIR/../.." >/dev/null; pwd`

MAKE_PATH="${APPLICATION_HOME}/auto"
LIBS_PATH="${CURRENT_DIR}/libs"

if [ ! -d "${MAKE_PATH}" ] || [ ! -f "${MAKE_PATH}/makefile" ]; then
  echo "Makefile not exists!"
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

# 创建文件夹
# $1 : 路径
file_mkdirs() {
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
file_exists() {
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

for abi in ${APP_ABI[@]}
do
    CROSS_PREFIX=""
    LIB_ARCH=""
    LIB_PATH=""
    LIB_MODULES=""

    case ${abi} in
        x86_64)
            LIB_ARCH="x64"
            LIB_PATH="linux/x64"
            LIB_MODULES="*.so"
            ;;
        win64)
            CROSS_PREFIX="x86_64-w64-mingw32-"
            LIB_ARCH="x64"
            LIB_PATH="windows/x64"
            LIB_MODULES="*.dll"
            ;;
    esac

    if [ $(check_command_exists "${CROSS_PREFIX}gcc") = true ]; then
        echo "#########################################################################"
        echo ""
        echo "Building libs for: ${abi}"
        echo ""
        echo "#########################################################################"
        echo ""

        cd ${MAKE_PATH} > /dev/null 2>&1
        export PROJECT_PATH LIB_ARCH
        export CROSS_TOOLCHAINS="${CROSS_PREFIX}"
        make

        status=$PIPESTATUS

        if [ $(file_exists "${MAKE_PATH}/${LIB_MODULES}") = true ]; then
            file_mkdirs ${LIBS_PATH}/${LIB_PATH}
            cp ${MAKE_PATH}/${LIB_MODULES} ${LIBS_PATH}/${LIB_PATH}
        fi

        make clean
        cd - > /dev/null 2>&1

        [ $status == 0 ] || exit 1
    fi
done

exit 0