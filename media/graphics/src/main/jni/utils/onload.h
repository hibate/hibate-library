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
 * onload.h
 *
 * @author Hibate
 * Created on 2017/06/19.
 */

#ifndef JNI_UTILS_ONLOAD_H_
#define JNI_UTILS_ONLOAD_H_

#include "jni_util.h"

#ifdef __cplusplus
extern "C" {
#endif

int getMethodLoaders(MethodLoader** loaders, int* size);

#ifdef __cplusplus
}
#endif


#endif /* JNI_UTILS_ONLOAD_H_ */
