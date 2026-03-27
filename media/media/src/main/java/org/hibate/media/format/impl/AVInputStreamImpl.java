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

package org.hibate.media.format.impl;

import androidx.annotation.NonNull;
import org.hibate.media.codec.AVPacket;
import org.hibate.media.format.AVFormatContext;
import org.hibate.media.format.AVInputStream;
import org.hibate.media.util.AVUtils;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
public class AVInputStreamImpl implements AVInputStream {

    static {
        AVUtils.LOADER.isAvailable();
    }

    private final AVFormatContext context;

    public AVInputStreamImpl(@NonNull AVFormatContext context) {
        this.context = context;
    }

    @Override
    public int read(@NonNull AVPacket packet) {
        return this._read(this.context, packet);
    }

    private native int _read(AVFormatContext context, AVPacket packet);
}
