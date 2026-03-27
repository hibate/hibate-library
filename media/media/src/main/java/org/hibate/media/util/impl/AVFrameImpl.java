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

package org.hibate.media.util.impl;

import androidx.annotation.NonNull;
import org.hibate.media.util.*;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
public class AVFrameImpl implements AVFrame {

    static {
        AVUtils.LOADER.isAvailable();
    }

    /**
     * AVFrame
     */
    private long ctx;

    public AVFrameImpl() {
        this._initialize();
    }

    @Override
    public native int getWidth();

    @Override
    public native int getHeight();

    @Override
    public native int getFlags();

    @Override
    public native int getFormat();

    @NonNull
    @Override
    public AVPictureType getPictureType() {
        return AVPictureType.of(this._getPictureType());
    }

    @Override
    public AVFrame setPictureType(AVPictureType pictureType) {
        if (pictureType != null) {
            this._setPictureType(pictureType.getPictureType());
        }
        return this;
    }

    @Override
    public native long getPts();

    @Override
    public AVFrame setPts(long pts) {
        this._setPts(pts);
        return this;
    }

    @Override
    public native long getDts();

    @Override
    public native AVRational getTimebase();

    @Override
    public AVFrame setTimebase(AVRational rational) {
        this._setTimebase(rational);
        return this;
    }

    @Override
    public native int getQuality();

    @Override
    public native int getSampleRate();

    @Override
    public native long getBestEffortTimestamp();

    @Override
    public native AVChannelLayout getChannelLayout();

    @Override
    public native long getDuration();

    @Override
    public AVFrame unref() {
        this._unref();
        return this;
    }

    @Override
    public native void close();

    private native void _initialize();
    private native int _getPictureType();
    private native int _setPictureType(int pictureType);
    private native int _setPts(long pts);
    private native int _setTimebase(AVRational rational);
    private native int _unref();
}
