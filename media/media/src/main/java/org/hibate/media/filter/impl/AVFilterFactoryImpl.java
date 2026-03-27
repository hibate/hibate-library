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

import androidx.annotation.NonNull;
import org.hibate.media.filter.AVFilter;
import org.hibate.media.filter.AVFilterFactory;
import org.hibate.media.util.AVUtils;

import java.util.Optional;

/**
 * @author Hibate
 * Created on 2026/03/21.
 */
public class AVFilterFactoryImpl implements AVFilterFactory {

    static {
        AVUtils.LOADER.isAvailable();
    }

    @NonNull
    @Override
    public Optional<AVFilter> getFilter(String name) {
        return (name == null) ? Optional.empty() : Optional.ofNullable(this._getFilter(name));
    }

    private native AVFilter _getFilter(String name);
}
