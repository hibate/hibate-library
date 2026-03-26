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

package org.hibate.logger.logback.core;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * @author Hibate
 * Created on 2018/03/18.
 */
public class ConsoleAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    static final String ANONYMOUS_TAG = "null";
    static final int TAG_MAX_LENGTH = 23;
    private boolean debuggable;

    public ConsoleAppender() {
    }

    public void setDebuggable(boolean debuggable) {
        this.debuggable = debuggable;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (this.isStarted()) {
            switch (event.getLevel().levelInt) {
                case Level.ALL_INT:
                case Level.TRACE_INT:
                    this.appendInternal(Log.VERBOSE, event);
                    break;
                case Level.DEBUG_INT:
                    this.appendInternal(Log.DEBUG, event);
                    break;
                case Level.INFO_INT:
                    this.appendInternal(Log.INFO, event);
                    break;
                case Level.WARN_INT:
                    this.appendInternal(Log.WARN, event);
                    break;
                case Level.ERROR_INT:
                    this.appendInternal(Log.ERROR, event);
                    break;
                case Level.OFF_INT:
                default:
                    break;
            }
        }
    }

    protected void appendInternal(int priority, ILoggingEvent event) {
        String tag = loggerNameToTag(event.getLoggerName());
        if (this.debuggable || Log.isLoggable(tag, priority)) {
            Throwable throwable = null;
            if ((event.getThrowableProxy() != null) &&
                    (event.getThrowableProxy() instanceof ThrowableProxy)) {
                throwable = ((ThrowableProxy) event.getThrowableProxy()).getThrowable();
            }

            this.appendInternal(tag, priority, event, throwable);
        }
    }

    protected void appendInternal(String tag, int priority, ILoggingEvent event, Throwable throwable) {
        switch (priority) {
            case Log.VERBOSE:
                if (throwable == null) {
                    Log.v(tag, event.getFormattedMessage());
                } else {
                    Log.v(tag, event.getFormattedMessage(), throwable);
                }
                break;
            case Log.DEBUG:
                if (throwable == null) {
                    Log.d(tag, event.getFormattedMessage());
                } else {
                    Log.d(tag, event.getFormattedMessage(), throwable);
                }
                break;
            case Log.INFO:
                if (throwable == null) {
                    Log.i(tag, event.getFormattedMessage());
                } else {
                    Log.i(tag, event.getFormattedMessage(), throwable);
                }
                break;
            case Log.WARN:
                if (throwable == null) {
                    Log.w(tag, event.getFormattedMessage());
                } else {
                    Log.w(tag, event.getFormattedMessage(), throwable);
                }
                break;
            case Log.ERROR:
                if (throwable == null) {
                    Log.e(tag, event.getFormattedMessage());
                } else {
                    Log.e(tag, event.getFormattedMessage(), throwable);
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    static String loggerNameToTag(String loggerName) {
        // Anonymous logger
        if (loggerName == null) {
            return ANONYMOUS_TAG;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return loggerName;
        }

        int length = loggerName.length();
        if (length <= TAG_MAX_LENGTH) {
            return loggerName;
        }

        int tagLength = 0;
        int lastTokenIndex = 0;
        int lastPeriodIndex;
        StringBuilder tagName = new StringBuilder(TAG_MAX_LENGTH + 3);
        while ((lastPeriodIndex = loggerName.indexOf('.', lastTokenIndex)) != -1) {
            tagName.append(loggerName.charAt(lastTokenIndex));
            // token of one character appended as is otherwise truncate it to one character
            int tokenLength = lastPeriodIndex - lastTokenIndex;
            if (tokenLength > 1) {
                tagName.append('*');
            }
            tagName.append('.');
            lastTokenIndex = lastPeriodIndex + 1;

            // check if name is already too long
            tagLength = tagName.length();
            if (tagLength > TAG_MAX_LENGTH) {
                return getSimpleName(loggerName);
            }
        }

        // Either we had no useful dot location at all
        // or last token would exceed TAG_MAX_LENGTH
        int tokenLength = length - lastTokenIndex;
        if (tagLength == 0 || (tagLength + tokenLength) > TAG_MAX_LENGTH) {
            return getSimpleName(loggerName);
        }

        // last token (usually class name) appended as is
        tagName.append(loggerName, lastTokenIndex, length);
        return tagName.toString();
    }

    static String getSimpleName(String loggerName) {
        // Take leading part and append '*' to indicate that it was truncated
        int length = loggerName.length();
        int lastPeriodIndex = loggerName.lastIndexOf('.');
        return lastPeriodIndex != -1 && length - (lastPeriodIndex + 1) <= TAG_MAX_LENGTH ? loggerName.substring(lastPeriodIndex + 1) : '*' + loggerName
                .substring(length - TAG_MAX_LENGTH + 1);
    }
}
