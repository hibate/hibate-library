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
public enum AVError {

    AVERROR_EOF(FFERRTAG( 'E','O','F',' ')), // End of file

    ;

    private final int statusCode;

    AVError(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static int AVERROR(int code) {
        return -code;
    }

    public static int AVERROR(Error error) {
        return AVERROR(error.getStatusCode());
    }

    public static int FFERRTAG(char a, char b, char c, char d) {
        return -MKTAG(a, b, c, d);
    }

    public static int MKTAG(char a, char b, char c, char d) {
        return ((a) | ((b) << 8) | ((c) << 16) | ((d) << 24));
    }
}
