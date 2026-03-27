################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
	$(SOURCE_HOME)/libjni/format/jni_av_format.cpp \
	$(SOURCE_HOME)/libjni/format/jni_av_format_context.cpp \
	$(SOURCE_HOME)/libjni/format/jni_av_input_format.cpp \
	$(SOURCE_HOME)/libjni/format/jni_av_input_stream.cpp \
	$(SOURCE_HOME)/libjni/format/jni_av_output_format.cpp \
	$(SOURCE_HOME)/libjni/format/jni_av_output_stream.cpp \
	$(SOURCE_HOME)/libjni/format/jni_av_stream.cpp \

OBJS += \
	$(LOCAL_HOME)/libjni/format/jni_av_format.o \
	$(LOCAL_HOME)/libjni/format/jni_av_format_context.o \
	$(LOCAL_HOME)/libjni/format/jni_av_input_format.o \
	$(LOCAL_HOME)/libjni/format/jni_av_input_stream.o \
	$(LOCAL_HOME)/libjni/format/jni_av_output_format.o \
	$(LOCAL_HOME)/libjni/format/jni_av_output_stream.o \
	$(LOCAL_HOME)/libjni/format/jni_av_stream.o \

CPP_DEPS += \
	$(LOCAL_HOME)/libjni/format/jni_av_format.d \
	$(LOCAL_HOME)/libjni/format/jni_av_format_context.d \
	$(LOCAL_HOME)/libjni/format/jni_av_input_format.d \
	$(LOCAL_HOME)/libjni/format/jni_av_input_stream.d \
	$(LOCAL_HOME)/libjni/format/jni_av_output_format.d \
	$(LOCAL_HOME)/libjni/format/jni_av_output_stream.d \
	$(LOCAL_HOME)/libjni/format/jni_av_stream.d \


# Each subdirectory must supply rules for building sources it contributes
$(LOCAL_HOME)/libjni/format/%.o: $(SOURCE_HOME)/libjni/format/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	$(GG) $(CFLAGS) -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


