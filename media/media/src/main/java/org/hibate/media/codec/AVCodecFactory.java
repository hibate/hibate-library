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

package org.hibate.media.codec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Optional;

/**
 * @author Hibate
 * Created on 2024/03/25.
 */
public interface AVCodecFactory {

    /**
     * 获取编解码器
     * @param id 编解码器 id
     * @param type 编解码类型
     * @return 返回解码器
     */
    @NonNull
    Optional<AVCodec> getAVCodec(@NonNull AVCodecID id, @NonNull AVCodecType type);

    /**
     * 获取编码器
     * @param id 编码器 id
     * @return 返回编码器
     */
    @NonNull
    Optional<AVCodec> getEncoder(@Nullable AVCodecID id);

    /**
     * 获取解码器
     * @param id 解码器 id
     * @return 返回解码器
     */
    @NonNull
    Optional<AVCodec> getDecoder(@Nullable AVCodecID id);
}
