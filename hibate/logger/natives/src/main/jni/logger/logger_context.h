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
 * logger_context.h
 *
 * @author Hibate
 * Created on 2017/06/22.
 */

#ifndef LOGGER_CONTEXT_H
#define LOGGER_CONTEXT_H

#include "logger.h"
#include "logger_core.h"

class OnLoggerListener {
public:
    OnLoggerListener() = default;
    virtual ~OnLoggerListener() = default;
    virtual int onLogging(LOG_PRIORITY priority, const char* tag, const char* location, const char* message) {
        return 0;
    }
};

class LoggerContext {
protected:
    LoggerContext();
    ~LoggerContext();

public:
    static LoggerContext* getInstance();
    [[nodiscard]] LoggerListenerRegistry* getLoggerListenerRegistry() const;

    [[nodiscard]] bool isLoggable(LOG_PRIORITY priority) const;
    int setPriority(LOG_PRIORITY priority);
    [[nodiscard]] bool isLineEnabled() const;
    int setLineEnabled(int enabled);

    static int addLoggerListener(OnLoggerListener* listener);

private:
    static LoggerContext* ctx;
    LoggerListenerRegistry* registry;

    LOG_PRIORITY level;
    bool         lineEnabled;
};

#endif //LOGGER_CONTEXT_H
