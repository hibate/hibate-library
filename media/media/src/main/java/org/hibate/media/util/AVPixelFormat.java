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

import org.hibate.annotation.RequiresApi;

/**
 * libavutil/pixdesc.c
 * @author Hibate
 * Created on 2024/03/29.
 */
public enum AVPixelFormat {

    AV_PIX_FMT_NONE,
    AV_PIX_FMT_YUV420P("yuv420p"), ///< planar YUV 4:2:0, 12bpp, (1 Cr & Cb sample per 2x2 Y samples)
    AV_PIX_FMT_YUYV422("yuyv422"), ///< packed YUV 4:2:2, 16bpp, Y0 Cb Y1 Cr
    AV_PIX_FMT_RGB24("rgb24"), ///< packed RGB 8:8:8, 24bpp, RGBRGB...
    AV_PIX_FMT_BGR24("bgr24"), ///< packed RGB 8:8:8, 24bpp, BGRBGR...
    AV_PIX_FMT_YUV422P("yuv422p"), ///< planar YUV 4:2:2, 16bpp, (1 Cr & Cb sample per 2x1 Y samples)
    AV_PIX_FMT_YUV444P("yuv444p"), ///< planar YUV 4:4:4, 24bpp, (1 Cr & Cb sample per 1x1 Y samples)
    AV_PIX_FMT_YUV410P("yuv410p"), ///< planar YUV 4:1:0,  9bpp, (1 Cr & Cb sample per 4x4 Y samples)
    AV_PIX_FMT_YUV411P("yuv411p"), ///< planar YUV 4:1:1, 12bpp, (1 Cr & Cb sample per 4x1 Y samples)
    AV_PIX_FMT_GRAY8("gray"), ///<        Y        ,  8bpp
    AV_PIX_FMT_MONOWHITE("monow"), ///<        Y        ,  1bpp, 0 is white, 1 is black, in each byte pixels are ordered from the msb to the lsb
    AV_PIX_FMT_MONOBLACK("monob"), ///<        Y        ,  1bpp, 0 is black, 1 is white, in each byte pixels are ordered from the msb to the lsb
    AV_PIX_FMT_PAL8("pal8"), ///< 8 bits with AV_PIX_FMT_RGB32 palette
    AV_PIX_FMT_YUVJ420P("yuvj420p"), ///< planar YUV 4:2:0, 12bpp, full scale (JPEG), deprecated in favor of AV_PIX_FMT_YUV420P and setting color_range
    AV_PIX_FMT_YUVJ422P("yuvj422p"), ///< planar YUV 4:2:2, 16bpp, full scale (JPEG), deprecated in favor of AV_PIX_FMT_YUV422P and setting color_range
    AV_PIX_FMT_YUVJ444P("yuvj444p"), ///< planar YUV 4:4:4, 24bpp, full scale (JPEG), deprecated in favor of AV_PIX_FMT_YUV444P and setting color_range
    AV_PIX_FMT_UYVY422("uyvy422"), ///< packed YUV 4:2:2, 16bpp, Cb Y0 Cr Y1
    AV_PIX_FMT_UYYVYY411("uyyvyy411"), ///< packed YUV 4:1:1, 12bpp, Cb Y0 Y1 Cr Y2 Y3
    AV_PIX_FMT_BGR8("bgr8"), ///< packed RGB 3:3:2,  8bpp, (msb)2B 3G 3R(lsb)
    AV_PIX_FMT_BGR4("bgr4"), ///< packed RGB 1:2:1 bitstream,  4bpp, (msb)1B 2G 1R(lsb), a byte contains two pixels, the first pixel in the byte is the one composed by the 4 msb bits
    AV_PIX_FMT_BGR4_BYTE("bgr4_byte"), ///< packed RGB 1:2:1,  8bpp, (msb)1B 2G 1R(lsb)
    AV_PIX_FMT_RGB8("rgb8"), ///< packed RGB 3:3:2,  8bpp, (msb)2R 3G 3B(lsb)
    AV_PIX_FMT_RGB4("rgb4"), ///< packed RGB 1:2:1 bitstream,  4bpp, (msb)1R 2G 1B(lsb), a byte contains two pixels, the first pixel in the byte is the one composed by the 4 msb bits
    AV_PIX_FMT_RGB4_BYTE("rgb4_byte"), ///< packed RGB 1:2:1,  8bpp, (msb)1R 2G 1B(lsb)
    AV_PIX_FMT_NV12("nv12"), ///< planar YUV 4:2:0, 12bpp, 1 plane for Y and 1 plane for the UV components, which are interleaved (first byte U and the following byte V)
    AV_PIX_FMT_NV21("nv21"), ///< as above, but U and V bytes are swapped

    AV_PIX_FMT_ARGB("argb"), ///< packed ARGB 8:8:8:8, 32bpp, ARGBARGB...
    AV_PIX_FMT_RGBA("rgba"), ///< packed RGBA 8:8:8:8, 32bpp, RGBARGBA...
    AV_PIX_FMT_ABGR("abgr"), ///< packed ABGR 8:8:8:8, 32bpp, ABGRABGR...
    AV_PIX_FMT_BGRA("bgra"), ///< packed BGRA 8:8:8:8, 32bpp, BGRABGRA...

    AV_PIX_FMT_GRAY16BE("gray16be"), ///<        Y        , 16bpp, big-endian
    AV_PIX_FMT_GRAY16LE("gray16le"), ///<        Y        , 16bpp, little-endian
    AV_PIX_FMT_YUV440P("yuv440p"), ///< planar YUV 4:4:0 (1 Cr & Cb sample per 1x2 Y samples)
    AV_PIX_FMT_YUVJ440P("yuvj440p"), ///< planar YUV 4:4:0 full scale (JPEG), deprecated in favor of AV_PIX_FMT_YUV440P and setting color_range
    AV_PIX_FMT_YUVA420P("yuva420p"), ///< planar YUV 4:2:0, 20bpp, (1 Cr & Cb sample per 2x2 Y & A samples)
    AV_PIX_FMT_RGB48BE("rgb48be"), ///< packed RGB 16:16:16, 48bpp, 16R, 16G, 16B, the 2-byte value for each R/G/B component is stored as big-endian
    AV_PIX_FMT_RGB48LE("rgb48le"), ///< packed RGB 16:16:16, 48bpp, 16R, 16G, 16B, the 2-byte value for each R/G/B component is stored as little-endian

