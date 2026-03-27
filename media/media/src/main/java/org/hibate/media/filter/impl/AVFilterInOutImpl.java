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

import org.hibate.media.filter.AVFilterContext;
import org.hibate.media.filter.AVFilterInOut;
import org.hibate.media.util.AVUtils;

/**
 * @author Hibate
 * Created on 2026/03/21.
 */
public class AVFilterInOutImpl implements AVFilterInOut {

    static {
        AVUtils.LOADER.isAvailable();
    }

    /**
     * AVFilterInOut
     */
    private long ctx;

    public AVFilterInOutImpl() {
        this._initialize();
    }

    /**
     * Called by native
     */
    private AVFilterInOutImpl(int ctx) {}

    @Override
    public native String getName();

    @Override
    public AVFilterInOut setName(String name) {
        this._setName(name);
        return this;
    }

    @Override
    public native AVFilterContext getFilterContext();

    @Override
    public AVFilterInOut setFilterContext(AVFilterContext context) {
        this._setFilterContext(context);
        return this;
    }

    @Override
    public native int getIndex();

    @Override
    public AVFilterInOut setIndex(int index) {
        this._setIndex(index);
        return this;
    }

    @Override
    public native AVFilterInOut getNext();

    @Override
    public AVFilterInOut setNext(AVFilterInOut filter) {
        this._setNext(filter);
        return this;
    }

    @Override
    public native void close();

    private native void _initialize();
    private native int _setName(String name);
    private native int _setFilterContext(AVFilterContext context);
    private native int _setIndex(int index);
    private native int _setNext(AVFilterInOut filter);
}
