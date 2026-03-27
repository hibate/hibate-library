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
import org.hibate.media.format.AVFormat;
import org.hibate.media.format.AVFormatContext;
import org.hibate.media.util.AVDictionary;
import org.hibate.media.util.AVUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
public class AVFormatImpl implements AVFormat {

    static {
        AVUtils.LOADER.isAvailable();
    }

    private final int mode;

    public AVFormatImpl() {
        this(INPUT);
    }

    public AVFormatImpl(@Mode int mode) {
        this.mode = mode;
    }

    @NonNull
    @Override
    public AVFormatContext open(@Nullable File url) throws IOException, RuntimeException {
        return this.open(url, null);
    }

    @NonNull
    @Override
    public AVFormatContext open(@Nullable File url, @Nullable AVDictionary dictionary) throws IOException, RuntimeException {
        if (url == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if ((INPUT == this.mode) && !url.exists()) {
            throw new FileNotFoundException("File not found");
        }
        return this.open(url.getAbsolutePath(), dictionary);
    }

    @NonNull
    @Override
    public AVFormatContext open(@Nullable String url) throws IOException, RuntimeException {
        return this.open(url, null);
    }

    @NonNull
    @Override
    public AVFormatContext open(@Nullable String url, @Nullable AVDictionary dictionary) throws IOException, RuntimeException {
        if (url == null) {
            throw new IllegalArgumentException("Url cannot be null");
        }
        return this._open(url, dictionary, this.mode);
    }

    private native AVFormatContext _open(@NonNull String url, @Nullable AVDictionary dictionary, int mode) throws IOException, RuntimeException;
}