    AV_PIX_FMT_RGB565BE("rgb565be"), ///< packed RGB 5:6:5, 16bpp, (msb)   5R 6G 5B(lsb), big-endian
    AV_PIX_FMT_RGB565LE("rgb565le"), ///< packed RGB 5:6:5, 16bpp, (msb)   5R 6G 5B(lsb), little-endian
    AV_PIX_FMT_RGB555BE("rgb555be"), ///< packed RGB 5:5:5, 16bpp, (msb)1X 5R 5G 5B(lsb), big-endian   , X=unused/undefined
    AV_PIX_FMT_RGB555LE("rgb555le"), ///< packed RGB 5:5:5, 16bpp, (msb)1X 5R 5G 5B(lsb), little-endian, X=unused/undefined

    AV_PIX_FMT_BGR565BE("bgr565be"), ///< packed BGR 5:6:5, 16bpp, (msb)   5B 6G 5R(lsb), big-endian
    AV_PIX_FMT_BGR565LE("bgr565le"), ///< packed BGR 5:6:5, 16bpp, (msb)   5B 6G 5R(lsb), little-endian
    AV_PIX_FMT_BGR555BE("bgr555be"), ///< packed BGR 5:5:5, 16bpp, (msb)1X 5B 5G 5R(lsb), big-endian   , X=unused/undefined
    AV_PIX_FMT_BGR555LE("bgr555le"), ///< packed BGR 5:5:5, 16bpp, (msb)1X 5B 5G 5R(lsb), little-endian, X=unused/undefined

    /**
     *  Hardware acceleration through VA-API, data[3] contains a
     *  VASurfaceID.
     */
    AV_PIX_FMT_VAAPI("vaapi"),

    AV_PIX_FMT_YUV420P16LE("yuv420p16le"), ///< planar YUV 4:2:0, 24bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
    AV_PIX_FMT_YUV420P16BE("yuv420p16be"), ///< planar YUV 4:2:0, 24bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
    AV_PIX_FMT_YUV422P16LE("yuv422p16le"), ///< planar YUV 4:2:2, 32bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    AV_PIX_FMT_YUV422P16BE("yuv422p16be"), ///< planar YUV 4:2:2, 32bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    AV_PIX_FMT_YUV444P16LE("yuv444p16le"), ///< planar YUV 4:4:4, 48bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
    AV_PIX_FMT_YUV444P16BE("yuv444p16be"), ///< planar YUV 4:4:4, 48bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
    AV_PIX_FMT_DXVA2_VLD("dxva2_vld"), ///< HW decoding through DXVA2, Picture.data[3] contains a LPDIRECT3DSURFACE9 pointer

    AV_PIX_FMT_RGB444LE("rgb444le"), ///< packed RGB 4:4:4, 16bpp, (msb)4X 4R 4G 4B(lsb), little-endian, X=unused/undefined
    AV_PIX_FMT_RGB444BE("rgb444be"), ///< packed RGB 4:4:4, 16bpp, (msb)4X 4R 4G 4B(lsb), big-endian,    X=unused/undefined
    AV_PIX_FMT_BGR444LE("bgr444le"), ///< packed BGR 4:4:4, 16bpp, (msb)4X 4B 4G 4R(lsb), little-endian, X=unused/undefined
    AV_PIX_FMT_BGR444BE("bgr444be"), ///< packed BGR 4:4:4, 16bpp, (msb)4X 4B 4G 4R(lsb), big-endian,    X=unused/undefined
    AV_PIX_FMT_YA8("ya8"), ///< 8 bits gray, 8 bits alpha

    AV_PIX_FMT_Y400A(AV_PIX_FMT_YA8), ///< alias for AV_PIX_FMT_YA8
    AV_PIX_FMT_GRAY8A(AV_PIX_FMT_YA8), ///< alias for AV_PIX_FMT_YA8

    AV_PIX_FMT_BGR48BE("bgr48be"), ///< packed RGB 16:16:16, 48bpp, 16B, 16G, 16R, the 2-byte value for each R/G/B component is stored as big-endian
    AV_PIX_FMT_BGR48LE("bgr48le"), ///< packed RGB 16:16:16, 48bpp, 16B, 16G, 16R, the 2-byte value for each R/G/B component is stored as little-endian

