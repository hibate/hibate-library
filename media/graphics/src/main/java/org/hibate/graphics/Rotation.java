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

package org.hibate.graphics;

import android.view.Surface;

import androidx.annotation.NonNull;

/**
 * @author Hibate
 * Created on 2018/04/02.
 */
public enum Rotation {

    ROTATION_0(Surface.ROTATION_0, 0),
    ROTATION_90(Surface.ROTATION_90, 90),
    ROTATION_180(Surface.ROTATION_180, 180),
    ROTATION_270(Surface.ROTATION_270, 270),
    ;

    private final int rotation;
    private final int degrees;

    Rotation(int rotation, int degrees) {
        this.rotation = rotation;
        this.degrees = degrees;
    }

    public int getRotation() {
        return this.rotation;
    }

    public int getDegrees() {
        return degrees;
    }

    @NonNull
    public static Rotation of(int rotation) {
        for (Rotation v : Rotation.values()) {
            if (v.getRotation() == rotation) {
                return v;
            }
        }
        return Rotation.ROTATION_0;
    }
}
