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

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Hibate
 * Created on 2026/03/20.
 */
public interface AVLogger {

    /**
     * 不输出
     */
    int AV_LOG_QUIET = -8;
    /**
     * 出现崩溃的严重问题
     */
    int AV_LOG_PANIC = 0;
    /**
     * 出现无法恢复的错误
     */
    int AV_LOG_FATAL = 8;
    /**
     * 出现无法无损恢复的错误
     */
    int AV_LOG_ERROR = 16;
    /**
     * 警告消息
     */
    int AV_LOG_WARNING = 24;
    /**
     * 标准消息
     */
    int AV_LOG_INFO = 32;
    /**
     * 详细消息
     */
    int AV_LOG_VERBOSE = 40;
    /**
     * 仅对 libav* 开发者有用的内容
     */
    int AV_LOG_DEBUG = 48;
    /**
     * 极度详细的调试输出, 适用于 libav* 开发
     */
    int AV_LOG_TRACE = 56;

    @IntDef({AV_LOG_QUIET, AV_LOG_PANIC, AV_LOG_FATAL, AV_LOG_ERROR, AV_LOG_WARNING,
            AV_LOG_INFO, AV_LOG_VERBOSE, AV_LOG_DEBUG, AV_LOG_TRACE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Priority {}

    /**
     * 跳过重复消息
     */
    int AV_LOG_SKIP_REPEATED = 1;
    /**
     * 输出日志级别
     */
    int AV_LOG_PRINT_LEVEL = 2;
    /**
     * 输出系统时间
     */
    int AV_LOG_PRINT_TIME = 4;
    /**
     * 输出系统日期和时间
     */
    int AV_LOG_PRINT_DATETIME = 8;

    @IntDef({AV_LOG_SKIP_REPEATED, AV_LOG_PRINT_LEVEL, AV_LOG_PRINT_TIME, AV_LOG_PRINT_DATETIME})
    @Retention(RetentionPolicy.SOURCE)
    @interface Flags {}

    /**
     * 获取日志输出级别
     * @return 日志输出级别
     */
    @Priority
    int getPriority();

    /**
     * 设置日志输出级别
     * @param priority 日志级别
     * @return {@link AVLogger}
     */
    @SuppressWarnings("UnusedReturnValue")
    AVLogger setPriority(@Priority int priority);

    /**
     * 获取日志标记
     * @return 返回日志标记
     */
    @Flags
    int getFlags();

    /**
     * 获取日志标记
     * @param flags 日志标记
     * @return {@link AVLogger}
     */
    AVLogger setFlags(@Flags int flags);
}