    /**
     * The following 12 formats have the disadvantage of needing 1 format for each bit depth.
     * Notice that each 9/10 bits sample is stored in 16 bits with extra padding.
     * If you want to support multiple bit depths, then using AV_PIX_FMT_YUV420P16* with the bpp stored separately is better.
     */
    AV_PIX_FMT_YUV420P9BE("yuv420p9be"), ///< planar YUV 4:2:0, 13.5bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
    AV_PIX_FMT_YUV420P9LE("yuv420p9le"), ///< planar YUV 4:2:0, 13.5bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
    AV_PIX_FMT_YUV420P10BE("yuv420p10be"), ///< planar YUV 4:2:0, 15bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
    AV_PIX_FMT_YUV420P10LE("yuv420p10le"), ///< planar YUV 4:2:0, 15bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
    AV_PIX_FMT_YUV422P10BE("yuv422p10be"), ///< planar YUV 4:2:2, 20bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    AV_PIX_FMT_YUV422P10LE("yuv422p10le"), ///< planar YUV 4:2:2, 20bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    AV_PIX_FMT_YUV444P9BE("yuv444p9be"), ///< planar YUV 4:4:4, 27bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
    AV_PIX_FMT_YUV444P9LE("yuv444p9le"), ///< planar YUV 4:4:4, 27bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
    AV_PIX_FMT_YUV444P10BE("yuv444p10be"), ///< planar YUV 4:4:4, 30bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
    AV_PIX_FMT_YUV444P10LE("yuv444p10le"), ///< planar YUV 4:4:4, 30bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
    AV_PIX_FMT_YUV422P9BE("yuv422p9be"), ///< planar YUV 4:2:2, 18bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    AV_PIX_FMT_YUV422P9LE("yuv422p9le"), ///< planar YUV 4:2:2, 18bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    AV_PIX_FMT_GBRP("gbrp"), ///< planar GBR 4:4:4 24bpp
    AV_PIX_FMT_GBR24P(AV_PIX_FMT_GBRP), /// alias for #AV_PIX_FMT_GBRP
    AV_PIX_FMT_GBRP9BE("gbrp9be"), ///< planar GBR 4:4:4 27bpp, big-endian
    AV_PIX_FMT_GBRP9LE("gbrp9le"), ///< planar GBR 4:4:4 27bpp, little-endian
    AV_PIX_FMT_GBRP10BE("gbrp10be"), ///< planar GBR 4:4:4 30bpp, big-endian
    AV_PIX_FMT_GBRP10LE("gbrp10le"), ///< planar GBR 4:4:4 30bpp, little-endian
    AV_PIX_FMT_GBRP16BE("gbrp16be"), ///< planar GBR 4:4:4 48bpp, big-endian
    AV_PIX_FMT_GBRP16LE("gbrp16le"), ///< planar GBR 4:4:4 48bpp, little-endian
    AV_PIX_FMT_YUVA422P("yuva422p"), ///< planar YUV 4:2:2 24bpp, (1 Cr & Cb sample per 2x1 Y & A samples)
    AV_PIX_FMT_YUVA444P("yuva444p"), ///< planar YUV 4:4:4 32bpp, (1 Cr & Cb sample per 1x1 Y & A samples)
    AV_PIX_FMT_YUVA420P9BE("yuva420p9be"), ///< planar YUV 4:2:0 22.5bpp, (1 Cr & Cb sample per 2x2 Y & A samples), big-endian
    AV_PIX_FMT_YUVA420P9LE("yuva420p9le"), ///< planar YUV 4:2:0 22.5bpp, (1 Cr & Cb sample per 2x2 Y & A samples), little-endian
    AV_PIX_FMT_YUVA422P9BE("yuva422p9be"), ///< planar YUV 4:2:2 27bpp, (1 Cr & Cb sample per 2x1 Y & A samples), big-endian
    AV_PIX_FMT_YUVA422P9LE("yuva422p9le"), ///< planar YUV 4:2:2 27bpp, (1 Cr & Cb sample per 2x1 Y & A samples), little-endian
    AV_PIX_FMT_YUVA444P9BE("yuva444p9be"), ///< planar YUV 4:4:4 36bpp, (1 Cr & Cb sample per 1x1 Y & A samples), big-endian
    AV_PIX_FMT_YUVA444P9LE("yuva444p9le"), ///< planar YUV 4:4:4 36bpp, (1 Cr & Cb sample per 1x1 Y & A samples), little-endian
    AV_PIX_FMT_YUVA420P10BE("yuva420p10be"), ///< planar YUV 4:2:0 25bpp, (1 Cr & Cb sample per 2x2 Y & A samples, big-endian)
    AV_PIX_FMT_YUVA420P10LE("yuva420p10le"), ///< planar YUV 4:2:0 25bpp, (1 Cr & Cb sample per 2x2 Y & A samples, little-endian)
    AV_PIX_FMT_YUVA422P10BE("yuva422p10be"), ///< planar YUV 4:2:2 30bpp, (1 Cr & Cb sample per 2x1 Y & A samples, big-endian)
    AV_PIX_FMT_YUVA422P10LE("yuva422p10le"), ///< planar YUV 4:2:2 30bpp, (1 Cr & Cb sample per 2x1 Y & A samples, little-endian)
    AV_PIX_FMT_YUVA444P10BE("yuva444p10be"), ///< planar YUV 4:4:4 40bpp, (1 Cr & Cb sample per 1x1 Y & A samples, big-endian)
    AV_PIX_FMT_YUVA444P10LE("yuva444p10le"), ///< planar YUV 4:4:4 40bpp, (1 Cr & Cb sample per 1x1 Y & A samples, little-endian)
    AV_PIX_FMT_YUVA420P16BE("yuva420p16be"), ///< planar YUV 4:2:0 40bpp, (1 Cr & Cb sample per 2x2 Y & A samples, big-endian)
    AV_PIX_FMT_YUVA420P16LE("yuva420p16le"), ///< planar YUV 4:2:0 40bpp, (1 Cr & Cb sample per 2x2 Y & A samples, little-endian)
    AV_PIX_FMT_YUVA422P16BE("yuva422p16be"), ///< planar YUV 4:2:2 48bpp, (1 Cr & Cb sample per 2x1 Y & A samples, big-endian)
    AV_PIX_FMT_YUVA422P16LE("yuva422p16le"), ///< planar YUV 4:2:2 48bpp, (1 Cr & Cb sample per 2x1 Y & A samples, little-endian)
    AV_PIX_FMT_YUVA444P16BE("yuva444p16be"), ///< planar YUV 4:4:4 64bpp, (1 Cr & Cb sample per 1x1 Y & A samples, big-endian)
    AV_PIX_FMT_YUVA444P16LE("yuva444p16le"), ///< planar YUV 4:4:4 64bpp, (1 Cr & Cb sample per 1x1 Y & A samples, little-endian)

