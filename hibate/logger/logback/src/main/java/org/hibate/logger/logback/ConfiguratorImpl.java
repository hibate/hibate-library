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

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.SubstituteLoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * @author Hibate
 * Created on 2018/03/18.
 */
class ConfiguratorImpl implements Configurator {

    private static final String TAG = "Configurator";

    private SoftReference<Context> reference;
    private Files files;
    private Registry registry;

    public ConfiguratorImpl(@NonNull android.content.Context context) {
        this.initialize(context);
    }

    @NonNull
    @Override
    public Registry getRegistry() {
        return this.registry;
    }

    @NonNull
    @Override
    public String getFilesHome() {
        return (this.files.getFilesDir() == null) ? "" : this.files.getFilesDir().getAbsolutePath();
    }

    @NonNull
    @Override
    public String getFilesHome(@Nullable String type) {
        if (this.files.getFilesDir() == null) {
            return "";
        }
        return ((type == null) || type.isEmpty()) ? this.files.getFilesDir().getAbsolutePath() :
                new File(this.files.getFilesDir(), type).getAbsolutePath();
    }

    @NonNull
    @Override
    public String getLogsHome() {
        return this.getFilesHome("logs");
    }

    @Override
    public void configure() {
        this.configure(LOGBACK);
    }

    @Override
    public void configure(String name) {
        this.configure(name, null);
    }

    @Override
    public void configure(String name, @Nullable String active) {
        if (name == null) {
            return;
        }
        if ((active != null) && !active.isEmpty()) {
            final String suffix = ".xml";
            if (name.endsWith(suffix)) {
                name = name.substring(0, name.length() - suffix.length());
            }
            name += "-" + active + suffix;
        }

        android.content.Context context = this.reference.get();
        if (context == null) {
            return;
        }

        AssetManager assets = context.getAssets();
        try {
            configure(assets.open(name));
        } catch (IOException ioe) {
            Log.w(TAG, "Configure with assets file[" + name + "] failed.", ioe);
        }
    }

    @Override
    public void configure(InputStream inputStream) {
        if (inputStream == null) {
            return;
        }

        LoggerContext context = (LoggerContext) getLoggerFactory();
        if (context == null) {
            return;
        }

        Consumer<LoggerContext> listener = ctx -> {
            this.registry.putProperty("context.files.home", getFilesHome())
                    .putProperty("context.logs.home", getLogsHome());
            Map<String, String> properties = ((RegistryImpl) this.registry).getProperties();
            for (String name : properties.keySet()) {
                ctx.putProperty(name, properties.get(name));
            }
        };

        try {
            context.reset();
            listener.accept(context);
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            configurator.doConfigure(inputStream);
        } catch (JoranException e) {
            Log.w(TAG, "Configure failed.", e);
        }
    }

    @Override
    public void reset() {
        LoggerContext context = (LoggerContext) getLoggerFactory();
        if (context != null) {
            context.reset();
        }
        this.registry.clear();
    }

    protected void initialize(@NonNull android.content.Context context) {
        this.reference = new SoftReference<>(context);
        this.files = new FilesImpl(() -> context);
        this.registry = new RegistryImpl();
    }

    private static ILoggerFactory getLoggerFactory() {
        ILoggerFactory factory = LoggerFactory.getILoggerFactory();
        while (factory instanceof SubstituteLoggerFactory) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for non-substitute logger factory", ex);
            }
            factory = LoggerFactory.getILoggerFactory();
        }
        return factory;
    }

    protected interface Files {

        File getFilesDir();
    }

    protected static class FilesImpl implements Files {

        private final File filesDir;

        public FilesImpl(Supplier<android.content.Context> supplier) {
            this.filesDir = this.getFilesDir(supplier);
        }

        @Override
        public File getFilesDir() {
            return this.filesDir;
        }

        protected File getFilesDir(Supplier<android.content.Context> supplier) {
            Files[] files = this.getFiles(supplier);
            for (Files file : files) {
                File filesDir = file.getFilesDir();
                if (filesDir != null) {
                    return filesDir;
                }
            }
            return null;
        }

        protected Files[] getFiles(Supplier<android.content.Context> supplier) {
            return new Files[] {
                    new ExternalFiles(supplier),
                    new InternalFiles(supplier)
            };
        }
    }

    protected static class ExternalFiles implements Files {

        private final Supplier<android.content.Context> supplier;

        public ExternalFiles(Supplier<android.content.Context> supplier) {
            this.supplier = supplier;
        }

        @Override
        public File getFilesDir() {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                android.content.Context context = supplier.get();
                if (context == null) {
                    return null;
                }
                File filesDir = context.getExternalFilesDir(null);
                filesDir = (filesDir == null) ? null : context.getExternalCacheDir();
                if (filesDir != null) {
                    if (filesDir.getAbsolutePath().endsWith("cache")) {
                        filesDir = new File(filesDir.getParent(), "files");
                    }
                    return filesDir;
                }
            }
            return null;
        }
    }

    protected static class InternalFiles implements Files {

        private final Supplier<android.content.Context> supplier;

        public InternalFiles(Supplier<android.content.Context> supplier) {
            this.supplier = supplier;
        }

        @Override
        public File getFilesDir() {
            android.content.Context context = supplier.get();
            if (context == null) {
                return null;
            }
            return context.getFilesDir();
        }
    }
}
