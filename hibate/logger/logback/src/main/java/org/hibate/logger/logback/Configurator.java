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

package org.hibate.logger.logback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;

/**
 * @author Hibate
 * Created on 2018/03/18.
 */
public interface Configurator {

    String LOGBACK = "logback.xml";

    static Configurator create(@NonNull android.content.Context context) {
        return new ConfiguratorImpl(context);
    }

    @NonNull
    Registry getRegistry();

    @NonNull
    String getFilesHome();

    @NonNull
    String getFilesHome(@Nullable String type);

    @NonNull
    String getLogsHome();

    void configure();

    void configure(String name);

    void configure(String name, @Nullable String active);

    void configure(InputStream inputStream);

    void reset();
}
