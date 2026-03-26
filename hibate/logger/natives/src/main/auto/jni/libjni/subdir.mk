################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
	$(SOURCE_HOME)/libjni/onLoad.cpp \
	$(SOURCE_HOME)/libjni/jni_logger_context.cpp \
	$(SOURCE_HOME)/libjni/jni_logger_handler.cpp \

OBJS += \
	$(LOCAL_HOME)/libjni/onLoad.o \
	$(LOCAL_HOME)/libjni/jni_logger_context.o \
	$(LOCAL_HOME)/libjni/jni_logger_handler.o \

CPP_DEPS += \
	$(LOCAL_HOME)/libjni/onLoad.d \
	$(LOCAL_HOME)/libjni/jni_logger_context.d \
	$(LOCAL_HOME)/libjni/jni_logger_handler.d \


# Each subdirectory must supply rules for building sources it contributes
$(LOCAL_HOME)/libjni/%.o: $(SOURCE_HOME)/libjni/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	$(GG) $(CFLAGS) -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


