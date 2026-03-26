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
 * lock.h
 *
 * @author Hibate
 * Created on 2025/12/19.
 */

#ifndef LOGGER_LOCK_H
#define LOGGER_LOCK_H

#ifdef _WIN32
#include <windows.h>
#else
#include <pthread.h>
#endif

#include "api.h"

class LIB_API Lock {
public:
    Lock();
    virtual ~Lock();

    /**
     * 获取锁。
     * 如果锁不可用, 则当前线程将被禁用以进行线程调度, 并处于休眠状态, 直到获取锁为止。
     */
    virtual void lock() const;

    /**
     * 仅当调用时锁是空闲的时才获取锁。
     * 如果可用则获取锁并立即返回值 {@code true}, 如果锁不可用, 则此方法将立即返回值 {@code false}。
     * @return 如果获取了锁, 则返回 {@code true}, 否则返回 {@code false}
     */
    [[nodiscard]] virtual bool tryLock() const;

    /**
     * 如果在给定的等待时间内锁是空闲的, 则获取锁。
     * 如果锁可用, 此方法将立即返回 {@code true}。如果锁不可用, 则当前线程将被禁用以进行线程调度, 并处于休眠状态, 直到以下两种情况之一发生:
     * 1. 锁已被当前线程获取
     * 2. 指定的等待时间已过
     * @param millis 等待锁的最长时间
     * @return 如果已获取锁, 则返回 {@code true}；如果在获取锁之前等待了一段时间, 则返回 {@code false}
     */
    [[nodiscard]] virtual bool tryLock(long millis) const;

    /**
     * 释放锁
     */
    virtual void unlock() const;
private:
#ifdef _WIN32
    HANDLE hMutex;
#else
    mutable pthread_mutex_t hMutex{};
#endif
};

class LIB_API ReadWriteLock {
public:
    ReadWriteLock();
    ~ReadWriteLock();

    /**
     * 返回用于读取的锁。
     * @return 读锁
     */
    [[nodiscard]] Lock* readLock() const;

    /**
     * 返回用于写入的锁。
     * @return 写锁
     */
    [[nodiscard]] Lock* writeLock() const;
private:
    Lock* readerLock;
    Lock* writerLock;
#ifdef _WIN32
    SRWLOCK hMutex{};
#else
    mutable pthread_rwlock_t hMutex{};
#endif
};

#endif //LOGGER_LOCK_H