/*
 * Copyright (C) 2018 Hibate <ycaia86@126.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * jni_util.h
 *
 * @author Hibate
 * Created on 2017/06/19.
 */

#ifndef JNI_UTILS_JNI_UTIL_H_
#define JNI_UTILS_JNI_UTIL_H_

#include <jni.h>

#define NELEM(m) (sizeof(m) / sizeof((m)[0]))

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    int (*registerMethod)(JNIEnv* env);
    void (*unregisterMethod)(JNIEnv* env);
} MethodLoader;

/**
 * 抛出异常
 */
int throwException(JNIEnv* env, const char* className, const char* message);

/**
 * 抛出自定义异常
 */
int throwUserException(JNIEnv* env, jclass clazz, const char* msg);

/**
 * 返回 JVM
 */
JavaVM* getVM();

/**
 * 返回 JNIEnv 对象
 */
JNIEnv* getJniEnv();

/**
 * 注册本地方法
 */
int registerNativeMethods(JNIEnv* env, const char* className,
        const JNINativeMethod* methods, int numMethods);

#ifdef __cplusplus
}
#endif


#endif /* JNI_UTILS_JNI_UTIL_H_ */
