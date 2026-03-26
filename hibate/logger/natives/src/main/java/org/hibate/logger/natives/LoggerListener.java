/*
 * Copyright (C) 2018 Hibate <ycaia86@126.com>
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

package org.hibate.logger.natives;

/**
 * @author Hibate
 * Created on 2018/04/24.
 */
public interface LoggerListener {

    /**
     * 触发日志输出
     * @param tag 标签
     * @param priority 日志级别
     * @param location 文件/行号信息(禁用时为 {@code null})
     * @param msg 日志信息
     */
    void onLogger(String tag, Priority priority, String location, String msg);
}
