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

/**
 * lock.cpp
 *
 * @author Hibate
 * Created on 2025/12/19.
 */

#ifndef _WIN32
#include <ctime>
#endif

#ifdef __APPLE__
#include <unistd.h>
#include <errno.h>
#endif

#include "lock.h"

#ifndef _WIN32
void mutex_timed_lock(long millis, timespec *ts);
#endif

#ifdef __APPLE__
int pthread_mutex_timedlock(pthread_mutex_t *mutex, const struct timespec *abstime);
int pthread_rwlock_timedrdlock(pthread_rwlock_t *rwlock, const struct timespec *abstime);
int pthread_rwlock_timedwrlock(pthread_rwlock_t *rwlock, const struct timespec *abstime);
#endif

class ReadLock final : public Lock {
public:
#ifdef _WIN32
    explicit ReadLock(PSRWLOCK hMutex);
#else
    explicit ReadLock(pthread_rwlock_t* hMutex);
#endif
    ~ReadLock() override;

    void lock() const override;
    [[nodiscard]] bool tryLock(long millis) const override;
    void unlock() const override;
private:
#ifdef _WIN32
    PSRWLOCK hMutex{};
#else
    pthread_rwlock_t* hMutex;
#endif
};

class WriteLock final : public Lock {
public:
#ifdef _WIN32
    explicit WriteLock(PSRWLOCK hMutex);
#else
    explicit WriteLock(pthread_rwlock_t* hMutex);
#endif
    ~WriteLock() override;

    void lock() const override;
    [[nodiscard]] bool tryLock(long millis) const override;
    void unlock() const override;
private:
#ifdef _WIN32
    PSRWLOCK hMutex{};
#else
    pthread_rwlock_t* hMutex;
#endif
};

#ifndef _WIN32
void mutex_timed_lock(long millis, timespec *ts) {
    clock_gettime(CLOCK_REALTIME, ts);
    ts->tv_sec += millis / 1000;
    ts->tv_nsec += (millis % 1000) * 1000000;
    if (ts->tv_nsec >= 1000000000) { // 纳秒溢出
        ts->tv_sec += 1;
        ts->tv_nsec -= 1000000000;
    }
}
#endif

#ifdef __APPLE__
int pthread_mutex_timedlock(pthread_mutex_t *mutex, const struct timespec *abstime) {
    int ret;
    while ((ret = pthread_mutex_trylock(mutex)) == EBUSY) {
        struct timespec now;
        clock_gettime(CLOCK_REALTIME, &now);
        if (now.tv_sec > abstime->tv_sec ||
           (now.tv_sec == abstime->tv_sec && now.tv_nsec >= abstime->tv_nsec)) {
            return ETIMEDOUT;
        }
        usleep(1000);
    }
    return ret;
}

int pthread_rwlock_timedrdlock(pthread_rwlock_t *rwlock, const struct timespec *abstime) {
    int ret;
    while ((ret = pthread_rwlock_tryrdlock(rwlock)) == EBUSY) {
        struct timespec now;
        clock_gettime(CLOCK_REALTIME, &now);
        if (now.tv_sec > abstime->tv_sec ||
           (now.tv_sec == abstime->tv_sec && now.tv_nsec >= abstime->tv_nsec)) {
            return ETIMEDOUT;
        }
        usleep(1000);
    }
    return ret;
}

int pthread_rwlock_timedwrlock(pthread_rwlock_t *rwlock, const struct timespec *abstime) {
    int ret;
    while ((ret = pthread_rwlock_trywrlock(rwlock)) == EBUSY) {
        struct timespec now;
        clock_gettime(CLOCK_REALTIME, &now);
        if (now.tv_sec > abstime->tv_sec ||
           (now.tv_sec == abstime->tv_sec && now.tv_nsec >= abstime->tv_nsec)) {
            return ETIMEDOUT;
        }
        usleep(1000);
    }
    return ret;
}
#endif

Lock::Lock() {
#ifdef _WIN32
    this->hMutex = CreateMutex(nullptr, FALSE, nullptr);
#else
    pthread_mutex_init(&this->hMutex, nullptr);
#endif
}

