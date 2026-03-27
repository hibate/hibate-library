################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
	$(SOURCE_HOME)/libjni/codec/jni_av_codec.cpp \
	$(SOURCE_HOME)/libjni/codec/jni_av_codec_context.cpp \
	$(SOURCE_HOME)/libjni/codec/jni_av_codec_factory.cpp \
	$(SOURCE_HOME)/libjni/codec/jni_av_codec_id.cpp \
	$(SOURCE_HOME)/libjni/codec/jni_av_codec_parameters.cpp \
	$(SOURCE_HOME)/libjni/codec/jni_av_decoder.cpp \
	$(SOURCE_HOME)/libjni/codec/jni_av_encoder.cpp \
	$(SOURCE_HOME)/libjni/codec/jni_av_packet.cpp \

OBJS += \
	$(LOCAL_HOME)/libjni/codec/jni_av_codec.o \
	$(LOCAL_HOME)/libjni/codec/jni_av_codec_context.o \
	$(LOCAL_HOME)/libjni/codec/jni_av_codec_factory.o \
	$(LOCAL_HOME)/libjni/codec/jni_av_codec_id.o \
	$(LOCAL_HOME)/libjni/codec/jni_av_codec_parameters.o \
	$(LOCAL_HOME)/libjni/codec/jni_av_decoder.o \
	$(LOCAL_HOME)/libjni/codec/jni_av_encoder.o \
	$(LOCAL_HOME)/libjni/codec/jni_av_packet.o \

CPP_DEPS += \
	$(LOCAL_HOME)/libjni/codec/jni_av_codec.d \
	$(LOCAL_HOME)/libjni/codec/jni_av_codec_context.d \
	$(LOCAL_HOME)/libjni/codec/jni_av_codec_factory.d \
	$(LOCAL_HOME)/libjni/codec/jni_av_codec_id.d \
	$(LOCAL_HOME)/libjni/codec/jni_av_codec_parameters.d \
	$(LOCAL_HOME)/libjni/codec/jni_av_decoder.d \
	$(LOCAL_HOME)/libjni/codec/jni_av_encoder.d \
	$(LOCAL_HOME)/libjni/codec/jni_av_packet.d \


# Each subdirectory must supply rules for building sources it contributes
$(LOCAL_HOME)/libjni/codec/%.o: $(SOURCE_HOME)/libjni/codec/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	$(GG) $(CFLAGS) -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


