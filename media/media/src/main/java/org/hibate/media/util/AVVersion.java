/*
 * Copyright (C) 2026 Hibate <ycaia86@126.com>
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
 * Created on 2026/03/25.
 */
public class AVVersion {

    /**
     * n6.1
     *
     * <p>Released in November 2023
     */
    public static final int N6_1 = 0x3a1d64;

    /**
     * n7.0
     *
     * <p>Released in April 2024
     */
    public static final int N7_0 = 0x3b0864;

    /**
     * n7.1
     *
     * <p>Released in September 2024
     */
    public static final int N7_1 = 0x3b2764;

    /**
     * n8.0
     *
     * <p>Released in August 2025
     */
    public static final int N8_0 = 0x3c0864;

    /**
     * n8.1
     *
     * <p>Released in February 2026.
     */
    public static final int N8_1 = 0x3c1a64;

    public static int VERSION(int major, int minor, int micro) {
        return (((major) << 16) | ((minor) << 8) | (micro));
    }
}
