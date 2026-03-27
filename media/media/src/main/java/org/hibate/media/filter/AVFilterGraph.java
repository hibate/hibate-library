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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Closeable;

/**
 * @author Hibate
 * Created on 2026/03/21.
 */
public interface AVFilterGraph extends Closeable {

    /**
     * 将由字符串描述的图添加到现有图中: avfilter_graph_parse_ptr
     * @param specs 待解析字符串
     * @param input 指向图输入的链表
     * @param output 指向图输出的链表
     * @return {@link AVFilterGraph}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVFilterGraph setDescription(@NonNull String specs, @Nullable AVFilterInOut input, @Nullable AVFilterInOut output);

    /**
     * 检查图中所有链接和格式的有效性并进行配置: avfilter_graph_config
     * @return {@link AVFilterGraph}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVFilterGraph prepare();

    /**
     * 释放资源
     */
    void close();
}
