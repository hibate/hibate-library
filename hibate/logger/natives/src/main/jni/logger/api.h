/*
* Copyright (C) 2025 Hibate <ycaia86@126.com>
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

/*
 * logger.h
 *
 *  Created on: Dec 19, 2025
 *      Author: Hibate
 */

#ifndef LIBRARY_API_H_
#define LIBRARY_API_H_

#ifndef LIBEXPORT
    #if defined(_WIN32) || defined(_WIN64)
        #define LIBEXPORT __declspec(dllexport)
    #else
        #if __GNUC__ >= 4
            #define LIBEXPORT __attribute__ ((visibility ("default")))
        #else
            #define LIBEXPORT
        #endif
    #endif
#endif

#ifndef LIBIMPORT
    #if defined(_WIN32) || defined(_WIN64)
        #define LIBIMPORT __declspec(dllimport)
    #else
        #if __GNUC__ >= 4
            #define LIBIMPORT __attribute__ ((visibility ("default")))
        #else
            #define LIBIMPORT
        #endif
    #endif
#endif

#ifndef LIBCALL
    #if defined(_WIN32) || defined(_WIN64)
        #if !defined(_WIN64)
            #define LIBCALL __cdecl
        #else
            #define LIBCALL
        #endif
    #else
        #define LIBCALL
    #endif
#endif

#ifdef LIB_IMPLEMENTATION
    #define LIB_API LIBEXPORT
#else
    #define LIB_API LIBIMPORT
#endif

#endif //LIBRARY_API_H_
