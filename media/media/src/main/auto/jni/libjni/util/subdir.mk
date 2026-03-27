################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
	$(SOURCE_HOME)/libjni/util/jni_av_channel_layout.cpp \
	$(SOURCE_HOME)/libjni/util/jni_av_dictionary.cpp \
	$(SOURCE_HOME)/libjni/util/jni_av_frame.cpp \
	$(SOURCE_HOME)/libjni/util/jni_av_log.cpp \
	$(SOURCE_HOME)/libjni/util/jni_av_math.cpp \
	$(SOURCE_HOME)/libjni/util/jni_av_options.cpp \
	$(SOURCE_HOME)/libjni/util/jni_av_rational.cpp \
	$(SOURCE_HOME)/libjni/util/jni_av_util.cpp \

OBJS += \
	$(LOCAL_HOME)/libjni/util/jni_av_channel_layout.o \
	$(LOCAL_HOME)/libjni/util/jni_av_dictionary.o \
	$(LOCAL_HOME)/libjni/util/jni_av_frame.o \
	$(LOCAL_HOME)/libjni/util/jni_av_log.o \
	$(LOCAL_HOME)/libjni/util/jni_av_math.o \
	$(LOCAL_HOME)/libjni/util/jni_av_options.o \
	$(LOCAL_HOME)/libjni/util/jni_av_rational.o \
	$(LOCAL_HOME)/libjni/util/jni_av_util.o \

CPP_DEPS += \
	$(LOCAL_HOME)/libjni/util/jni_av_channel_layout.d \
	$(LOCAL_HOME)/libjni/util/jni_av_dictionary.d \
	$(LOCAL_HOME)/libjni/util/jni_av_frame.d \
	$(LOCAL_HOME)/libjni/util/jni_av_log.d \
	$(LOCAL_HOME)/libjni/util/jni_av_math.d \
	$(LOCAL_HOME)/libjni/util/jni_av_options.d \
	$(LOCAL_HOME)/libjni/util/jni_av_rational.d \
	$(LOCAL_HOME)/libjni/util/jni_av_util.d \


# Each subdirectory must supply rules for building sources it contributes
$(LOCAL_HOME)/utils/%.o: $(SOURCE_HOME)/utils/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	$(GG) $(CFLAGS) -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


