################################################################################
# Automatically-generated file. Do not edit!
################################################################################

USER_OBJS :=

ifeq ($(BUILD_CROSS), 1)
    LIBS := -lpthread -llogger -lavcodec-62 -lavfilter-11 -lavformat-62 -lavutil-60 -lswscale-9
else
    LIBS := -llogger -lavcodec -lavfilter -lavformat -lavutil -lswscale
endif

