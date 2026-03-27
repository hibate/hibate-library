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

import org.hibate.media.util.AVLogger;
import org.hibate.media.util.AVUtils;

/**
 * @author Hibate
 * Created on 2026/03/20.
 */
public class AVLoggerImpl implements AVLogger {

    static {
        AVUtils.LOADER.isAvailable();
    }

    @Override
    public native int getPriority();

    @Override
    public AVLogger setPriority(int priority) {
        this._setPriority(priority);
        return this;
    }

    @Override
    public native int getFlags();

    @Override
    public AVLogger setFlags(int flags) {
        this._setFlags(flags);
        return this;
    }

    private native int _setPriority(int priority);
    private native int _setFlags(int flags);
}
