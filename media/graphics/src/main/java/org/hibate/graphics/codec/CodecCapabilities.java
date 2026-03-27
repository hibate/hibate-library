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

import android.media.Image;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.hibate.graphics.PixelFormat;
import org.hibate.graphics.Rotation;

/**
 * @author Hibate
 * Created on 2018/04/02.
 */
public interface CodecCapabilities {

    /**
     * 像素空间转换
     * @param source 数据源
     * @param size 大小
     * @param from 数据源像素空间
     * @param to 目标像素空间
     * @return 返回转换后的数据
     */
    default byte[] toPixelFormat(@NonNull byte[] source, @NonNull Size size,
                                 @NonNull PixelFormat from, @NonNull PixelFormat to) {
        return this.toPixelFormat(source, size, from, to, null);
    }

    /**
     * 像素空间转换
     * @param source 数据源
     * @param size 大小
     * @param from 数据源像素空间
     * @param to 目标像素空间
     * @param rotation 旋转角度
     * @return 返回转换后的数据
     */
    default byte[] toPixelFormat(@NonNull byte[] source, @NonNull Size size,
                                 @NonNull PixelFormat from, @NonNull PixelFormat to,
                                 @Nullable Rotation rotation) {
        return this.toPixelFormat(source, size, from, to, rotation, false);
    }

    /**
     * 像素空间转换
     * @param source 数据源
     * @param size 大小
     * @param from 数据源像素空间
     * @param to 目标像素空间
     * @param rotation 旋转角度
     * @param mirror 镜像
     * @return 返回转换后的数据
     */
    byte[] toPixelFormat(@NonNull byte[] source, @NonNull Size size, @NonNull PixelFormat from,
                         @NonNull PixelFormat to, @Nullable Rotation rotation, boolean mirror);

    /**
     * 像素空间转换
     * @param image 数据源
     * @param to 目标像素空间
     * @return 返回转换后的数据
     */
    default byte[] toPixelFormat(@NonNull Image image, @NonNull PixelFormat to) {
        return this.toPixelFormat(image, to, null, false);
    }

    /**
     * 像素空间转换
     * @param image 数据源
     * @param to 目标像素空间
     * @param rotation 旋转角度
     * @return 返回转换后的数据
     */
    default byte[] toPixelFormat(@NonNull Image image, @NonNull PixelFormat to,
                                 @Nullable Rotation rotation) {
        return this.toPixelFormat(image, to, rotation, false);
    }

    /**
     * 像素空间转换
     * @param image 数据源
     * @param to 目标像素空间
     * @param rotation 旋转角度
     * @param mirror 镜像
     * @return 返回转换后的数据
     */
    byte[] toPixelFormat(@NonNull Image image, @NonNull PixelFormat to,
                         @Nullable Rotation rotation, boolean mirror);
}
