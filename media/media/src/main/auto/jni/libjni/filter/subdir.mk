################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
	$(SOURCE_HOME)/libjni/filter/jni_av_filter.cpp \
	$(SOURCE_HOME)/libjni/filter/jni_av_filter_context.cpp \
	$(SOURCE_HOME)/libjni/filter/jni_av_filter_factory.cpp \
	$(SOURCE_HOME)/libjni/filter/jni_av_filter_graph.cpp \
	$(SOURCE_HOME)/libjni/filter/jni_av_filter_in_out.cpp \

OBJS += \
	$(LOCAL_HOME)/libjni/filter/jni_av_filter.o \
	$(LOCAL_HOME)/libjni/filter/jni_av_filter_context.o \
	$(LOCAL_HOME)/libjni/filter/jni_av_filter_factory.o \
	$(LOCAL_HOME)/libjni/filter/jni_av_filter_graph.o \
	$(LOCAL_HOME)/libjni/filter/jni_av_filter_in_out.o \

CPP_DEPS += \
	$(LOCAL_HOME)/libjni/filter/jni_av_filter.d \
	$(LOCAL_HOME)/libjni/filter/jni_av_filter_context.d \
	$(LOCAL_HOME)/libjni/filter/jni_av_filter_factory.d \
	$(LOCAL_HOME)/libjni/filter/jni_av_filter_graph.d \
	$(LOCAL_HOME)/libjni/filter/jni_av_filter_in_out.d \


# Each subdirectory must supply rules for building sources it contributes
$(LOCAL_HOME)/libjni/filter/%.o: $(SOURCE_HOME)/libjni/filter/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	$(GG) $(CFLAGS) -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


