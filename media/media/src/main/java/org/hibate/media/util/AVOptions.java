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
 * Created on 2026/03/22.
 */
public interface AVOptions {

    /**
     * Search in possible children of the given object first
     */
    int AV_OPT_SEARCH_CHILDREN = 1;
    /**
     *  The obj passed to av_opt_find() is fake -- only a double pointer to AVClass
     *  instead of a required pointer to a struct containing AVClass. This is
     *  useful for searching for options without needing to allocate the corresponding
     *  object.
     */
    int AV_OPT_SEARCH_FAKE_OBJ = 1 << 1;
    /**
     *  In av_opt_get, return NULL if the option has a pointer type and is set to NULL,
     *  rather than returning an empty string.
     */
    int AV_OPT_ALLOW_NULL = 1 << 2;
    /**
     * May be used with av_opt_set_array() to signal that new elements should
     * replace the existing ones in the indicated range.
     */
    int AV_OPT_ARRAY_REPLACE = 1 << 3;
    /**
     *  Allows av_opt_query_ranges and av_opt_query_ranges_default to return more than
     *  one component for certain option types.
     */
    int AV_OPT_MULTI_COMPONENT_RANGE = 1 << 12;

    /**
     * av_opt_set
     * set the field of obj with the given name to value
     * @param name the name of the field to set
     * @param value The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void put(String name, String value, int flags);

    /**
     * av_opt_set_int
     * set the field of obj with the given name to value
     * @param name the name of the field to set
     * @param value The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void put(String name, long value, int flags);

    /**
     * av_opt_set_double
     * set the field of obj with the given name to value
     * @param name the name of the field to set
     * @param value The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void put(String name, double value, int flags);

    /**
     * av_opt_set_q
     * set the field of obj with the given name to value
     * @param name the name of the field to set
     * @param value The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void put(String name, AVRational value, int flags);

    /**
     * av_opt_set_bin
     * set the field of obj with the given name to value
     * @param name the name of the field to set
     * @param value The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void put(String name, byte[] value, int flags);

    /**
     * av_opt_set_image_size
     * set the field of obj with the given name to value
     * @param name the name of the field to set
     * @param width The value to set. In case of av_opt_set() if the field is not
     * @param height The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void putImageSize(String name, int width, int height, int flags);

    /**
     * av_opt_set_pixel_fmt
     * set the field of obj with the given name to value
     * @param name the name of the field to set
     * @param format The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void putPixelFormat(String name, AVPixelFormat format, int flags);

    /**
     * av_opt_set_sample_fmt
     * set the field of obj with the given name to value
     * @param name the name of the field to set
     * @param format The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void putSampleFormat(String name, AVSampleFormat format, int flags);

    /**
     * av_opt_set_video_rate
     * set the field of obj with the given name to value
     * @param name the name of the field to set
     * @param value The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void putVideoRate(String name, AVRational value, int flags);

    /**
     * av_opt_set_chlayout
     * set the field of obj with the given name to value
     * any old chlayout present is discarded and replaced with a copy of the new one.
     * the caller still owns layout and is responsible for uninitializing it.
     * @param name the name of the field to set
     * @param value The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void putChannelLayout(String name, AVChannelLayout value, int flags);

    /**
     * av_opt_set_dict_val
     * set the field of obj with the given name to value
     * any old dictionary present is discarded and replaced with a copy of the new one.
     * the caller still owns val is and responsible for freeing it
     * @param name the name of the field to set
     * @param value The value to set. In case of av_opt_set() if the field is not
     * @param flags flags passed to av_opt_find2
     */
    void put(String name, AVDictionary value, int flags);
}
