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
import org.hibate.media.util.AVFrame;

/**
 * @author Hibate
 * Created on 2024/03/27.
 */
public interface AVDecoder extends AVCoder {

    /**
     * 解码数据包
     * @param packet 数据包
     * @return 状态码
     * <p>
     * {@code 0}                 success
     * {@link org.hibate.media.util.Error#EAGAIN}
     *                           input is not accepted in the current state - user
     *                           must read output with avcodec_receive_frame() (once
     *                           all output is read, the packet should be resent,
     *                           and the call will not fail with EAGAIN).
     * {@link org.hibate.media.util.AVError#AVERROR_EOF}
     *                           the decoder has been flushed, and no new packets can be
     *                           sent to it (also returned if more than 1 flush
     *                           packet is sent)
     * {@link org.hibate.media.util.Error#EINVAL}
     *                           codec not opened, it is an encoder, or requires flush
     * {@link org.hibate.media.util.Error#ENOMEM}
     *                           failed to add packet to internal queue, or similar
     * {@code else} "another negative error code" legitimate decoding errors
     */
    int send(@NonNull AVPacket packet);

    /**
     * 接收数据包
     * @param frame 数据包
     * @return 状态码
     * <p>
     * {@code 0} success, a frame was returned
     * {@link org.hibate.media.util.Error#EAGAIN}
     *                           output is not available in this state - user must
     *                           try to send new input
     * {@link org.hibate.media.util.AVError#AVERROR_EOF}
     *                           the codec has been fully flushed, and there will be
     *                           no more output frames
     * {@link org.hibate.media.util.Error#EINVAL}
     *                           codec not opened, or it is an encoder without the
     *                           &#064;ref  AV_CODEC_FLAG_RECON_FRAME flag enabled
     * {@code else} "other negative error code" legitimate decoding errors
     */
    int receive(@NonNull AVFrame frame);

    /**
     * 清空缓冲区, 发送流结束信号
     * @return 状态码
     */
    int flush();
}
