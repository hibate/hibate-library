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
import org.hibate.media.codec.AVCodec;
import org.hibate.media.codec.AVCodecFactory;
import org.hibate.media.codec.AVCodecID;
import org.hibate.media.codec.AVCodecType;
import org.hibate.media.util.AVUtils;

import java.util.Optional;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
public class AVCodecFactoryImpl implements AVCodecFactory {

    static {
        AVUtils.LOADER.isAvailable();
    }

    @NonNull
    @Override
    public Optional<AVCodec> getAVCodec(@NonNull AVCodecID id, @NonNull AVCodecType type) {
        if (AVCodecType.AVCODEC_TYPE_ENCODER == type) {
            return this.getEncoder(id);
        } else if (AVCodecType.AVCODEC_TYPE_DECODER == type) {
            return this.getDecoder(id);
        }
        return Optional.empty();
    }

    @NonNull
    @Override
    public Optional<AVCodec> getEncoder(@Nullable AVCodecID id) {
        return (id == null) ? Optional.empty() : Optional.ofNullable(this._getEncoder(id.getName()));
    }

    @NonNull
    @Override
    public Optional<AVCodec> getDecoder(@Nullable AVCodecID id) {
        return (id == null) ? Optional.empty() : Optional.ofNullable(this._getDecoder(id.getName()));
    }

    private native AVCodec _getEncoder(String name);
    private native AVCodec _getDecoder(String name);
}
