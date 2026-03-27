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

package org.hibate.media.filter.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.hibate.media.filter.AVFilter;
import org.hibate.media.filter.AVFilterContext;
import org.hibate.media.filter.AVFilterGraph;
import org.hibate.media.util.*;

/**
 * @author Hibate
 * Created on 2026/03/22.
 */
public class AVFilterContextImpl implements AVFilterContext {

    /**
     * AVFilterContext
     */
    private long ctx;

    /**
     * Called by native
     */
    private AVFilterContextImpl() {
    }

    public AVFilterContextImpl(@NonNull AVFilterGraph graph, @NonNull AVFilter filter) {
        this(graph, filter, null);
    }

    public AVFilterContextImpl(@NonNull AVFilterGraph graph, @NonNull AVFilter filter, @Nullable String name) {
        this._initialize(graph, filter, name);
    }

    @Override
    public native String getName();

    @Override
    public native AVRational getTimebase();

    @Override
    public AVFilterContext setDictionary(@Nullable AVDictionary dictionary) {
        int value = this._setDictionary(dictionary);
        if (value < 0) {
            throw new IllegalArgumentException("Cannot initialize AVFilterContext: " + value);
        }
        return this;
    }

    @Override
    public AVFilterContext setFrameSize(int frameSize) {
        this._setFrameSize(frameSize);
        return this;
    }

    @Override
    public int send(AVFrame frame) {
        return this.send(frame, 0);
    }

    @Override
    public native int send(AVFrame frame, int flags);

    @Override
    public native int receive(@NonNull AVFrame frame);

    @Override
    public native void close();

    private native void _initialize(@NonNull AVFilterGraph graph, @NonNull AVFilter filter, @Nullable String name);
    private native int _setDictionary(@Nullable AVDictionary dictionary);
    private native int _setFrameSize(int frameSize);
}
