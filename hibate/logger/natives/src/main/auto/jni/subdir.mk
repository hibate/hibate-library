################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# All of the sources participating in the build are defined here
-include ${LOCAL_HOME}/utils/subdir.mk
-include ${LOCAL_HOME}/logger/subdir.mk
-include ${LOCAL_HOME}/libjni/subdir.mk

INCLUDES = \
	-I$(JAVA_HOME)/include \
	-I$(JAVA_HOME)/include/linux \
	-I$(SOURCE_HOME)/utils \
	-I$(SOURCE_HOME)/logger

EXPORT_INCLUDES = $(INCLUDES)
EXPORT_LIBRARIES = \

CFLAGS += $(INCLUDES)
CFLAGS += $(EXPORT_LIBRARIES)
