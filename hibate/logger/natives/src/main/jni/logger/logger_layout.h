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
 * logger_layout.h
 *
 * @author Hibate
 * Created on 2017/06/23.
 */

#ifndef LOGGER_LAYOUT_H
#define LOGGER_LAYOUT_H

#include "api.h"
#include "logger.h"

class LIB_API LoggerLayout {
public:
    static int toLayout(char* buffer, int size, const char* tag, const char* location, const char* message);
    static int toLayout(char* buffer, int size, LOG_PRIORITY priority, const char* tag, const char* location, const char* message);
    static int toTimeLayout(char* buffer, int size, const char* tag, const char* location, const char* message);
    static int toTimeLayout(char* buffer, int size, LOG_PRIORITY priority, const char* tag, const char* location, const char* message);
protected:
    LoggerLayout();
    ~LoggerLayout();
private:
    static int getPriority(char** buffer, LOG_PRIORITY priority);
    static int getCurrentTime(char* buffer);
};

#endif //LOGGER_LAYOUT_H