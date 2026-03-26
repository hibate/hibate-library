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
 * logger_core.cpp
 *
 * @author Hibate
 * Created on 2017/06/23.
 */

#include <cstdio>
#include <cstdarg>

#include "logger.h"
#include "logger_core.h"
#include "logger_context.h"

LIB_API void log_appender_internal(LOG_PRIORITY priority, const char* tag, const char* location, const char* format, ...);
void (*log_appender)(LOG_PRIORITY priority, const char* tag, const char* location, const char* format, ...) = log_appender_internal;

LoggerListenerRegistry::LoggerListenerRegistry() {
    this->lock = new ReadWriteLock();
}

LoggerListenerRegistry::~LoggerListenerRegistry() {
    this->lock->writeLock()->lock();
    for (auto it = this->listeners.begin(); it != this->listeners.end();) {
        delete *it;
        it = this->listeners.erase(it);
    }
    this->lock->writeLock()->unlock();
    delete this->lock;
    this->lock = nullptr;
}

int LoggerListenerRegistry::addLoggerListener(OnLoggerListener *listener) {
    if (nullptr != listener) {
        this->lock->writeLock()->lock();
        this->listeners.push_back(listener);
        this->lock->writeLock()->unlock();
    }
    return 0;
}

void LoggerListenerRegistry::notifyListeners(const LOG_PRIORITY priority, const char* tag, const char* location, const char* message) const {
    this->lock->readLock()->lock();
    for (const auto listener : this->listeners) {
        if (nullptr == listener) {
            continue;
        }
        listener->onLogging(priority, tag, location, message);
    }
    this->lock->readLock()->unlock();
}

LIB_API void log_appender_internal(LOG_PRIORITY priority, const char* tag, const char* location, const char* format, ...) {
    const LoggerContext* ctx = LoggerContext::getInstance();
    if (!ctx->isLoggable(priority)) {
        return;
    }

    char buf[LOGGER_BUFFER_MAX_LENGTH];
    va_list args;
    va_start(args, format);
    vsnprintf(buf, LOGGER_BUFFER_MAX_LENGTH, format, args);
    va_end(args);

    if (const LoggerListenerRegistry* registry = ctx->getLoggerListenerRegistry(); nullptr != registry) {
        registry->notifyListeners(priority, (nullptr == tag) ? "" : tag,
                ctx->isLineEnabled() ? location : nullptr, buf);
    }
}

int addLoggerListenerInternal(OnLoggerListener* listener) {
    const LoggerContext* ctx = LoggerContext::getInstance();
    if (LoggerListenerRegistry* registry = ctx->getLoggerListenerRegistry(); nullptr != registry) {
        return registry->addLoggerListener(listener);
    }
    return -1;
}
