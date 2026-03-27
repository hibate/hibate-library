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

/**
 * @author Hibate
 * Created on 2018/04/02.
 */
public enum PixelFormat {

    // 10 Primary YUV formats: 5 planar, 2 biplanar, 2 packed.
    FOURCC_I420('I', '4', '2', '0'),
    FOURCC_I422('I', '4', '2', '2'),
    FOURCC_I444('I', '4', '4', '4'),
    FOURCC_I400('I', '4', '0', '0'),
    FOURCC_NV21('N', 'V', '2', '1'),
    FOURCC_NV12('N', 'V', '1', '2'),
    FOURCC_YUY2('Y', 'U', 'Y', '2'),
    FOURCC_UYVY('U', 'Y', 'V', 'Y'),
    FOURCC_I010('I', '0', '1', '0'),  // bt.601 10 bit 420
    FOURCC_I210('I', '2', '1', '0'),  // bt.601 10 bit 422

    // 1 Secondary YUV format: row biplanar.  deprecated.
    FOURCC_M420('M', '4', '2', '0'),

    // 13 Primary RGB formats: 4 32 bpp, 2 24 bpp, 3 16 bpp, 1 10 bpc 2 64 bpp
    FOURCC_ARGB('A', 'R', 'G', 'B'),
    FOURCC_BGRA('B', 'G', 'R', 'A'),
    FOURCC_ABGR('A', 'B', 'G', 'R'),
    FOURCC_AR30('A', 'R', '3', '0'),  // 10 bit per channel. 2101010.
    FOURCC_AB30('A', 'B', '3', '0'),  // ABGR version of 10 bit
    FOURCC_AR64('A', 'R', '6', '4'),  // 16 bit per channel.
    FOURCC_AB64('A', 'B', '6', '4'),  // ABGR version of 16 bit
    FOURCC_24BG('2', '4', 'B', 'G'),
    FOURCC_RAW('r', 'a', 'w', ' '),
    FOURCC_RGBA('R', 'G', 'B', 'A'),
    FOURCC_RGBP('R', 'G', 'B', 'P'),  // rgb565 LE.
    FOURCC_RGBO('R', 'G', 'B', 'O'),  // argb1555 LE.
    FOURCC_R444('R', '4', '4', '4'),  // argb4444 LE.

    // 1 Primary Compressed YUV format.
    FOURCC_MJPG('M', 'J', 'P', 'G'),

    // 14 Auxiliary YUV variations: 3 with U and V planes are swapped, 1 Alias.
    FOURCC_YV12('Y', 'V', '1', '2'),
    FOURCC_YV16('Y', 'V', '1', '6'),
    FOURCC_YV24('Y', 'V', '2', '4'),
    FOURCC_YU12('Y', 'U', '1', '2'),  // Linux version of I420.
    FOURCC_J420('J', '4', '2', '0'),  // jpeg (bt.601 full), unofficial fourcc
    FOURCC_J422('J', '4', '2', '2'),  // jpeg (bt.601 full), unofficial fourcc
    FOURCC_J444('J', '4', '4', '4'),  // jpeg (bt.601 full), unofficial fourcc
    FOURCC_J400('J', '4', '0', '0'),  // jpeg (bt.601 full), unofficial fourcc
    FOURCC_F420('F', '4', '2', '0'),  // bt.709 full, unofficial fourcc
    FOURCC_F422('F', '4', '2', '2'),  // bt.709 full, unofficial fourcc
    FOURCC_F444('F', '4', '4', '4'),  // bt.709 full, unofficial fourcc
    FOURCC_H420('H', '4', '2', '0'),  // bt.709, unofficial fourcc
    FOURCC_H422('H', '4', '2', '2'),  // bt.709, unofficial fourcc
    FOURCC_H444('H', '4', '4', '4'),  // bt.709, unofficial fourcc
    FOURCC_U420('U', '4', '2', '0'),  // bt.2020, unofficial fourcc
    FOURCC_U422('U', '4', '2', '2'),  // bt.2020, unofficial fourcc
    FOURCC_U444('U', '4', '4', '4'),  // bt.2020, unofficial fourcc
    FOURCC_F010('F', '0', '1', '0'),  // bt.709 full range 10 bit 420
    FOURCC_H010('H', '0', '1', '0'),  // bt.709 10 bit 420
    FOURCC_U010('U', '0', '1', '0'),  // bt.2020 10 bit 420
    FOURCC_F210('F', '2', '1', '0'),  // bt.709 full range 10 bit 422
    FOURCC_H210('H', '2', '1', '0'),  // bt.709 10 bit 422
    FOURCC_U210('U', '2', '1', '0'),  // bt.2020 10 bit 422
    FOURCC_P010('P', '0', '1', '0'),
    FOURCC_P210('P', '2', '1', '0'),

    // 14 Auxiliary aliases.  CanonicalFourCC() maps these to canonical fourcc.
    FOURCC_IYUV('I', 'Y', 'U', 'V'),  // Alias for I420.
    FOURCC_YU16('Y', 'U', '1', '6'),  // Alias for I422.
    FOURCC_YU24('Y', 'U', '2', '4'),  // Alias for I444.
    FOURCC_YUYV('Y', 'U', 'Y', 'V'),  // Alias for YUY2.
    FOURCC_YUVS('y', 'u', 'v', 's'),  // Alias for YUY2 on Mac.
    FOURCC_HDYC('H', 'D', 'Y', 'C'),  // Alias for UYVY.
    FOURCC_2VUY('2', 'v', 'u', 'y'),  // Alias for UYVY on Mac.
    FOURCC_JPEG('J', 'P', 'E', 'G'),  // Alias for MJPG.
    FOURCC_DMB1('d', 'm', 'b', '1'),  // Alias for MJPG on Mac.
    FOURCC_BA81('B', 'A', '8', '1'),  // Alias for BGGR.
    FOURCC_RGB3('R', 'G', 'B', '3'),  // Alias for RAW.
    FOURCC_BGR3('B', 'G', 'R', '3'),  // Alias for 24BG.
    FOURCC_CM32(0, 0, 0, 32),  // Alias for BGRA kCMPixelFormat_32ARGB
    FOURCC_CM24(0, 0, 0, 24),  // Alias for RAW kCMPixelFormat_24RGB
    FOURCC_L555('L', '5', '5', '5'),  // Alias for RGBO.
    FOURCC_L565('L', '5', '6', '5'),  // Alias for RGBP.
    FOURCC_5551('5', '5', '5', '1'),  // Alias for RGBO.

    // deprecated formats.  Not supported, but defined for backward compatibility.
    FOURCC_I411('I', '4', '1', '1'),
    FOURCC_Q420('Q', '4', '2', '0'),
    FOURCC_RGGB('R', 'G', 'G', 'B'),
    FOURCC_BGGR('B', 'G', 'G', 'R'),
    FOURCC_GRBG('G', 'R', 'B', 'G'),
    FOURCC_GBRG('G', 'B', 'R', 'G'),
    FOURCC_H264('H', '2', '6', '4'),
    ;

    private final int codes;

    PixelFormat(char a, char b, char c, char d) {
        this(a, b, c, (int) d);
    }

    PixelFormat(int a, int b, int c, int d) {
        this.codes = a | (b << 8) | (c << 16) | (d << 24);
    }

    public int getCodes() {
        return codes;
    }
}
