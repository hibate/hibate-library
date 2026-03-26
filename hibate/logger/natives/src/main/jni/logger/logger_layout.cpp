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
 * logger_layout.cpp
 *
 * @author Hibate
 * Created on 2017/06/23.
 */

#include <cstdio>
#include <chrono>

#ifdef _WIN32
#include <windows.h>
#else
#include <sys/time.h>
#endif

#include "logger.h"
#include "logger_layout.h"

#define TIME_FORMAT_LENGTH 80

LoggerLayout::LoggerLayout() = default;

LoggerLayout::~LoggerLayout() = default;

int LoggerLayout::getPriority(char **buffer, LOG_PRIORITY priority) {
    static const char* PRIORITIES[NUM_PRIORITY] = {
        "VERBOSE",
        "DEBUG",
        "INFO",
        "WARN",
        "ERROR",
        "FATAL",
        "SILENT",
        "UNKNOWN",
    };
    *buffer = const_cast<char *>(PRIORITIES[priority - 1]);
    return 0;
}

int LoggerLayout::getCurrentTime(char *buffer) {
    const auto now = std::chrono::system_clock::now();
    const std::time_t time = std::chrono::system_clock::to_time_t(now);
    std::tm local{};
#ifdef _WIN32
    localtime_s(&local, &time);
#else
    localtime_r(&time, &local);
#endif
    const auto msec = std::chrono::duration_cast<std::chrono::milliseconds>(
        now.time_since_epoch() % std::chrono::seconds(1)
    ).count();

    snprintf(buffer, TIME_FORMAT_LENGTH,
            "%4d-%02d-%02d %02d:%02d:%02d.%03d",
            local.tm_year + 1900, local.tm_mon + 1, local.tm_mday,
            local.tm_hour, local.tm_min, local.tm_sec, static_cast<int>(msec));
    return 0;
}

int LoggerLayout::toLayout(char *buffer, const int size, const char *tag, const char *location, const char *message) {
    if (nullptr != location) {
        snprintf(buffer, size, "[%.23s] [%.256s] %s",
                tag, location, (message) ? message : "");
    } else {
        snprintf(buffer, size, "[%.23s] %s", tag, (message) ? message : "");
    }
    return 0;
}

int LoggerLayout::toLayout(char *buffer, const int size, const LOG_PRIORITY priority, const char *tag, const char *location, const char *message) {
    char* priorities;
    getPriority(&priorities, priority);

    if (nullptr != location) {
        snprintf(buffer, size, "[%.10s] [%.23s] [%.256s] %s",
                static_cast<const char *>(priorities), tag, location, (nullptr != message) ? message : "");
    } else {
        snprintf(buffer, size, "[%.10s] [%.23s] %s",
                static_cast<const char *>(priorities), tag, (nullptr != message) ? message : "");
    }
    return 0;
}

int LoggerLayout::toTimeLayout(char *buffer, const int size, const char *tag, const char *location, const char *message) {
    char time[TIME_FORMAT_LENGTH] = {};
    getCurrentTime(time);

    if (nullptr != location) {
        snprintf(buffer, size, "%.30s [%.23s] [%.256s] %s",
                static_cast<const char *>(time), tag, location, (nullptr != message) ? message : "");
    } else {
        snprintf(buffer, size, "%.30s [%.23s] %s",
                static_cast<const char *>(time), tag, (nullptr != message) ? message : "");
    }
    return 0;
}

int LoggerLayout::toTimeLayout(char *buffer, const int size, const LOG_PRIORITY priority, const char *tag, const char *location, const char *message) {
    char time[TIME_FORMAT_LENGTH] = {};
    char* priorities;
    getCurrentTime(time);
    getPriority(&priorities, priority);

    if (nullptr != location) {
        snprintf(buffer, size, "%.30s [%.10s] [%.23s] [%.256s] %s",
               static_cast<const char *>(time), static_cast<const char *>(priorities), tag, location, (nullptr != message) ? message : "");
    } else {
        snprintf(buffer, size, "%.30s [%.10s] [%.23s] %s",
               static_cast<const char *>(time), static_cast<const char *>(priorities), tag, (nullptr != message) ? message : "");
    }
    return 0;
}
