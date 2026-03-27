################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# All of the sources participating in the build are defined here
-include ${LOCAL_HOME}/lang/subdir.mk
-include ${LOCAL_HOME}/libjni/subdir.mk
-include ${LOCAL_HOME}/utils/subdir.mk

INCLUDES = \
	-I$(JAVA_HOME)/include \
	-I$(JAVA_HOME)/include/linux \
	-I$(NATIVES_PATH)/jni/logger \
	-I$(SOURCE_HOME)/prebuilt/ffmpeg/include \
	-I$(SOURCE_HOME)/lang \
	-I$(SOURCE_HOME)/libjni \
	-I$(SOURCE_HOME)/utils

EXPORT_INCLUDES = $(INCLUDES)
EXPORT_LIBRARIES = \
	-L"$(NATIVES_PATH)/libs/$(BUILD_PLATFORM)/$(LIB_ARCH)" \
	-L"$(SOURCE_HOME)/prebuilt/ffmpeg/libs/$(BUILD_PLATFORM)/$(LIB_ARCH)"

CFLAGS += $(INCLUDES)
CFLAGS += $(EXPORT_LIBRARIES)
