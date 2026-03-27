################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
	$(SOURCE_HOME)/utils/jni_util.cpp \

OBJS += \
	$(LOCAL_HOME)/utils/jni_util.o \

CPP_DEPS += \
	$(LOCAL_HOME)/utils/jni_util.d \


# Each subdirectory must supply rules for building sources it contributes
$(LOCAL_HOME)/utils/%.o: $(SOURCE_HOME)/utils/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	$(GG) $(CFLAGS) -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


