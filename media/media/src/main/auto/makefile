################################################################################
# Automatically-generated file. Do not edit!
################################################################################

-include ../makefile.init

TARGET_NAME = libmedia-jni

CC = $(CROSS_TOOLCHAINS)gcc
GG = $(CROSS_TOOLCHAINS)g++
STRIP=$(CROSS_TOOLCHAINS)strip
RM := rm -rf

# All of the sources participating in the build are defined here
-include options.mk
-include sources.mk
-include jni/subdir.mk
-include subdir.mk
-include objects.mk

# Auto Configuration
ifeq ($(BUILD_CROSS), 0)
TARGET=$(TARGET_NAME).so
else
TARGET=$(TARGET_NAME).dll
endif
STRIPPED_TARGET=stripped-$(TARGET)

ifneq ($(MAKECMDGOALS),clean)
ifneq ($($(STRIP) $(CC_DEPS)),)
-include $(CC_DEPS)
endif
ifneq ($($(STRIP) $(C++_DEPS)),)
-include $(C++_DEPS)
endif
ifneq ($($(STRIP) $(C_UPPER_DEPS)),)
-include $(C_UPPER_DEPS)
endif
ifneq ($($(STRIP) $(CXX_DEPS)),)
-include $(CXX_DEPS)
endif
ifneq ($($(STRIP) $(CPP_DEPS)),)
-include $(CPP_DEPS)
endif
ifneq ($($(STRIP) $(C_DEPS)),)
-include $(C_DEPS)
endif
endif

-include ../makefile.defs

# Add inputs and outputs from these tool invocations to the build variables 

# All Target
all: $(TARGET)

# Tool invocations
$(TARGET): $(OBJS) $(USER_OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: GCC C++ Linker'
	$(GG) $(CFLAGS) -shared -o $(TARGET) $(OBJS) $(USER_OBJS) $(LIBS)
ifeq ($(DEBUG), 0)
	cp $(TARGET) $(STRIPPED_TARGET)
	$(STRIP) $(STRIPPED_TARGET)
endif
	@echo 'Finished building target: $@'
	@echo ' '

# Other Targets
clean:
	-$(RM) $(LIBRARIES)$(CC_DEPS)$(C++_DEPS)$(C_UPPER_DEPS)$(CXX_DEPS)$(OBJS)$(CPP_DEPS)$(C_DEPS) $(TARGET) $(STRIPPED_TARGET)
	-@echo ' '

.PHONY: all clean dependents
.SECONDARY:

-include ../makefile.targets
