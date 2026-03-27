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

package org.hibate.media.filter;

import androidx.annotation.Nullable;

/**
 * @author Hibate
 * Created on 2026/03/21.
 */
public interface AVFilter {

    /**
     * 获取滤镜名称
     * @return 返回滤镜名称
     */
    String getName();

    /**
     * 获取滤镜描述
     * @return 返回滤镜描述
     */
    @Nullable
    String getDescription();

    /**
     * 获取滤镜标记
     * @return 返回滤镜标记
     */
    int getFlags();
}
