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
public final class LibraryLoader {

    private String[] nativeLibraries;
    private boolean loadAttempted;
    private boolean isAvailable;

    /**
     * @param libraries The names of the libraries to load.
     */
    public LibraryLoader(String... libraries) {
        this.nativeLibraries = libraries;
    }

    /**
     * Overrides the names of the libraries to load. Must be called before any call to
     * {@link #isAvailable()}.
     * @param libraries The names of the libraries to load.
     */
    public synchronized void setLibraries(String... libraries) {
        if (this.loadAttempted) {
            throw new IllegalStateException("Cannot set libraries after loading");
        }
        this.nativeLibraries = libraries;
    }

    /**
     * @return Returns whether the underlying libraries are available, loading them if necessary.
     */
    public synchronized boolean isAvailable() {
        return this.isAvailable(false);
    }

    /**
     * @param traceable Traceable if load failed.
     * @return Returns whether the underlying libraries are available, loading them if necessary.
     */
    public synchronized boolean isAvailable(boolean traceable) {
        if (this.loadAttempted) {
            return this.isAvailable;
        }
        this.loadAttempted = true;
        try {
            for (String lib : this.nativeLibraries) {
                this.loadLibrary(lib, traceable);
            }
            this.isAvailable = true;
        } catch (UnsatisfiedLinkError exception) {
            if (traceable) {
                exception.printStackTrace(System.err);
            }
        }
        return this.isAvailable;
    }

    private void loadLibrary(String name, boolean traceable) {
        final String os = System.getProperty("os.name");
        final boolean WINDOWS = (os != null) && os.startsWith("Windows");
        String[] names = (WINDOWS && (!name.startsWith("lib"))) ?
                new String[] { String.format("lib%s", name), name } : new String[] { name };
        for (String library : names) {
            try {
                System.loadLibrary(library);
                return;
            } catch (UnsatisfiedLinkError exception) {
                if (traceable) {
                    exception.printStackTrace(System.err);
                }
            }
        }
    }
}
