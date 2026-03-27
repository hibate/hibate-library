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

package org.hibate.media.util.impl;

import org.hibate.media.util.AVChannelLayout;
import org.hibate.media.util.AVChannelOrder;

/**
 * @author Hibate
 * Created on 2024/03/30.
 */
class AVChannelLayoutImpl implements AVChannelLayout {

    /**
     * AVChannelLayout
     */
    private long ctx;

    @Override
    public native String getDescription();

    @Override
    public AVChannelOrder getChannelOrder() {
        return AVChannelOrder.of(this._getChannelOrder());
    }

    @Override
    public AVChannelLayout setChannelOrder(AVChannelOrder order) {
        if (order != null) {
            this._setChannelOrder(order.getChannelOrder());
        }
        return this;
    }

    @Override
    public native int getChannels();

    @Override
    public AVChannelLayout setChannels(int channels) {
        this._setChannels(channels);
        return this;
    }

    @Override
    public AVChannelLayout setDefaults(int channels) {
        return this;
    }

    private native int _getChannelOrder();
    private native int _setChannelOrder(int order);
    private native int _setChannels(int channels);
    private native int _setDefaults(int channels);
}
