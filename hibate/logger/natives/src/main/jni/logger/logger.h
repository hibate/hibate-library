/*
 * Copyright (C) 2017 Hibate <ycaia86@126.com>
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
 * logger.h
 *
 * @author Hibate
 * Created on 2015/12/07.
 */

#ifndef LOGGER_LOGGER_H_
#define LOGGER_LOGGER_H_

#include "api.h"

#ifdef __cplusplus
extern "C"
{
#endif

#ifndef TAG
#define TAG "Logger"
#endif

#ifdef TAG_MAX_LENGTH
#undef TAG_MAX_LENGTH
#endif
#define TAG_MAX_LENGTH 23

#ifndef LOGGER_BUFFER_MAX_LENGTH
#define LOGGER_BUFFER_MAX_LENGTH  2048
#endif

typedef enum
{
    LOG_VERBOSE = 1,
    LOG_DEBUG,
    LOG_INFO,
    LOG_WARN,
    LOG_ERROR,
    LOG_FATAL,
    LOG_SILENT,
    NUM_PRIORITY,
} LOG_PRIORITY;

#define  STRINGIFY(x) #x
#define  TOSTRING(x) STRINGIFY(x)
#define  LOCATION __FILE__ " : " TOSTRING(__LINE__)

LIB_API extern void (*log_appender)(LOG_PRIORITY priority, const char* tag, const char* location, const char* format, ...);

#define LPRINT(prio, ...)		log_appender(prio, TAG, LOCATION, ##__VA_ARGS__)
#define LVERBOSE(...)         	LPRINT(LOG_VERBOSE, ##__VA_ARGS__)
#define LDEBUG(...)           	LPRINT(LOG_DEBUG, ##__VA_ARGS__)
#define LINFO(...)              LPRINT(LOG_INFO, ##__VA_ARGS__)
#define LWARN(...)            	LPRINT(LOG_WARN, ##__VA_ARGS__)
#define LERROR(...)           	LPRINT(LOG_ERROR, ##__VA_ARGS__)
#define LFATAL(...)           	LPRINT(LOG_FATAL, ##__VA_ARGS__)
#define LSILENT(...)          	LPRINT(LOG_SILENT, ##__VA_ARGS__)

#define CHECK_ERROR(cond, value, __format, ...) \
    do { \
        if (cond) { \
            LERROR(__format, ##__VA_ARGS__); \
            return value; \
        } \
    } while(0)

/**
 * 当设置 enableLine 为 false 时，location 参数为 null
 */
typedef int (*LoggerListener)(LOG_PRIORITY priority, const char* tag, const char* location, const char* message);
typedef struct {
    int (*isLoggable)(LOG_PRIORITY priority);
    int (*setPriority)(LOG_PRIORITY priority);
    int (*setLineEnabled)(int iEnable);
    int (*addLoggerListener)(LoggerListener callback);
} Logger_t;

LIB_API extern Logger_t Logger;

#ifdef __cplusplus
}
#endif

#endif /* LOGGER_LOGGER_H_ */
