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

package org.hibate.media.codec;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
public enum AVMediaType {

    AVMEDIA_TYPE_UNKNOWN(-1),      ///< Usually treated as AVMEDIA_TYPE_DATA
    AVMEDIA_TYPE_VIDEO(0),
    AVMEDIA_TYPE_AUDIO(1),
    AVMEDIA_TYPE_DATA(2),          ///< Opaque data information usually continuous
    AVMEDIA_TYPE_SUBTITLE(3),
    AVMEDIA_TYPE_ATTACHMENT(4),    ///< Opaque data information usually sparse

    ;

    private final int mediaType;

    AVMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public int getMediaType() {
        return mediaType;
    }

    public static AVMediaType of(int mediaType) {
        for (AVMediaType type : AVMediaType.values()) {
            if (type.getMediaType() == mediaType) {
                return type;
            }
        }
        return AVMediaType.AVMEDIA_TYPE_UNKNOWN;
    }
}
