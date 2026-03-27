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

import androidx.annotation.Nullable;
import org.hibate.media.codec.AVCodecID;
import org.hibate.media.format.AVOutputFormat;

/**
 * @author Hibate
 * Created on 2024/03/28.
 */
class AVOutputFormatImpl implements AVOutputFormat {

    /**
     * AVFormatContext
     */
    private long ctx;

    @Override
    public native String getName();

    @Override
    public native String getLongName();

    @Override
    public native String getMimeType();

    @Override
    public AVCodecID getAudioCodecId() {
        return AVCodecID.of(this._getAudioCodecId());
    }

    @Override
    public AVCodecID getVideoCodecId() {
        return AVCodecID.of(this._getVideoCodecId());
    }

    @Override
    public AVCodecID getSubtitleCodecId() {
        return AVCodecID.of(this._getSubtitleCodecId());
    }

    @Override
    public native int getFlags();

    @Nullable
    private native String _getAudioCodecId();
    @Nullable
    private native String _getVideoCodecId();
    @Nullable
    private native String _getSubtitleCodecId();
}
