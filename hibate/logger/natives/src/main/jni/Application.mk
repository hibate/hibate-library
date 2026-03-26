# Uncomment this if you're using STL in your project
# See CPLUSPLUS-SUPPORT.html in the NDK documentation for more information

ifeq (,$(PROJECT_PATH))

# 项目路径(绝对路径): jni 上一级目录路径(使用 AS 则为 module 路径)
PROJECT_PATH := $(call find-project-dir, ., .)

else

# PROJECT_PATH 由 build.bat 脚本传递: jni 目录

endif

APP_STL := c++_static

APP_OPTIM := release
APP_ABI := all
