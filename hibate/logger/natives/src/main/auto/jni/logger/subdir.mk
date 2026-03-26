################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables
CPP_SRCS += \
    $(SOURCE_HOME)/logger/lock.cpp \
    $(SOURCE_HOME)/logger/logger_adapter.cpp \
    $(SOURCE_HOME)/logger/logger_context.cpp \
    $(SOURCE_HOME)/logger/logger_core.cpp \
    $(SOURCE_HOME)/logger/logger_layout.cpp \

C_SRCS += \
    $(SOURCE_HOME)/logger/logger.c \

OBJS += \
    $(LOCAL_HOME)/logger/lock.o \
    $(LOCAL_HOME)/logger/logger.o \
    $(LOCAL_HOME)/logger/logger_adapter.o \
    $(LOCAL_HOME)/logger/logger_context.o \
    $(LOCAL_HOME)/logger/logger_core.o \
    $(LOCAL_HOME)/logger/logger_layout.o \

CPP_DEPS += \
    $(LOCAL_HOME)/logger/lock.d \
    $(LOCAL_HOME)/logger/logger_adapter.d \
    $(LOCAL_HOME)/logger/logger_context.d \
    $(LOCAL_HOME)/logger/logger_core.d \
    $(LOCAL_HOME)/logger/logger_layout.d \

C_DEPS += \
    $(LOCAL_HOME)/logger/logger.d \


# Each subdirectory must supply rules for building sources it contributes
$(LOCAL_HOME)/logger/%.o: $(SOURCE_HOME)/logger/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	$(GG) $(CFLAGS) -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

$(LOCAL_HOME)/logger/%.o: $(SOURCE_HOME)/logger/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	$(CC) $(CFLAGS) -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


