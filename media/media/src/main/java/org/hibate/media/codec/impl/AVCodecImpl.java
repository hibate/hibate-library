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
import org.hibate.media.codec.*;
import org.hibate.media.util.AVPixelFormat;
import org.hibate.media.util.AVSampleFormat;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
class AVCodecImpl implements AVCodec {

    /**
     * AVCodec
     */
    private long ctx;

    @NonNull
    @Override
    public AVCodecID getCodecId() {
        return AVCodecID.of(this._getCodecId());
    }

    @NonNull
    @Override
    public native String getCodecName();

    @NonNull
    @Override
    public AVCodecType getCodecType() {
        return this._isEncoder() ? AVCodecType.AVCODEC_TYPE_ENCODER : AVCodecType.AVCODEC_TYPE_DECODER;
    }

    @NonNull
    @Override
    public AVMediaType getMediaType() {
        return AVMediaType.of(this._getMediaType());
    }

    @Override
    public native int getCapabilities();

    @NonNull
    @Override
    public AVPixelFormat[] getPixelFormats() {
        String[] pixels = this._getPixelFormats();
        if ((pixels == null) || (pixels.length == 0)) {
            return new AVPixelFormat[0];
        }
        AVPixelFormat[] formats = new AVPixelFormat[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            formats[i] = AVPixelFormat.of(pixels[i]);
        }
        return formats;
    }

    @NonNull
    @Override
    public AVSampleFormat[] getSampleFormats() {
        String[] samples = this._getSampleFormats();
        if ((samples == null) || (samples.length == 0)) {
            return new AVSampleFormat[0];
        }
        AVSampleFormat[] formats = new AVSampleFormat[samples.length];
        for (int i = 0; i < samples.length; i++) {
            formats[i] = AVSampleFormat.of(samples[i]);
        }
        return formats;
    }

    @NonNull
    @Override
    public native AVCodecContext getCodecContext();

    @Nullable
    private native String _getCodecId();
    private native int _getMediaType();
    private native boolean _isEncoder();
    @Nullable
    private native String[] _getPixelFormats();
    private native String[] _getSampleFormats();
}
