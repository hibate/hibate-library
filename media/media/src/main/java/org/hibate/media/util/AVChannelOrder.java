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
public enum AVChannelOrder {

    /**
     * Only the channel count is specified, without any further information
     * about the channel order.
     */
    AV_CHANNEL_ORDER_UNSPEC(0),
    /**
     * The native channel order, i.e. the channels are in the same order in
     * which they are defined in the AVChannel enum. This supports up to 63
     * different channels.
     */
    AV_CHANNEL_ORDER_NATIVE(1),
    /**
     * The channel order does not correspond to any other predefined order and
     * is stored as an explicit map. For example, this could be used to support
     * layouts with 64 or more channels, or with empty/skipped (AV_CHAN_SILENCE)
     * channels at arbitrary positions.
     */
    AV_CHANNEL_ORDER_CUSTOM(2),
    /**
     * The audio is represented as the decomposition of the sound field into
     * spherical harmonics. Each channel corresponds to a single expansion
     * component. Channels are ordered according to ACN (Ambisonic Channel
     * Number).
     * <p>
     * The channel with the index n in the stream contains the spherical
     * harmonic of degree l and order m given by
     * &#064;code
     *   l   = floor(sqrt(n)),
     *   m   = n - l * (l + 1).
     * &#064;endcode
     * <p>
     * Conversely given a spherical harmonic of degree l and order m, the
     * corresponding channel index n is given by
     * &#064;code
     *   n = l * (l + 1) + m.
     * &#064;endcode
     * <p>
     * Normalization is assumed to be SN3D (Schmidt Semi-Normalization)
     * as defined in AmbiX format $ 2.1.
     */
    AV_CHANNEL_ORDER_AMBISONIC(3),
    ;

    private final int channelOrder;

    AVChannelOrder(int channelOrder) {
        this.channelOrder = channelOrder;
    }

    public int getChannelOrder() {
        return channelOrder;
    }

    public static AVChannelOrder of(int channelOrder) {
        for (AVChannelOrder order : AVChannelOrder.values()) {
            if (order.getChannelOrder() == channelOrder) {
                return order;
            }
        }
        return AVChannelOrder.AV_CHANNEL_ORDER_UNSPEC;
    }
}
