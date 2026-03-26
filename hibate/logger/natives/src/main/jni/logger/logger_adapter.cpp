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
 * logger_adapter.cpp
 *
 * @author Hibate
 * Created on 2017/06/22.
 */

#include "logger_context.h"

class LoggerListenerAdapter : public OnLoggerListener {
public:
    explicit LoggerListenerAdapter(LoggerListener callback);
    ~LoggerListenerAdapter() override;

    int onLogging(LOG_PRIORITY priority, const char* tag, const char* location, const char* message) override;
private:
    LoggerListener callback;
};

LoggerListenerAdapter::LoggerListenerAdapter(const LoggerListener callback) {
    this->callback = callback;
}

LoggerListenerAdapter::~LoggerListenerAdapter() = default;

int LoggerListenerAdapter::onLogging(const LOG_PRIORITY priority, const char* tag, const char *location, const char *message) {
    if (nullptr != this->callback) {
        return this->callback(priority, tag, location, message);
    }
    return -1;
}

#ifdef __cplusplus
extern "C"
{
#endif

int isLoggableAdapter(const LOG_PRIORITY priority) {
    return LoggerContext::getInstance()->isLoggable(priority) ? 1 : 0;
}

int setPriorityAdapter(const LOG_PRIORITY priority) {
    return LoggerContext::getInstance()->setPriority(priority);
}

int setLineEnabledAdapter(const int iEnable) {
    return LoggerContext::getInstance()->setLineEnabled(iEnable);
}

int addLoggerListenerAdapter(const LoggerListener callback) {
    if (nullptr != callback) {
        return LoggerContext::addLoggerListener(new LoggerListenerAdapter(callback));
    }
    return 0;
}

#ifdef __cplusplus
}
#endif
