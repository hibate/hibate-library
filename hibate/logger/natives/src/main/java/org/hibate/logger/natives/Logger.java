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
public interface Logger {

    /**
     * 判断日志输出是否可用
     * @return {@code true} 可用, {@code false} 不可用
     */
    boolean isAvailable();

    /**
     * 判断日志级别是否可以输出
     * @param priority 日志级别
     * @return {@code true} 是, {@code false} 否
     * @see Priority
     */
    boolean isEnabled(Priority priority);

    /**
     * 设置日志输出级别
     * @param priority 日志级别
     * @see Priority
     */
    void setPriority(Priority priority);

    /**
     * 判断文件/行号信息输出是否启用
     * @return {@code true} 启用, {@code false} 禁用
     */
    boolean isLineEnabled();

    /**
     * 使能文件/行号信息输出
     * @param enabled {@code true} 启用, {@code false} 禁用
     */
    void setLineEnabled(boolean enabled);

    /**
     * 添加日志回调
     * @param listener 日志回调
     * @see LoggerListener
     */
    void addLoggerListener(LoggerListener listener);
}