Lock::~Lock() {
#ifdef _WIN32
    CloseHandle(this->hMutex);
    this->hMutex = nullptr;
#else
    pthread_mutex_destroy(&this->hMutex);
#endif
}

void Lock::lock() const {
#ifdef _WIN32
    WaitForSingleObject(this->hMutex, INFINITE);
#else
    pthread_mutex_lock(&this->hMutex);
#endif
}

bool Lock::tryLock() const {
    return this->tryLock(0);
}

bool Lock::tryLock(const long millis) const {
#ifdef _WIN32
    const DWORD status = WaitForSingleObject(this->hMutex, millis);
    return WAIT_OBJECT_0 == status;
#else
    if (millis <= 0) {
        return pthread_mutex_trylock(&this->hMutex) == 0;
    }
    timespec ts{};
    mutex_timed_lock(millis, &ts);
    return pthread_mutex_timedlock(&this->hMutex, &ts) == 0;
#endif
}

void Lock::unlock() const {
#ifdef _WIN32
    ReleaseMutex(this->hMutex);
#else
    pthread_mutex_unlock(&this->hMutex);
#endif
}

/**
 * ReadWriteLock
 */
ReadWriteLock::ReadWriteLock() {
#ifdef _WIN32
    InitializeSRWLock(&this->hMutex);
    this->readerLock = new ReadLock(PSRWLOCK(&this->hMutex));
    this->writerLock = new WriteLock(PSRWLOCK(&this->hMutex));
#else
    pthread_rwlock_init(&this->hMutex, nullptr);
    this->readerLock = new ReadLock(&this->hMutex);
    this->writerLock = new WriteLock(&this->hMutex);
#endif
}

ReadWriteLock::~ReadWriteLock() {
    delete this->readerLock;
    delete this->writerLock;
#ifdef _WIN32
#else
    pthread_rwlock_destroy(&this->hMutex);
#endif
    this->readerLock = nullptr;
    this->writerLock = nullptr;
}

Lock* ReadWriteLock::readLock() const {
    return this->readerLock;
}

Lock* ReadWriteLock::writeLock() const {
    return this->writerLock;
}

#ifdef _WIN32
ReadLock::ReadLock(PSRWLOCK hMutex) {
#else
ReadLock::ReadLock(pthread_rwlock_t* hMutex) {
#endif
    this->hMutex = hMutex;
}

ReadLock::~ReadLock() = default;

void ReadLock::lock() const {
#ifdef _WIN32
    AcquireSRWLockShared(this->hMutex);
#else
    pthread_rwlock_rdlock(this->hMutex);
#endif
}

bool ReadLock::tryLock(const long millis) const {
#ifdef _WIN32
    return TryAcquireSRWLockShared(this->hMutex);
#else
    if (millis <= 0) {
        return pthread_rwlock_tryrdlock(this->hMutex) == 0;
    }
    timespec ts{};
    mutex_timed_lock(millis, &ts);
    return pthread_rwlock_timedrdlock(this->hMutex, &ts) == 0;
#endif
}

void ReadLock::unlock() const {
#ifdef _WIN32
    ReleaseSRWLockShared(this->hMutex);
#else
    pthread_rwlock_unlock(this->hMutex);
#endif
}

#ifdef _WIN32
WriteLock::WriteLock(PSRWLOCK hMutex) {
#else
WriteLock::WriteLock(pthread_rwlock_t* hMutex) {
#endif
    this->hMutex = hMutex;
}

WriteLock::~WriteLock() = default;

void WriteLock::lock() const {
#ifdef _WIN32
    AcquireSRWLockExclusive(this->hMutex);
#else
    pthread_rwlock_wrlock(this->hMutex);
#endif
}

bool WriteLock::tryLock(const long millis) const {
#ifdef _WIN32
    return TryAcquireSRWLockExclusive(this->hMutex);
#else
    if (millis <= 0) {
        return pthread_rwlock_trywrlock(this->hMutex) == 0;
    }
    timespec ts{};
    mutex_timed_lock(millis, &ts);
    return pthread_rwlock_timedwrlock(this->hMutex, &ts) == 0;
#endif
}

void WriteLock::unlock() const {
#ifdef _WIN32
    ReleaseSRWLockExclusive(this->hMutex);
#else
    pthread_rwlock_unlock(this->hMutex);
#endif
}