    AV_PIX_FMT_VDPAU("vdpau"), ///< HW acceleration through VDPAU, Picture.data[3] contains a VdpVideoSurface

    AV_PIX_FMT_XYZ12LE("xyz12le"), ///< packed XYZ 4:4:4, 36 bpp, (msb) 12X, 12Y, 12Z (lsb), the 2-byte value for each X/Y/Z is stored as little-endian, the 4 lower bits are set to 0
    AV_PIX_FMT_XYZ12BE("xyz12be"), ///< packed XYZ 4:4:4, 36 bpp, (msb) 12X, 12Y, 12Z (lsb), the 2-byte value for each X/Y/Z is stored as big-endian, the 4 lower bits are set to 0
    AV_PIX_FMT_NV16("nv16"), ///< interleaved chroma YUV 4:2:2, 16bpp, (1 Cr & Cb sample per 2x1 Y samples)
    AV_PIX_FMT_NV20LE("nv20le"), ///< interleaved chroma YUV 4:2:2, 20bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    AV_PIX_FMT_NV20BE("nv20be"), ///< interleaved chroma YUV 4:2:2, 20bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian

    AV_PIX_FMT_RGBA64BE("rgba64be"), ///< packed RGBA 16:16:16:16, 64bpp, 16R, 16G, 16B, 16A, the 2-byte value for each R/G/B/A component is stored as big-endian
    AV_PIX_FMT_RGBA64LE("rgba64le"), ///< packed RGBA 16:16:16:16, 64bpp, 16R, 16G, 16B, 16A, the 2-byte value for each R/G/B/A component is stored as little-endian
    AV_PIX_FMT_BGRA64BE("bgra64be"), ///< packed RGBA 16:16:16:16, 64bpp, 16B, 16G, 16R, 16A, the 2-byte value for each R/G/B/A component is stored as big-endian
    AV_PIX_FMT_BGRA64LE("bgra64le"), ///< packed RGBA 16:16:16:16, 64bpp, 16B, 16G, 16R, 16A, the 2-byte value for each R/G/B/A component is stored as little-endian

    AV_PIX_FMT_YVYU422("yvyu422"), ///< packed YUV 4:2:2, 16bpp, Y0 Cr Y1 Cb

    AV_PIX_FMT_YA16BE("ya16be"), ///< 16 bits gray, 16 bits alpha (big-endian)
    AV_PIX_FMT_YA16LE("ya16le"), ///< 16 bits gray, 16 bits alpha (little-endian)

    AV_PIX_FMT_GBRAP("gbrap"), ///< planar GBRA 4:4:4:4 32bpp
    AV_PIX_FMT_GBRAP16BE("gbrap16be"), ///< planar GBRA 4:4:4:4 64bpp, big-endian
    AV_PIX_FMT_GBRAP16LE("gbrap16le"), ///< planar GBRA 4:4:4:4 64bpp, little-endian
    /**
     * HW acceleration through QSV, data[3] contains a pointer to the
     * mfxFrameSurface1 structure.
     * <p>
     * Before FFmpeg 5.0:
     * mfxFrameSurface1.Data.MemId contains a pointer when importing
     * the following frames as QSV frames:
     * <p>
     * VAAPI:
     * mfxFrameSurface1.Data.MemId contains a pointer to VASurfaceID
     * <p>
     * DXVA2:
     * mfxFrameSurface1.Data.MemId contains a pointer to IDirect3DSurface9
     * <p>
     * FFmpeg 5.0 and above:
     * mfxFrameSurface1.Data.MemId contains a pointer to the mfxHDLPair
     * structure when importing the following frames as QSV frames:
     * <p>
     * VAAPI:
     * mfxHDLPair.first contains a VASurfaceID pointer.
     * mfxHDLPair.second is always MFX_INFINITE.
     * <p>
     * DXVA2:
     * mfxHDLPair.first contains IDirect3DSurface9 pointer.
     * mfxHDLPair.second is always MFX_INFINITE.
     * <p>
     * D3D11:
     * mfxHDLPair.first contains a ID3D11Texture2D pointer.
     * mfxHDLPair.second contains the texture array index of the frame if the
     * ID3D11Texture2D is an array texture, or always MFX_INFINITE if it is a
     * normal texture.
     */
    AV_PIX_FMT_QSV("qsv"),
    /**
     * HW acceleration though MMAL, data[3] contains a pointer to the
     * MMAL_BUFFER_HEADER_T structure.
     */
    AV_PIX_FMT_MMAL("mmal"),

    AV_PIX_FMT_D3D11VA_VLD("d3d11va_vld"), ///< HW decoding through Direct3D11 via old API, Picture.data[3] contains a ID3D11VideoDecoderOutputView pointer

    /**
     * HW acceleration through CUDA. data[i] contain CUdeviceptr pointers
     * exactly as for system memory frames.
     */
    AV_PIX_FMT_CUDA("cuda"),

    AV_PIX_FMT_0RGB("0rgb"), ///< packed RGB 8:8:8, 32bpp, XRGBXRGB...   X=unused/undefined
    AV_PIX_FMT_RGB0("rgb0"), ///< packed RGB 8:8:8, 32bpp, RGBXRGBX...   X=unused/undefined
    AV_PIX_FMT_0BGR("0bgr"), ///< packed BGR 8:8:8, 32bpp, XBGRXBGR...   X=unused/undefined
    AV_PIX_FMT_BGR0("bgr0"), ///< packed BGR 8:8:8, 32bpp, BGRXBGRX...   X=unused/undefined

