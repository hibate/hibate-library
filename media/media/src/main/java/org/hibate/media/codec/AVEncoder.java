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
import org.hibate.media.util.AVFrame;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
public interface AVEncoder extends AVCoder {

    /**
     * 解码数据包
     * @param frame 数据包
     * @return 状态码
     * <p>
     * {@code 0}                 success
     * {@link org.hibate.media.util.Error#EAGAIN}
     *                           input is not accepted in the current state - user must
     *                           read output with avcodec_receive_packet() (once all
     *                           output is read, the packet should be resent, and the
     *                           call will not fail with EAGAIN).
     * {@link org.hibate.media.util.AVError#AVERROR_EOF}
     *                           the encoder has been flushed, and no new frames can
     *                           be sent to it
     * {@link org.hibate.media.util.Error#EINVAL}
     *                           codec not opened, it is a decoder, or requires flush
     * {@link org.hibate.media.util.Error#ENOMEM}
     *                           failed to add packet to internal queue, or similar
     * {@code else} "another negative error code" legitimate encoding errors
     */
    int send(@Nullable AVFrame frame);

    /**
     * 接收数据包
     * @param packet 数据包
     * @return 状态码
     * <p>
     * {@code 0}               success
     * {@link org.hibate.media.util.Error#EAGAIN}
     *                         output is not available in the current state - user must
     *                         try to send input
     * {@link org.hibate.media.util.AVError#AVERROR_EOF}
     *                         the encoder has been fully flushed, and there will be no
     *                         more output packets
     * {@link org.hibate.media.util.Error#EINVAL}
     *                         codec not opened, or it is a decoder
     * {@code else} "another negative error code" legitimate encoding errors
     */
    int receive(@NonNull AVPacket packet);

    /**
     * 清空缓冲区, 发送流结束信号
     * @return 状态码
     */
    int flush();
}
