/*
 * Copyright (C) 2018 Hibate <ycaia86@126.com>
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

package org.hibate.graphics.codec;

import android.graphics.ImageFormat;
import android.media.Image;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.hibate.graphics.PixelFormat;
import org.hibate.graphics.Rotation;
import org.hibate.logger.natives.LibraryLoader;

import java.nio.ByteBuffer;

/**
 * @author Hibate
 * Created on 2018/04/02.
 */
class CodecCapabilitiesImpl implements CodecCapabilities {

    public static final LibraryLoader LOADER =
            new LibraryLoader("yuv-jni");

    static {
        LOADER.isAvailable();
    }

    public CodecCapabilitiesImpl() {
    }

    @Override
    public byte[] toPixelFormat(@NonNull byte[] source, @NonNull Size size,
                                @NonNull PixelFormat from, @NonNull PixelFormat to,
                                @Nullable Rotation rotation, boolean mirror) {
        if (size.getWidth() <= 0 || size.getHeight() <= 0) {
            throw new IllegalArgumentException("The data size must be greater than zero");
        }
        rotation = (rotation == null) ? Rotation.ROTATION_0 : rotation;
        return this._toPixelFormat(source, size.getWidth(), size.getHeight(),
                from.getCodes(), to.getCodes(), rotation.getDegrees(), mirror);
    }

    @Override
    public byte[] toPixelFormat(@NonNull Image image, @NonNull PixelFormat to,
                                @Nullable Rotation rotation, boolean mirror) {
        if (image.getFormat() != ImageFormat.YUV_420_888) {
            throw new IllegalArgumentException("The image format only support YUV_420_888");
        }
        if (image.getWidth() <= 0 || image.getHeight() <= 0) {
            throw new IllegalArgumentException("The data size must be greater than zero");
        }
        rotation = (rotation == null) ? Rotation.ROTATION_0 : rotation;
        return this._toPixelFormat(image.getPlanes()[0].getBuffer(),
                image.getPlanes()[0].getRowStride(),
                image.getPlanes()[1].getBuffer(), image.getPlanes()[2].getBuffer(),
                image.getPlanes()[1].getRowStride(), image.getPlanes()[1].getPixelStride(),
                image.getWidth(), image.getHeight(), to.getCodes(), rotation.getDegrees(), mirror);
    }

    private native byte[] _toPixelFormat(byte[] source, int width, int height, int from, int to,
                                         int rotation, boolean mirror);
    private native byte[] _toPixelFormat(ByteBuffer yBuffer, int yStride, ByteBuffer uBuffer,
                                         ByteBuffer vBuffer, int uvRowStride, int uvPixelStride,
                                         int width, int height, int to, int rotation, boolean mirror);
}
