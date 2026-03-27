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
import org.hibate.media.util.*;

import java.util.Optional;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
class AVCodecContextImpl implements AVCodecContext {

    /**
     * AVCodecContext
     */
    private long ctx;
    private AVCodec codec;

    @NonNull
    @Override
    public AVCodec getCodec() {
        return this.codec;
    }

    @Override
    public AVCodecContext setCodecParameters(@NonNull AVCodecParameters parameters) {
        this._setCodecParameters(parameters);
        return this;
    }

    @Override
    public AVMediaType getMediaType() {
        return AVMediaType.of(this._getMediaType());
    }

    @Override
    public AVCodecID getCodecId() {
        return AVCodecID.of(this._getCodecId());
    }

    @Override
    public int getCodecTag() {
        return this._getCodecTag();
    }

    @Override
    public AVCodecContext setCodecTag(int codecTag) {
        this._setCodecTag(codecTag);
        return this;
    }

    @Override
    public native AVRational getTimebase();

    @Override
    public AVCodecContext setTimebase(AVRational rational) {
        this._setTimebase(rational);
        return this;
    }

    @Override
    public native AVRational getPktTimebase();

    @Override
    public AVCodecContext setPktTimebase(AVRational rational) {
        this._setPktTimebase(rational);
        return this;
    }

    @Override
    public native AVRational getFrameRate();

    @Override
    public AVCodecContext setFrameRate(AVRational frameRate) {
        this._setFrameRate(frameRate);
        return this;
    }

    @Override
    public native long getBitRate();

    @Override
    public AVCodecContext setBitRate(long bitRate) {
        this._setBitRate(bitRate);
        return this;
    }

    @Override
    public native int getWidth();

    @Override
    public AVCodecContext setWidth(int width) {
        this._setWidth(width);
        return this;
    }

    @Override
    public native int getHeight();

    @Override
    public AVCodecContext setHeight(int height) {
        this._setHeight(height);
        return this;
    }

    @Override
    public native AVRational getAspectRatio();

    @Override
    public AVCodecContext setAspectRatio(AVRational aspectRatio) {
        this._setAspectRatio(aspectRatio);
        return this;
    }

    @Override
    public AVPixelFormat getPixelFormat() {
        return AVPixelFormat.of(this._getPixelFormat());
    }

    @Override
    public AVCodecContext setPixelFormat(AVPixelFormat format) {
        if (format != null) {
            this._setPixelFormat(format.getName());
        }
        return this;
    }

    @Override
    public native int getSampleRate();

    @Override
    public AVCodecContext setSampleRate(int sampleRate) {
        this._setSampleRate(sampleRate);
        return this;
    }

    @Override
    public AVSampleFormat getSampleFormat() {
        return AVSampleFormat.of(this._getSampleFormat());
    }

    @Override
    public AVCodecContext setSampleFormat(AVSampleFormat format) {
        if (format != null) {
            this._setSampleFormat(format.getName());
        }
        return this;
    }

    @Override
    public native AVChannelLayout getChannelLayout();

    @Override
    public AVCodecContext setChannelLayout(AVChannelLayout channels) {
        this._setChannelLayout(channels);
        return this;
    }

    @Override
    public native int getFrameSize();

    @Override
    public native int getFlags();

    @Override
    public AVCodecContext setFlags(int flags) {
        this._setFlags(flags);
        return this;
    }

    @Override
    public <T extends AVCoder> Optional<T> open() {
        return this.open(null);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends AVCoder> Optional<T> open(@Nullable AVDictionary dictionary) {
        return (Optional<T>) Optional.ofNullable(this._open(dictionary));
    }

    @Override
    public native void close();

    private native int _setCodecParameters(AVCodecParameters parameters);
    private native int _getMediaType();
    @Nullable
    private native String _getCodecId();
    private native int _getCodecTag();
    private native int _setCodecTag(int codecTag);
    private native int _setTimebase(AVRational rational);
    private native int _setPktTimebase(AVRational rational);
    private native int _setFrameRate(AVRational frameRate);
    private native int _setBitRate(long bitRate);
    private native int _setWidth(int width);
    private native int _setHeight(int height);
    private native int _setAspectRatio(AVRational aspectRatio);
    @Nullable
    private native String _getPixelFormat();
    private native int _setPixelFormat(String name);
    private native int _setSampleRate(int sampleRate);
    @Nullable
    private native String _getSampleFormat();
    private native int _setSampleFormat(String format);
    private native int _setChannelLayout(AVChannelLayout channels);
    private native int _setFlags(int flags);
    private native AVCoder _open(@Nullable AVDictionary dictionary);
}
