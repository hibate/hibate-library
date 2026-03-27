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

package org.hibate.graphics.buffer;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Hibate
 * Created on 2017/07/26.
 */
public class ByteBufferFactoryImpl implements ByteBufferFactory {

    public static final int DEFAULT_CAPACITY = 1024;

    private ByteBuffer byteBuffer;
    private int capacity;

    private final ReadWriteLock lock;

    public ByteBufferFactoryImpl() {
        this(DEFAULT_CAPACITY);
    }

    public ByteBufferFactoryImpl(int capacity) {
        this.capacity = capacity;
        this.lock = new ReentrantReadWriteLock();

        this.resize(capacity);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Nullable
    @Override
    public ByteBuffer getByteBuffer() {
        ByteBuffer byteBuffer;

        Lock lock = this.lock.readLock();
        lock.lock();
        byteBuffer = this.byteBuffer;
        lock.unlock();

        return byteBuffer;
    }

    @Nullable
    @Override
    public ByteBuffer getByteBuffer(int capacity) {
        if (this.capacity < capacity) {
            this.resize(capacity);
        }

        return this.getByteBuffer();
    }

    @Override
    public void clean() {
        ByteBuffer byteBuffer = this.getByteBuffer();
        Lock lock = this.lock.writeLock();
        lock.lock();
        if ((byteBuffer != null) && byteBuffer.isDirect()) {
            try {
                releaseDirectBuffer(byteBuffer);
            } catch (Throwable t) {
                t.printStackTrace(System.out);
            }
        }
        this.byteBuffer = null;
        lock.unlock();
    }

    public void resize(int capacity) {
        ByteBuffer byteBuffer = this.getByteBuffer();
        if ((byteBuffer != null) && (this.capacity == capacity)) {
            return;
        }

        Lock lock = this.lock.writeLock();
        lock.lock();

        if ((byteBuffer != null) && byteBuffer.isDirect()) {
            try {
                releaseDirectBuffer(this.byteBuffer);
            } catch (Throwable t) {
                t.printStackTrace(System.out);
            }
        }

        this.capacity = capacity;
        this.byteBuffer = ByteBuffer.allocateDirect((this.capacity <= 0) ?
                DEFAULT_CAPACITY : this.capacity);
        lock.unlock();
    }

    /** @noinspection DataFlowIssue*/
    @SuppressLint("ObsoleteSdkInt")
    private void releaseDirectBuffer(@Nullable ByteBuffer buffer) throws Throwable {
        if (android.os.Build.VERSION_CODES.M < android.os.Build.VERSION.SDK_INT) {
            // Android 24 起 DirectByteBuffer 将使用 Cleaner 来释放资源
            Method method = buffer.getClass().getDeclaredMethod("cleaner");
            method.setAccessible(true);
            Object cleaner = method.invoke(buffer);
            Method clean = cleaner.getClass().getDeclaredMethod("clean");
            clean.setAccessible(true);
            clean.invoke(cleaner);
        } else {
            // Android 24 之前的版本通过调用 DirectByteBuffer 的 free 方法释放资源
            Method method = buffer.getClass().getDeclaredMethod("free");
            method.setAccessible(true);
            method.invoke(buffer);
        }
    }
}