    AV_PIX_FMT_YUV420P12BE("yuv420p12be"), ///< planar YUV 4:2:0,18bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
    AV_PIX_FMT_YUV420P12LE("yuv420p12le"), ///< planar YUV 4:2:0,18bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
    AV_PIX_FMT_YUV420P14BE("yuv420p14be"), ///< planar YUV 4:2:0,21bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
    AV_PIX_FMT_YUV420P14LE("yuv420p14le"), ///< planar YUV 4:2:0,21bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
    AV_PIX_FMT_YUV422P12BE("yuv422p12be"), ///< planar YUV 4:2:2,24bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    AV_PIX_FMT_YUV422P12LE("yuv422p12le"), ///< planar YUV 4:2:2,24bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    AV_PIX_FMT_YUV422P14BE("yuv422p14be"), ///< planar YUV 4:2:2,28bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    AV_PIX_FMT_YUV422P14LE("yuv422p14le"), ///< planar YUV 4:2:2,28bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    AV_PIX_FMT_YUV444P12BE("yuv444p12be"), ///< planar YUV 4:4:4,36bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
    AV_PIX_FMT_YUV444P12LE("yuv444p12le"), ///< planar YUV 4:4:4,36bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
    AV_PIX_FMT_YUV444P14BE("yuv444p14be"), ///< planar YUV 4:4:4,42bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
    AV_PIX_FMT_YUV444P14LE("yuv444p14le"), ///< planar YUV 4:4:4,42bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
    AV_PIX_FMT_GBRP12BE("gbrp12be"), ///< planar GBR 4:4:4 36bpp, big-endian
    AV_PIX_FMT_GBRP12LE("gbrp12le"), ///< planar GBR 4:4:4 36bpp, little-endian
    AV_PIX_FMT_GBRP14BE("gbrp14be"), ///< planar GBR 4:4:4 42bpp, big-endian
    AV_PIX_FMT_GBRP14LE("gbrp14le"), ///< planar GBR 4:4:4 42bpp, little-endian
    AV_PIX_FMT_YUVJ411P("yuvj411p"), ///< planar YUV 4:1:1, 12bpp, (1 Cr & Cb sample per 4x1 Y samples) full scale (JPEG), deprecated in favor of AV_PIX_FMT_YUV411P and setting color_range

    AV_PIX_FMT_BAYER_BGGR8("bayer_bggr8"), ///< bayer, BGBG..(odd line), GRGR..(even line), 8-bit samples
    AV_PIX_FMT_BAYER_RGGB8("bayer_rggb8"), ///< bayer, RGRG..(odd line), GBGB..(even line), 8-bit samples
    AV_PIX_FMT_BAYER_GBRG8("bayer_gbrg8"), ///< bayer, GBGB..(odd line), RGRG..(even line), 8-bit samples
    AV_PIX_FMT_BAYER_GRBG8("bayer_grbg8"), ///< bayer, GRGR..(odd line), BGBG..(even line), 8-bit samples
    AV_PIX_FMT_BAYER_BGGR16LE("bayer_bggr16le"), ///< bayer, BGBG..(odd line), GRGR..(even line), 16-bit samples, little-endian
    AV_PIX_FMT_BAYER_BGGR16BE("bayer_bggr16be"), ///< bayer, BGBG..(odd line), GRGR..(even line), 16-bit samples, big-endian
    AV_PIX_FMT_BAYER_RGGB16LE("bayer_rggb16le"), ///< bayer, RGRG..(odd line), GBGB..(even line), 16-bit samples, little-endian
    AV_PIX_FMT_BAYER_RGGB16BE("bayer_rggb16be"), ///< bayer, RGRG..(odd line), GBGB..(even line), 16-bit samples, big-endian
    AV_PIX_FMT_BAYER_GBRG16LE("bayer_gbrg16le"), ///< bayer, GBGB..(odd line), RGRG..(even line), 16-bit samples, little-endian
    AV_PIX_FMT_BAYER_GBRG16BE("bayer_gbrg16be"), ///< bayer, GBGB..(odd line), RGRG..(even line), 16-bit samples, big-endian
    AV_PIX_FMT_BAYER_GRBG16LE("bayer_grbg16le"), ///< bayer, GRGR..(odd line), BGBG..(even line), 16-bit samples, little-endian
    AV_PIX_FMT_BAYER_GRBG16BE("bayer_grbg16be"), ///< bayer, GRGR..(odd line), BGBG..(even line), 16-bit samples, big-endian

    AV_PIX_FMT_YUV440P10LE("yuv440p10le"), ///< planar YUV 4:4:0,20bpp, (1 Cr & Cb sample per 1x2 Y samples), little-endian
    AV_PIX_FMT_YUV440P10BE("yuv440p10be"), ///< planar YUV 4:4:0,20bpp, (1 Cr & Cb sample per 1x2 Y samples), big-endian
    AV_PIX_FMT_YUV440P12LE("yuv440p12le"), ///< planar YUV 4:4:0,24bpp, (1 Cr & Cb sample per 1x2 Y samples), little-endian
    AV_PIX_FMT_YUV440P12BE("yuv440p12be"), ///< planar YUV 4:4:0,24bpp, (1 Cr & Cb sample per 1x2 Y samples), big-endian
    AV_PIX_FMT_AYUV64LE("ayuv64le"), ///< packed AYUV 4:4:4,64bpp (1 Cr & Cb sample per 1x1 Y & A samples), little-endian
    AV_PIX_FMT_AYUV64BE("ayuv64be"), ///< packed AYUV 4:4:4,64bpp (1 Cr & Cb sample per 1x1 Y & A samples), big-endian

    AV_PIX_FMT_VIDEOTOOLBOX("videotoolbox_vld"), ///< hardware decoding through Videotoolbox

    AV_PIX_FMT_P010LE("p010le"), ///< like NV12, with 10bpp per component, data in the high bits, zeros in the low bits, little-endian
    AV_PIX_FMT_P010BE("p010be"), ///< like NV12, with 10bpp per component, data in the high bits, zeros in the low bits, big-endian

