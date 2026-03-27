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
import org.hibate.media.filter.AVFilterGraph;
import org.hibate.media.filter.AVFilterInOut;
import org.hibate.media.util.AVUtils;

/**
 * @author Hibate
 * Created on 2026/03/21.
 */
public class AVFilterGraphImpl implements AVFilterGraph {

    static {
        AVUtils.LOADER.isAvailable();
    }

    /**
     * AVFilterInOut
     */
    private long ctx;

    public AVFilterGraphImpl() {
        this._initialize();
    }

    @Override
    public AVFilterGraph setDescription(@NonNull String specs, @Nullable AVFilterInOut input,
                                        @Nullable AVFilterInOut output) {
        this._setDescription(specs, input, output);
        return this;
    }

    @Override
    public AVFilterGraph prepare() {
        this._prepare();
        return this;
    }

    @Override
    public native void close();

    private native void _initialize();
    private native int _setDescription(@NonNull String specs, @Nullable AVFilterInOut input,
                                       @Nullable AVFilterInOut output);
    private native int _prepare();
}
