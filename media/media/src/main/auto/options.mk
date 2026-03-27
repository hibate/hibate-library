################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# debug
ifeq ($(debug), 1)
    DEBUG = 1
else ifeq ($(DEBUG), 1)
    DEBUG = 1
else
    DEBUG = 0
endif

# cross
ifeq ($(CROSS_TOOLCHAINS), )
    BUILD_CROSS = 0
else ifeq ($(CROSS_TOOLCHAINS), "")
    BUILD_CROSS = 0
else
    BUILD_CROSS = 1
endif

ifeq ($(BUILD_CROSS), 1)
    BUILD_PLATFORM = "windows"
else
    BUILD_PLATFORM = "linux"
endif
