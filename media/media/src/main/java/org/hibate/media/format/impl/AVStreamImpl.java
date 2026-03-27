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

import org.hibate.media.codec.AVCodecParameters;
import org.hibate.media.format.AVFormatContext;
import org.hibate.media.format.AVStream;
import org.hibate.media.util.AVRational;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
class AVStreamImpl implements AVStream {

    /**
     * AVStream
     */
    private long ctx;

    @Override
    public native int getIndex();

    @Override
    public native AVRational getTimebase();

    @Override
    public AVStream setTimebase(AVRational rational) {
        this._setTimebase(rational);
        return this;
    }

    @Override
    public native long getStartTime();

    @Override
    public native long getDuration();

    @Override
    public native long getFrames();

    @Override
    public AVRational getFrameRate() {
        return this.getFrameRate(null);
    }

    @Override
    public native AVRational getFrameRate(AVFormatContext context);

    @Override
    public native AVCodecParameters getCodecParameters();

    @Override
    public AVStream setCodecParameters(AVCodecParameters parameters) {
        this._setCodecParameters(parameters);
        return this;
    }

    private native int _setTimebase(AVRational rational);
    private native int _setCodecParameters(AVCodecParameters parameters);
}
