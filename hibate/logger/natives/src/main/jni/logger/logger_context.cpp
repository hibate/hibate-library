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
 * logger_context.cpp
 *
 * @author Hibate
 * Created on 2017/06/22.
 */

#include "logger_context.h"

extern int addLoggerListenerInternal(OnLoggerListener* listener);

/**
 * LoggerContext
 */

LoggerContext* LoggerContext::ctx = new LoggerContext();

/**
 * 使用饿汉模式实现线程安全的单例
 * @return 返回单例对象
 */
LoggerContext* LoggerContext::getInstance() {
    return ctx;
}

LoggerContext::LoggerContext() {
    this->registry = new LoggerListenerRegistry();
    this->level = LOG_INFO;
    this->lineEnabled = false;
}

LoggerContext::~LoggerContext() {
    delete this->registry;
    this->registry = nullptr;
}

LoggerListenerRegistry* LoggerContext::getLoggerListenerRegistry() const {
    return this->registry;
}

bool LoggerContext::isLoggable(const LOG_PRIORITY priority) const {
    if ((LOG_SILENT > this->level) && (LOG_SILENT > priority)) {
        return this->level <= priority;
    }
    return false;
}

int LoggerContext::setPriority(const LOG_PRIORITY priority) {
    if (NUM_PRIORITY != priority) {
        this->level = priority;
    }
    return 0;
}

bool LoggerContext::isLineEnabled() const {
    return this->lineEnabled;
}

int LoggerContext::setLineEnabled(const int enabled) {
    this->lineEnabled = (enabled != 0);
    return 0;
}

int LoggerContext::addLoggerListener(OnLoggerListener *listener) {
    return addLoggerListenerInternal(listener);
}
