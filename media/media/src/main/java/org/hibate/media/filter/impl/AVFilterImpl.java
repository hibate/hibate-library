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

package org.hibate.media.filter.impl;

import org.hibate.media.filter.AVFilter;

/**
 * @author Hibate
 * Created on 2026/03/21.
 */
class AVFilterImpl implements AVFilter {

    /**
     * AVFilter
     */
    private long ctx;

    @Override
    public native String getName();

    @Override
    public native String getDescription();

    @Override
    public native int getFlags();
}
