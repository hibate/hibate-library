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
import androidx.annotation.Nullable;
import org.hibate.media.format.AVFormatContext;
import org.hibate.media.format.AVInputFormat;
import org.hibate.media.format.AVOutputFormat;
import org.hibate.media.format.AVStream;

import java.util.Optional;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
class AVFormatContextImpl implements AVFormatContext {

    /**
     * AVFormatContext
     */
    private long ctx;
    @Nullable
    private AVInputFormat inputFormat;
    @Nullable
    private AVOutputFormat outputFormat;

    @NonNull
    @Override
    public Optional<AVInputFormat> getInputFormat() {
        return Optional.ofNullable(this.inputFormat);
    }

    @NonNull
    @Override
    public Optional<AVOutputFormat> getOutputFormat() {
        return Optional.ofNullable(this.outputFormat);
    }

    @Override
    public native int getStreams();

    @NonNull
    @Override
    public native AVStream getStream(int index) throws IllegalArgumentException;

    @Override
    public Optional<AVStream> addStream() {
        return Optional.ofNullable(this._addStream());
    }

    @Override
    public native String getUrl();

    @Override
    public native long getStartTime();

    @Override
    public native long getDuration();

    @Override
    public native long getBitRate();

    @Override
    public AVFormatContext setAvoidNegativeTs(int flags) {
        this._setAvoidNegativeTs(flags);
        return this;
    }

    @Override
    public AVFormatContext print() {
        return this.print(0);
    }

    @Override
    public AVFormatContext print(int index) {
        this._print(index);
        return this;
    }

    @Override
    public native void close();

    private native AVStream _addStream();
    private native int _setAvoidNegativeTs(int flags);
    private native int _print(int index);
}
