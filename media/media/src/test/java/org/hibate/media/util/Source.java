package org.hibate.media.util;

import androidx.annotation.Nullable;

import java.io.File;

/**
 * @author Hibate
 * Created on 2024/03/30.
 */
public interface Source {

    String getUrl();

    File getFile();

    default File getFile(@Nullable String extension) {
        return null;
    }
}
