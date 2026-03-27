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

import androidx.annotation.Nullable;
import org.hibate.media.util.AVDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Hibate
 * Created on 2024/03/26.
 */
public class AVDictionaryImpl implements AVDictionary {

    private static final Logger logger =
            LoggerFactory.getLogger(AVDictionary.class);

    private final Map<String, Object> map;

    public AVDictionaryImpl() {
        this(0);
    }

    public AVDictionaryImpl(int capacity) {
        this.map = (capacity > 0) ? new HashMap<>(capacity) : new HashMap<>();
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(String key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public Object get(String key) {
        return this.map.get(key);
    }

    @Override
    public int getInt(String key) {
        return getInt(key, 0);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        Object o = this.map.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (o instanceof String) ? Integer.parseInt((String) o) : (Integer) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Integer", defaultValue, e);
            return defaultValue;
        }
    }

    @Override
    public long getLong(String key) {
        return getLong(key, 0L);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        Object o = this.map.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (o instanceof String) ? Long.parseLong((String) o) : (Long) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Long", defaultValue, e);
            return defaultValue;
        }
    }

    @Override
    public String getString(String key) {
        final Object o = this.map.get(key);
        try {
            return (String) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "String", e);
            return null;
        }
    }

    @Override
    public String getString(String key, String defaultValue) {
        final String s = getString(key);
        return (s == null) ? defaultValue : s;
    }

    @Override
    public void put(String key, int value) {
        this.map.put(key, value);
    }

    @Override
    public void put(String key, long value) {
        this.map.put(key, value);
    }

    @Override
    public void put(String key, String value) {
        this.map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(AVDictionary m) {
        if (m == null) {
            return;
        }
        Set<Map.Entry<String, Object>> entrySet = m.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getKey() == null) {
                continue;
            }
            this.map.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

    @SuppressWarnings("SameParameterValue")
    void typeWarning(String key, @Nullable Object value, String className, RuntimeException e) {
        typeWarning(key, value, className, "<null>", e);
    }

    // Log a message if the value was non-null but not of the expected type
    void typeWarning(String key, @Nullable Object value, String className,
                     Object defaultValue, RuntimeException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Key ");
        sb.append(key);
        sb.append(" expected ");
        sb.append(className);
        if (value != null) {
            sb.append(" but value was a ");
            sb.append(value.getClass().getName());
        } else {
            sb.append(" but value was of a different type");
        }
        sb.append(".  The default value ");
        sb.append(defaultValue);
        sb.append(" was returned.");
        if (logger.isWarnEnabled()) {
            logger.warn(sb.toString());
            logger.warn("Attempt to cast generated internal exception:", e);
        }
    }

    @Override
    public native AVDictionary clone();
}
