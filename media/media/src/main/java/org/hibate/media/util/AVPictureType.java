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

package org.hibate.media.util;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
public enum AVPictureType {

    AV_PICTURE_TYPE_NONE(0),  ///< Undefined
    AV_PICTURE_TYPE_I(1),     ///< Intra
    AV_PICTURE_TYPE_P(2),     ///< Predicted
    AV_PICTURE_TYPE_B(3),     ///< Bi-dir predicted
    AV_PICTURE_TYPE_S(4),     ///< S(GMC)-VOP MPEG-4
    AV_PICTURE_TYPE_SI(5),    ///< Switching Intra
    AV_PICTURE_TYPE_SP(6),    ///< Switching Predicted
    AV_PICTURE_TYPE_BI(7),    ///< BI type

    ;

    private final int pictureType;

    AVPictureType(int pictureType) {
        this.pictureType = pictureType;
    }

    public int getPictureType() {
        return pictureType;
    }

    public static AVPictureType of(int pictureType) {
        for (AVPictureType type : AVPictureType.values()) {
            if (type.getPictureType() == pictureType) {
                return type;
            }
        }
        return AVPictureType.AV_PICTURE_TYPE_NONE;
    }
}
