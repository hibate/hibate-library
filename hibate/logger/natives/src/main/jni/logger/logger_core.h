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
 * logger_core.h
 *
 * @author Hibate
 * Created on 2017/06/23.
 */

#ifndef LOGGER_CORE_H_
#define LOGGER_CORE_H_

#include <list>
#include "lock.h"

class OnLoggerListener;

class LoggerListenerRegistry {
public:
    LoggerListenerRegistry();
    ~LoggerListenerRegistry();

    int addLoggerListener(OnLoggerListener* listener);

    void notifyListeners(LOG_PRIORITY priority, const char* tag, const char* location, const char* message) const;
private:
    std::list<OnLoggerListener*> listeners;
    ReadWriteLock* lock;
};

#endif /* LOGGER_CORE_H_ */
