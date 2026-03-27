################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables
-include ${LOCAL_HOME}/libjni/codec/subdir.mk
-include ${LOCAL_HOME}/libjni/filter/subdir.mk
-include ${LOCAL_HOME}/libjni/format/subdir.mk
-include ${LOCAL_HOME}/libjni/util/subdir.mk

CPP_SRCS += \
	$(SOURCE_HOME)/libjni/onLoad.cpp \

OBJS += \
	$(LOCAL_HOME)/libjni/onLoad.o \

CPP_DEPS += \
	$(LOCAL_HOME)/libjni/onLoad.d \


# Each subdirectory must supply rules for building sources it contributes
$(LOCAL_HOME)/libjni/%.o: $(SOURCE_HOME)/libjni/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	$(GG) $(CFLAGS) -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


