/*
 * Copyright (C) 2026 Hibate <ycaia86@126.com>
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

package org.hibate.media.util.impl;

import androidx.annotation.NonNull;
import org.hibate.media.util.*;

/**
 * @author Hibate
 * Created on 2026/03/22.
 */
public class AVOptionsImpl implements AVOptions {

    /**
     * AVClass
     */
    private long ctx;

    public AVOptionsImpl(@NonNull Object object) {
        this._initialize(object);
    }

    @Override
    public void put(String name, String value, int flags) {
        this._put(name, value, flags);
    }

    @Override
    public void put(String name, long value, int flags) {
        this._put(name, value, flags);
    }

    @Override
    public void put(String name, double value, int flags) {
        this._put(name, value, flags);
    }

    @Override
    public void put(String name, AVRational value, int flags) {
        this._put(name, value, flags);
    }

    @Override
    public void put(String name, byte[] value, int flags) {
        this._put(name, value, flags);
    }

    @Override
    public void putImageSize(String name, int width, int height, int flags) {
        this._putImageSize(name, width, height, flags);
    }

    @Override
    public void putPixelFormat(String name, AVPixelFormat format, int flags) {
        if (format != null) {
            this._putPixelFormat(name, format.getName(), flags);
        }
    }

    @Override
    public void putSampleFormat(String name, AVSampleFormat format, int flags) {
        if (format != null) {
            this._putSampleFormat(name, format.getName(), flags);
        }
    }

    @Override
    public void putVideoRate(String name, AVRational value, int flags) {
        this._putVideoRate(name, value, flags);
    }

    @Override
    public void putChannelLayout(String name, AVChannelLayout value, int flags) {
        this._putChannelLayout(name, value, flags);
    }

    @Override
    public void put(String name, AVDictionary value, int flags) {
        this._put(name, value, flags);
    }

    private native void _initialize(@NonNull Object object);
    private native int _put(String name, String value, int flags);
    private native int _put(String name, long value, int flags);
    private native int _put(String name, double value, int flags);
    private native int _put(String name, AVRational value, int flags);
    private native int _put(String name, byte[] value, int flags);
    private native int _putImageSize(String name, int width, int height, int flags);
    private native int _putPixelFormat(String name, String fmt, int flags);
    private native int _putSampleFormat(String name, String fmt, int flags);
    private native int _putVideoRate(String name, AVRational value, int flags);
    private native int _putChannelLayout(String name, AVChannelLayout value, int flags);
    private native int _put(String name, AVDictionary value, int flags);
}
