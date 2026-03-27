/*
 * Copyright (C) 2024 Hibate <ycaia86@126.com>
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

package org.hibate.media.codec.impl;

import androidx.annotation.NonNull;
import org.hibate.media.codec.AVPacket;
import org.hibate.media.util.AVRational;
import org.hibate.media.util.AVUtils;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
public class AVPacketImpl implements AVPacket {

    static {
        AVUtils.LOADER.isAvailable();
    }

    /**
     * AVPacket
     */
    private long ctx;

    public AVPacketImpl() {
        this(0);
    }

    /**
     * 初始化容量, capacity <= 0 时不分配
     * @param capacity 容量
     */
    public AVPacketImpl(int capacity) {
        this._initialize(capacity);
    }

    @Override
    public native int getIndex();

    @Override
    public AVPacket setIndex(int index) {
        this._setIndex(index);
        return this;
    }

    @Override
    public native long getPts();

    @Override
    public native long getDts();

    @Override
    public native int getSize();

    @Override
    public native int getFlags();

    @Override
    public native long getDuration();

    @Override
    public native long getPosition();

    @Override
    public AVPacket setPosition(long position) {
        this._setPosition(position);
        return this;
    }

    @Override
    public AVPacket rescale(@NonNull AVRational src, @NonNull AVRational dst) {
        this._rescale(src, dst);
        return this;
    }

    @Override
    public AVPacket unref() {
        this._unref();
        return this;
    }

    @Override
    public native void close();

    private native void _initialize(int capacity);
    private native int _setIndex(int index);
    private native int _setPosition(long position);
    private native int _rescale(AVRational src, AVRational dst);
    private native int _unref();
}
