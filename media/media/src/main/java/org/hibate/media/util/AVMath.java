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

package org.hibate.media.util;

/**
 * @author Hibate
 * Created on 2026/03/23.
 */
public class AVMath {

    private AVMath() {
    }

    /**
     * 利用两个有理数对 64 位整数进行缩放
     * 此操作在数学上等价于 `a * bq / cq`, 此函数等价于使用带 AV_ROUND_NEAR_INF 参数的 av_rescale_q_rnd()
     */
    public static native long rescale(long a, AVRational bq, AVRational cq);
}
