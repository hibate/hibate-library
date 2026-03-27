################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
	$(SOURCE_HOME)/lang/text/text_utils.cpp \

OBJS += \
	$(LOCAL_HOME)/lang/text/text_utils.o \

CPP_DEPS += \
	$(LOCAL_HOME)/lang/text/text_utils.d \


# Each subdirectory must supply rules for building sources it contributes
$(LOCAL_HOME)/lang/text/%.o: $(SOURCE_HOME)/lang/text/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	$(GG) $(CFLAGS) -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


