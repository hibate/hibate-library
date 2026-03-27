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

package org.hibate.media.format;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
public interface AVInputFormat {

    /**
     * 获取以逗号分隔的格式名称列表. 新名称可以附加一个小凸起
     * @return 返回以逗号分隔的格式名称列表
     */
    String getName();

    /**
     * 获取格式的描述性名称，更便于阅读
     * @return 返回格式的描述性名称
     */
    String getLongName();

    /**
     * 获取以逗号分隔的 mime 类型列表. 用于在探测时检查匹配的 mime 类型
     * @return 返回以逗号分隔的 mime 类型列表
     */
    String getMimeType();
}