    AV_PIX_FMT_GBRAP12BE("gbrap12be"), ///< planar GBR 4:4:4:4 48bpp, big-endian
    AV_PIX_FMT_GBRAP12LE("gbrap12le"), ///< planar GBR 4:4:4:4 48bpp, little-endian

    AV_PIX_FMT_GBRAP10BE("gbrap10be"), ///< planar GBR 4:4:4:4 40bpp, big-endian
    AV_PIX_FMT_GBRAP10LE("gbrap10le"), ///< planar GBR 4:4:4:4 40bpp, little-endian

    AV_PIX_FMT_MEDIACODEC("mediacodec"), ///< hardware decoding through MediaCodec

    AV_PIX_FMT_GRAY12BE("gray12be"), ///<        Y        , 12bpp, big-endian
    AV_PIX_FMT_GRAY12LE("gray12le"), ///<        Y        , 12bpp, little-endian
    AV_PIX_FMT_GRAY10BE("gray10be"), ///<        Y        , 10bpp, big-endian
    AV_PIX_FMT_GRAY10LE("gray10le"), ///<        Y        , 10bpp, little-endian

    AV_PIX_FMT_P016LE("p016le"), ///< like NV12, with 16bpp per component, little-endian
    AV_PIX_FMT_P016BE("p016be"), ///< like NV12, with 16bpp per component, big-endian

    /**
     * Hardware surfaces for Direct3D11.
     * <p>
     * This is preferred over the legacy AV_PIX_FMT_D3D11VA_VLD. The new D3D11
     * hwaccel API and filtering support AV_PIX_FMT_D3D11 only.
     * <p>
     * data[0] contains a ID3D11Texture2D pointer, and data[1] contains the
     * texture array index of the frame as intptr_t if the ID3D11Texture2D is
     * an array texture (or always 0 if it's a normal texture).
     */
    AV_PIX_FMT_D3D11("d3d11"),

    AV_PIX_FMT_GRAY9BE("gray9be"), ///<        Y        , 9bpp, big-endian
    AV_PIX_FMT_GRAY9LE("gray9le"), ///<        Y        , 9bpp, little-endian

    AV_PIX_FMT_GBRPF32BE("gbrpf32be"), ///< IEEE-754 single precision planar GBR 4:4:4,     96bpp, big-endian
    AV_PIX_FMT_GBRPF32LE("gbrpf32le"), ///< IEEE-754 single precision planar GBR 4:4:4,     96bpp, little-endian
    AV_PIX_FMT_GBRAPF32BE("gbrapf32be"), ///< IEEE-754 single precision planar GBRA 4:4:4:4, 128bpp, big-endian
    AV_PIX_FMT_GBRAPF32LE("gbrapf32le"), ///< IEEE-754 single precision planar GBRA 4:4:4:4, 128bpp, little-endian

    /**
     * DRM-managed buffers exposed through PRIME buffer sharing.
     * <p>
     * data[0] points to an AVDRMFrameDescriptor.
     */
    AV_PIX_FMT_DRM_PRIME("drm_prime"),
    /**
     * Hardware surfaces for OpenCL.
     * <p>
     * data[i] contain 2D image objects (typed in C as cl_mem, used
     * in OpenCL as image2d_t) for each plane of the surface.
     */
    AV_PIX_FMT_OPENCL("opencl"),

    AV_PIX_FMT_GRAY14BE("gray14be"), ///<        Y        , 14bpp, big-endian
    AV_PIX_FMT_GRAY14LE("gray14le"), ///<        Y        , 14bpp, little-endian

    AV_PIX_FMT_GRAYF32BE("grayf32be"), ///< IEEE-754 single precision Y, 32bpp, big-endian
    AV_PIX_FMT_GRAYF32LE("grayf32le"), ///< IEEE-754 single precision Y, 32bpp, little-endian

    AV_PIX_FMT_YUVA422P12BE("yuva422p12be"), ///< planar YUV 4:2:2,24bpp, (1 Cr & Cb sample per 2x1 Y samples), 12b alpha, big-endian
    AV_PIX_FMT_YUVA422P12LE("yuva422p12le"), ///< planar YUV 4:2:2,24bpp, (1 Cr & Cb sample per 2x1 Y samples), 12b alpha, little-endian
    AV_PIX_FMT_YUVA444P12BE("yuva444p12be"), ///< planar YUV 4:4:4,36bpp, (1 Cr & Cb sample per 1x1 Y samples), 12b alpha, big-endian
    AV_PIX_FMT_YUVA444P12LE("yuva444p12le"), ///< planar YUV 4:4:4,36bpp, (1 Cr & Cb sample per 1x1 Y samples), 12b alpha, little-endian

    AV_PIX_FMT_NV24("nv24"), ///< planar YUV 4:4:4, 24bpp, 1 plane for Y and 1 plane for the UV components, which are interleaved (first byte U and the following byte V)
    AV_PIX_FMT_NV42("nv42"), ///< as above, but U and V bytes are swapped

    /**
     * Vulkan hardware images.
     * <p>
     * data[0] points to an AVVkFrame
     */
    AV_PIX_FMT_VULKAN("vulkan"),

    AV_PIX_FMT_Y210BE("y210be"), ///< packed YUV 4:2:2 like YUYV422, 20bpp, data in the high bits, big-endian
    AV_PIX_FMT_Y210LE("y210le"), ///< packed YUV 4:2:2 like YUYV422, 20bpp, data in the high bits, little-endian

