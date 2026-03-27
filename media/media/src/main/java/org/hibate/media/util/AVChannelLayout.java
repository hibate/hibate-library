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

/**
 * @author Hibate
 * Created on 2024/03/30.
 */
public interface AVChannelLayout {

    /**
     * 获取描述通道布局属性的可读字符串
     * @return 返回描述
     */
    String getDescription();

    /**
     * 获取通道顺序
     * @return 返回通道顺序
     */
    AVChannelOrder getChannelOrder();

    /**
     * 设置通道顺序
     * @param order 通道顺序
     * @return {@link AVChannelLayout}
     */
    AVChannelLayout setChannelOrder(AVChannelOrder order);

    /**
     * 获取声道数
     * @return 返回声道数
     */
    int getChannels();

    /**
     * 设置声道数, 仅限音频数据
     * @param channels 声道数
     * @return {@link AVChannelLayout}
     */
    AVChannelLayout setChannels(int channels);

    /**
     * 设置默认布局: av_channel_layout_default
     * @param channels 声道数
     * @return {@link AVChannelLayout}
     */
    AVChannelLayout setDefaults(int channels);
}
