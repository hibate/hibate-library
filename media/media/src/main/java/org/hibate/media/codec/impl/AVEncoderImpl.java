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
import androidx.annotation.Nullable;
import org.hibate.media.codec.AVCodecContext;
import org.hibate.media.codec.AVEncoder;
import org.hibate.media.codec.AVPacket;
import org.hibate.media.util.AVFrame;

/**
 * @author Hibate
 * Created on 2024/03/28.
 */
class AVEncoderImpl implements AVEncoder {

    /**
     * AVCodecContext
     */
    private long ctx;
    private AVCodecContext codecContext;

    @NonNull
    @Override
    public AVCodecContext getCodecContext() {
        return this.codecContext;
    }

    @Override
    public native int send(@Nullable AVFrame frame);

    @Override
    public native int receive(@NonNull AVPacket packet);

    @Override
    public native int flush();
}
