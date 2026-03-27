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
import org.hibate.media.codec.AVPacket;
import org.hibate.media.format.AVFormatContext;
import org.hibate.media.format.AVOutputStream;
import org.hibate.media.util.AVDictionary;
import org.hibate.media.util.AVUtils;

/**
 * @author Hibate
 * Created on 2024/03/29.
 */
public class AVOutputStreamImpl implements AVOutputStream {

    static {
        AVUtils.LOADER.isAvailable();
    }

    private final AVFormatContext context;
    private volatile State state;

    public AVOutputStreamImpl(@NonNull AVFormatContext context) {
        this(context, AVIO_FLAG_WRITE);
    }

    public AVOutputStreamImpl(@NonNull AVFormatContext context, @Flags int flags) {
        this.context = context;
        this.state = State.NEW;
        this._open(context, flags);
    }

    @Override
    public int write() {
        return this.write((AVDictionary) null);
    }

    @Override
    public int write(@Nullable AVDictionary dictionary) {
        State state = this.getState();
        if (state == State.HEADER) {
            return 0;
        }
        int value = this._write(this.context, dictionary);
        if (value >= 0) {
            this.setState(State.HEADER);
        }
        return value;
    }

    @Override
    public int write(@NonNull AVPacket packet) {
        return this.write(packet, false);
    }

    @Override
    public int write(@NonNull AVPacket packet, boolean interleaved) {
        State state = this.getState();
        if (state == State.HEADER) {
            return this._write(this.context, packet, interleaved);
        }
        return -1;
    }

    @Override
    public void close() {
        State state = this.getState();
        if (state == State.HEADER) {
            this._write(this.context);
            this._close(this.context);
        }
        this.setState(State.TRAILER);
    }

    private State getState() {
        return state;
    }

    private void setState(State state) {
        this.state = state;
    }

    private native int _open(AVFormatContext context, int flags);
    private native int _close(AVFormatContext context);
    private native int _write(AVFormatContext context, AVDictionary dictionary);
    private native int _write(AVFormatContext context, AVPacket packet, boolean interleaved);
    private native int _write(AVFormatContext context);

    private enum State {
        NEW,
        HEADER,
        TRAILER,
    }
}