    AV_PIX_FMT_X2RGB10LE("x2rgb10le"), ///< packed RGB 10:10:10, 30bpp, (msb)2X 10R 10G 10B(lsb), little-endian, X=unused/undefined
    AV_PIX_FMT_X2RGB10BE("x2rgb10be"), ///< packed RGB 10:10:10, 30bpp, (msb)2X 10R 10G 10B(lsb), big-endian, X=unused/undefined
    AV_PIX_FMT_X2BGR10LE("x2bgr10le"), ///< packed BGR 10:10:10, 30bpp, (msb)2X 10B 10G 10R(lsb), little-endian, X=unused/undefined
    AV_PIX_FMT_X2BGR10BE("x2bgr10be"), ///< packed BGR 10:10:10, 30bpp, (msb)2X 10B 10G 10R(lsb), big-endian, X=unused/undefined

    AV_PIX_FMT_P210BE("p210be"), ///< interleaved chroma YUV 4:2:2, 20bpp, data in the high bits, big-endian
    AV_PIX_FMT_P210LE("p210le"), ///< interleaved chroma YUV 4:2:2, 20bpp, data in the high bits, little-endian

    AV_PIX_FMT_P410BE("p410be"), ///< interleaved chroma YUV 4:4:4, 30bpp, data in the high bits, big-endian
    AV_PIX_FMT_P410LE("p410le"), ///< interleaved chroma YUV 4:4:4, 30bpp, data in the high bits, little-endian

    AV_PIX_FMT_P216BE("p216be"), ///< interleaved chroma YUV 4:2:2, 32bpp, big-endian
    AV_PIX_FMT_P216LE("p216le"), ///< interleaved chroma YUV 4:2:2, 32bpp, little-endian

    AV_PIX_FMT_P416BE("p416be"), ///< interleaved chroma YUV 4:4:4, 48bpp, big-endian
    AV_PIX_FMT_P416LE("p416le"), ///< interleaved chroma YUV 4:4:4, 48bpp, little-endian

    AV_PIX_FMT_VUYA("vuya"), ///< packed VUYA 4:4:4, 32bpp, VUYAVUYA...

    AV_PIX_FMT_RGBAF16BE("rgbaf16be"), ///< IEEE-754 half precision packed RGBA 16:16:16:16, 64bpp, RGBARGBA..., big-endian
    AV_PIX_FMT_RGBAF16LE("rgbaf16le"), ///< IEEE-754 half precision packed RGBA 16:16:16:16, 64bpp, RGBARGBA..., little-endian

    AV_PIX_FMT_VUYX("vuyx"), ///< packed VUYX 4:4:4, 32bpp, Variant of VUYA where alpha channel is left undefined

    AV_PIX_FMT_P012LE("p012le"), ///< like NV12, with 12bpp per component, data in the high bits, zeros in the low bits, little-endian
    AV_PIX_FMT_P012BE("p012be"), ///< like NV12, with 12bpp per component, data in the high bits, zeros in the low bits, big-endian

    AV_PIX_FMT_Y212BE("y212be"), ///< packed YUV 4:2:2 like YUYV422, 24bpp, data in the high bits, zeros in the low bits, big-endian
    AV_PIX_FMT_Y212LE("y212le"), ///< packed YUV 4:2:2 like YUYV422, 24bpp, data in the high bits, zeros in the low bits, little-endian

    AV_PIX_FMT_XV30BE("xv30be"), ///< packed XVYU 4:4:4, 32bpp, (msb)2X 10V 10Y 10U(lsb), big-endian, variant of Y410 where alpha channel is left undefined
    AV_PIX_FMT_XV30LE("xv30le"), ///< packed XVYU 4:4:4, 32bpp, (msb)2X 10V 10Y 10U(lsb), little-endian, variant of Y410 where alpha channel is left undefined

    AV_PIX_FMT_XV36BE("xv36be"), ///< packed XVYU 4:4:4, 48bpp, data in the high bits, zeros in the low bits, big-endian, variant of Y412 where alpha channel is left undefined
    AV_PIX_FMT_XV36LE("xv36le"), ///< packed XVYU 4:4:4, 48bpp, data in the high bits, zeros in the low bits, little-endian, variant of Y412 where alpha channel is left undefined

    AV_PIX_FMT_RGBF32BE("rgbf32be"), ///< IEEE-754 single precision packed RGB 32:32:32, 96bpp, RGBRGB..., big-endian
    AV_PIX_FMT_RGBF32LE("rgbf32le"), ///< IEEE-754 single precision packed RGB 32:32:32, 96bpp, RGBRGB..., little-endian

    AV_PIX_FMT_RGBAF32BE("rgbaf32be"), ///< IEEE-754 single precision packed RGBA 32:32:32:32, 128bpp, RGBARGBA..., big-endian
    AV_PIX_FMT_RGBAF32LE("rgbaf32le"), ///< IEEE-754 single precision packed RGBA 32:32:32:32, 128bpp, RGBARGBA..., little-endian

    AV_PIX_FMT_P212BE("p212be"), ///< interleaved chroma YUV 4:2:2, 24bpp, data in the high bits, big-endian
    AV_PIX_FMT_P212LE("p212le"), ///< interleaved chroma YUV 4:2:2, 24bpp, data in the high bits, little-endian

    AV_PIX_FMT_P412BE("p412be"), ///< interleaved chroma YUV 4:4:4, 36bpp, data in the high bits, big-endian
    AV_PIX_FMT_P412LE("p412le"), ///< interleaved chroma YUV 4:4:4, 36bpp, data in the high bits, little-endian

    AV_PIX_FMT_GBRAP14BE("gbrap14be"), ///< planar GBR 4:4:4:4 56bpp, big-endian
    AV_PIX_FMT_GBRAP14LE("gbrap14le"), ///< planar GBR 4:4:4:4 56bpp, little-endian

