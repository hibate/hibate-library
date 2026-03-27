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
import org.hibate.media.codec.AVCodecID;
import org.hibate.media.codec.AVMediaType;
import org.hibate.media.codec.AVCodecParameters;
import org.hibate.media.util.*;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
class AVCodecParametersImpl implements AVCodecParameters {

    /**
     * AVCodecParameters
     */
    private long ctx;

    @Override
    public AVCodecParameters setCodecContext(@NonNull AVCodecContext context) {
        this._setCodecContext(context);
        return this;
    }

    @Override
    public AVMediaType getMediaType() {
        return AVMediaType.of(this._getMediaType());
    }

    @Override
    public AVCodecParameters setMediaType(AVMediaType mediaType) {
        if (mediaType != null) {
            this._setMediaType(mediaType.getMediaType());
        }
        return this;
    }

    @Override
    public AVCodecID getCodecId() {
        return AVCodecID.of(this._getCodecId());
    }

    @Override
    public AVCodecParameters setCodecId(AVCodecID id) {
        if (id != null) {
            this._setCodecId(id.getName());
        }
        return this;
    }

    @Override
    public int getCodecTag() {
        return this._getCodecTag();
    }

    @Override
    public AVCodecParameters setCodecTag(int codecTag) {
        this._setCodecTag(codecTag);
        return this;
    }

    @Override
    public int getExtraDataSize() {
        return this._getExtraDataSize();
    }

    @Override
    public AVPixelFormat getPixelFormat() {
        return (AVMediaType.AVMEDIA_TYPE_VIDEO == this.getMediaType()) ?
                AVPixelFormat.of(this._getFormat()) : AVPixelFormat.AV_PIX_FMT_NONE;
    }

    @Override
    public AVCodecParameters setPixelFormat(AVPixelFormat format) {
        if (format != null) {
            this._setFormat(format.getName());
        }
        return this;
    }

    @Override
    public AVSampleFormat getSampleFormat() {
        return (AVMediaType.AVMEDIA_TYPE_AUDIO == this.getMediaType()) ?
                AVSampleFormat.of(this._getFormat()) : AVSampleFormat.AV_SAMPLE_FMT_NONE;
    }

    @Override
    public AVCodecParameters setSampleFormat(AVSampleFormat format) {
        if (format != null) {
            this._setFormat(format.getName());
        }
        return this;
    }

    @Override
    public native long getBitRate();

    @Override
    public AVCodecParameters setBitRate(long bitRate) {
        this._setBitRate(bitRate);
        return this;
    }

    @Override
    public native int getWidth();

    @Override
    public AVCodecParameters setWidth(int width) {
        this._setWidth(width);
        return this;
    }

    @Override
    public native int getHeight();

    @Override
    public AVCodecParameters setHeight(int height) {
        this._setHeight(height);
        return this;
    }

    @Override
    public native AVRational getAspectRatio();

    @Override
    public AVCodecParameters setAspectRatio(AVRational aspectRatio) {
        this._setAspectRatio(aspectRatio);
        return this;
    }

    @Override
    public native int getSampleRate();

    @Override
    public AVCodecParameters setSampleRate(int sampleRate) {
        this._setSampleRate(sampleRate);
        return this;
    }

    @Override
    public native AVChannelLayout getChannelLayout();

    @Override
    public AVCodecParameters setChannelLayout(AVChannelLayout channels) {
        this._setChannelLayout(channels);
        return this;
    }

    private native int _setCodecContext(AVCodecContext context);
    private native int _getMediaType();
    private native int _setMediaType(int mediaType);
    @Nullable
    private native String _getCodecId();
    private native int _setCodecId(String name);
    private native int _getCodecTag();
    private native int _setCodecTag(int codecTag);
    private native int _getExtraDataSize();
    @Nullable
    private native String _getFormat();
    private native int _setFormat(String format);
    private native int _setBitRate(long bitRate);
    private native int _setWidth(int width);
    private native int _setHeight(int height);
    private native int _setAspectRatio(AVRational aspectRatio);
    private native int _setSampleRate(int sampleRate);
    private native int _setChannelLayout(AVChannelLayout channels);
}
