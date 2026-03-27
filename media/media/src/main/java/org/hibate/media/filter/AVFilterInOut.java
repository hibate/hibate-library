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

import java.io.Closeable;

/**
 * @author Hibate
 * Created on 2026/03/21.
 */
public interface AVFilterInOut extends Closeable {

    /**
     * 获取唯一名称
     * @return 返回唯一名称
     */
    String getName();

    /**
     * 设置唯一名称
     * @param name 名称
     * @return {@link AVFilterInOut}
     */
    AVFilterInOut setName(String name);

    /**
     * 获取滤镜上下文
     * @return 返回滤镜上下文
     */
    AVFilterContext getFilterContext();

    /**
     * 设置滤镜上下文
     * @param context 上下文
     * @return {@link AVFilterInOut}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVFilterInOut setFilterContext(AVFilterContext context);

    /**
     * 获取索引
     * @return 返回索引
     */
    int getIndex();

    /**
     * 设置索引
     * @param index 索引
     * @return {@link AVFilterInOut}
     */
    AVFilterInOut setIndex(int index);

    /**
     * 获取下一个滤镜链, 若为最后一个, 则返回 null
     * @return 返回滤镜链
     */
    AVFilterInOut getNext();

    /**
     * 设置下一个链表
     * @param filter 滤镜链
     * @return {@link AVFilterInOut}
     */
    AVFilterInOut setNext(AVFilterInOut filter);

    /**
     * 释放资源
     */
    void close();
}
