/*
 * Copyright (C) 2026 Hibate <ycaia86@126.com>
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
 * jni_av_util.h
 *
 * @author Hibate
 * Created on 2026/04/25.
 */

#ifndef MEDIA_JNI_AV_UTIL_H
#define MEDIA_JNI_AV_UTIL_H

extern "C" {
#include <libavutil/version.h>
}

#define AV_VERSION_SDK LIBAVUTIL_VERSION_INT

/**
 * n6.1
 *
 * <p>Released in November 2023
 */
#define AV_VERSION_N6_1 AV_VERSION_INT(58, 29, 100)

/**
 * n7.0
 *
 * <p>Released in April 2024
 */
#define AV_VERSION_N7_0 AV_VERSION_INT(59, 8, 100)

/**
 * n7.1
 *
 * <p>Released in September 2024
 */
#define AV_VERSION_N7_1 AV_VERSION_INT(59, 39, 100)

/**
 * n8.0
 *
 * <p>Released in August 2025
 */
#define AV_VERSION_N8_0 AV_VERSION_INT(60, 8, 100)

/**
 * n8.1
 *
 * <p>Released in February 2026.
 */
#define AV_VERSION_N8_1 AV_VERSION_INT(60, 26, 100)

#endif //MEDIA_JNI_AV_UTIL_H
