################################################################################
# Automatically-generated file. Do not edit!
################################################################################

C_UPPER_SRCS := 
CXX_SRCS := 
C++_SRCS := 
OBJ_SRCS := 
CC_SRCS := 
ASM_SRCS := 
CPP_SRCS := 
C_SRCS := 
O_SRCS := 
S_UPPER_SRCS := 
LIBRARIES := 
CC_DEPS := 
C++_DEPS := 
C_UPPER_DEPS := 
CXX_DEPS := 
OBJS := 
CPP_DEPS := 
C_DEPS := 

LOCAL_PATH = .

LOCAL_HOME = $(LOCAL_PATH)/jni
SOURCE_HOME = $(LOCAL_PATH)/../jni
NATIVES_PATH = $(PROJECT_PATH)/../../hibate/logger/natives/src/main

CFLAGS += -fPIC -D__STDC_CONSTANT_MACROS

ifeq ($(DEBUG), 1)
    CFLAGS += -Wall -O0
    CFLAGS += -g3
else
    CFLAGS += -Wall -O3
endif
ifeq ($(BUILD_CROSS), 1)
    CFLAGS += -DWIN32
endif

# Every subdirectory with source files must be described here
SUBDIRS :=