    /**
     * Hardware surfaces for Direct3D 12.
     * <p>
     * data[0] points to an AVD3D12VAFrame
     */
    @RequiresApi(AVVersion.N7_0)
    AV_PIX_FMT_D3D12("d3d12"),

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_AYUV("ayuv"), ///< packed AYUV 4:4:4:4, 32bpp (1 Cr & Cb sample per 1x1 Y & A samples), AYUVAYUV...

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_UYVA("uyva"), ///< packed UYVA 4:4:4:4, 32bpp (1 Cr & Cb sample per 1x1 Y & A samples), UYVAUYVA...

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_VYU444("vyu444"), ///< packed VYU 4:4:4, 24bpp (1 Cr & Cb sample per 1x1 Y), VYUVYU...

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_V30XBE("v30xbe"), ///< packed VYUX 4:4:4 like XV30, 32bpp, (msb)10V 10Y 10U 2X(lsb), big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_V30XLE("v30xle"), ///< packed VYUX 4:4:4 like XV30, 32bpp, (msb)10V 10Y 10U 2X(lsb), little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_RGBF16BE("rgbf16be"), ///< IEEE-754 half precision packed RGB 16:16:16, 48bpp, RGBRGB..., big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_RGBF16LE("rgbf16le"), ///< IEEE-754 half precision packed RGB 16:16:16, 48bpp, RGBRGB..., little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_RGBA128BE("rgba128be"), ///< packed RGBA 32:32:32:32, 128bpp, RGBARGBA..., big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_RGBA128LE("rgba128le"), ///< packed RGBA 32:32:32:32, 128bpp, RGBARGBA..., little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_RGB96BE("rgb96be"), ///< packed RGBA 32:32:32, 96bpp, RGBRGB..., big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_RGB96LE("rgb96le"), ///< packed RGBA 32:32:32, 96bpp, RGBRGB..., little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_Y216BE("y216be"), ///< packed YUV 4:2:2 like YUYV422, 32bpp, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_Y216LE("y216le"), ///< packed YUV 4:2:2 like YUYV422, 32bpp, little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_XV48BE("xv48be"), ///< packed XVYU 4:4:4, 64bpp, big-endian, variant of Y416 where alpha channel is left undefined
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_XV48LE("xv48le"), ///< packed XVYU 4:4:4, 64bpp, little-endian, variant of Y416 where alpha channel is left undefined

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GBRPF16BE("gbrpf16be"), ///< IEEE-754 half precision planer GBR 4:4:4, 48bpp, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GBRPF16LE("gbrpf16le"), ///< IEEE-754 half precision planer GBR 4:4:4, 48bpp, little-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GBRAPF16BE("gbrapf16be"), ///< IEEE-754 half precision planar GBRA 4:4:4:4, 64bpp, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GBRAPF16LE("gbrapf16le"), ///< IEEE-754 half precision planar GBRA 4:4:4:4, 64bpp, little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GRAYF16BE("grayf16be"), ///< IEEE-754 half precision Y, 16bpp, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GRAYF16LE("grayf16le"), ///< IEEE-754 half precision Y, 16bpp, little-endian

    /**
     * HW acceleration through AMF. data[0] contain AMFSurface pointer
     */
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_AMF_SURFACE("amf"),

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GRAY32BE("gray32be"), ///<         Y        , 32bpp, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GRAY32LE("gray32le"), ///<         Y        , 32bpp, little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_YAF32BE("yaf32be"), ///< IEEE-754 single precision packed YA, 32 bits gray, 32 bits alpha, 64bpp, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_YAF32LE("yaf32le"), ///< IEEE-754 single precision packed YA, 32 bits gray, 32 bits alpha, 64bpp, little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_YAF16BE("yaf16be"), ///< IEEE-754 half precision packed YA, 16 bits gray, 16 bits alpha, 32bpp, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_YAF16LE("yaf16le"), ///< IEEE-754 half precision packed YA, 16 bits gray, 16 bits alpha, 32bpp, little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GBRAP32BE("gbrap32be"), ///< planar GBRA 4:4:4:4 128bpp, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GBRAP32LE("gbrap32le"), ///< planar GBRA 4:4:4:4 128bpp, little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_YUV444P10MSBBE("yuv444p10msbbe"), ///< planar YUV 4:4:4, 30bpp, (1 Cr & Cb sample per 1x1 Y samples), lowest bits zero, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_YUV444P10MSBLE("yuv444p10msble"), ///< planar YUV 4:4:4, 30bpp, (1 Cr & Cb sample per 1x1 Y samples), lowest bits zero, little-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_YUV444P12MSBBE("yuv444p12msbbe"), ///< planar YUV 4:4:4, 30bpp, (1 Cr & Cb sample per 1x1 Y samples), lowest bits zero, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_YUV444P12MSBLE("yuv444p12msble"), ///< planar YUV 4:4:4, 30bpp, (1 Cr & Cb sample per 1x1 Y samples), lowest bits zero, little-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GBRP10MSBBE("gbrp10msbbe"), ///< planar GBR 4:4:4 30bpp, lowest bits zero, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GBRP10MSBLE("gbrp10msble"), ///< planar GBR 4:4:4 30bpp, lowest bits zero, little-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GBRP12MSBBE("gbrp12msbbe"), ///< planar GBR 4:4:4 36bpp, lowest bits zero, big-endian
    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_GBRP12MSBLE("gbrp12msble"), ///< planar GBR 4:4:4 36bpp, lowest bits zero, little-endian

    @RequiresApi(AVVersion.N8_0)
    AV_PIX_FMT_OHCODEC("ohcodec"), /// hardware decoding through openharmony

    ;

    private final String name;

    AVPixelFormat() {
        this.name = null;
    }

    AVPixelFormat(String name) {
        this.name = name;
    }

    AVPixelFormat(AVPixelFormat alias) {
        this.name = alias.getName();
    }

    public String getName() {
        return name;
    }

    public static AVPixelFormat of(String name) {
        for (AVPixelFormat format : AVPixelFormat.values()) {
            if ((format.getName() != null) && format.getName().equals(name)) {
                return format;
            }
        }
        return AVPixelFormat.AV_PIX_FMT_NONE;
    }
}
