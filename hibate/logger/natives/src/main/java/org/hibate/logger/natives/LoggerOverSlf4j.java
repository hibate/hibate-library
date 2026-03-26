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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hibate
 * Created on 2018/04/24.
 */
public class LoggerOverSlf4j implements LoggerListener {

    private static final Map<String, Logger> map;

    static {
        map = new ConcurrentHashMap<>();
    }

    protected static Logger getLogger(String tag) {
        if ((tag == null) || (tag.isEmpty())) {
            tag = LoggerOverSlf4j.class.getSimpleName();
        }

        Logger logger;
        if (map.containsKey(tag)) {
            logger = map.get(tag);
        } else {
            logger = LoggerFactory.getLogger(tag);
            map.put(tag, logger);
        }
        return logger;
    }

    @Override
    public void onLogger(String tag, Priority priority, String location, String msg) {
        Logger logger = getLogger(tag);
        if ((logger == null) || (priority == null)) {
            return;
        }

        this.doLogger(logger, priority, location, msg);
    }

    protected void doLogger(Logger logger, Priority priority, String location, String msg) {
        switch (priority) {
            case VERBOSE:
                if (logger.isTraceEnabled()) {
                    logger.trace("{}", this.formatter(location, msg));
                }
                break;
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    logger.debug("{}", this.formatter(location, msg));
                }
                break;
            case INFO:
                if (logger.isInfoEnabled()) {
                    logger.info("{}", this.formatter(location, msg));
                }
                break;
            case WARN:
                if (logger.isWarnEnabled()) {
                    logger.warn("{}", this.formatter(location, msg));
                }
                break;
            case ERROR:
            case FATAL:
                if (logger.isErrorEnabled()) {
                    logger.error("{}", this.formatter(location, msg));
                }
                break;
            case SILENT:
                break;
        }
    }

    protected String formatter(String location, String msg) {
        StringBuilder sb = new StringBuilder();
        if (location != null) {
            sb.append("[")
                    .append(location)
                    .append("]");
        }
        if (msg != null) {
            //noinspection SizeReplaceableByIsEmpty
            if (sb.length() != 0) {
                sb.append(" ");
            }
            sb.append(msg);
        }
        return sb.toString();
    }
}
