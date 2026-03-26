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

package org.hibate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated element should only be called on the given API level or higher.
 * <p>
 * This is similar in purpose to the older `@TargetApi` annotation, but more clearly expresses that
 * this is a requirement on the caller, rather than being used to "suppress" warnings within the
 * method that exceed the `minSdkVersion`.
 *
 * @author Hibate
 * Created on 2026/03/25.
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.ANNOTATION_TYPE,
        ElementType.TYPE,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD
})
public @interface RequiresApi {

    /** The API level to require. Alias for [.api] which allows you to leave out the `api=` part. */
    int value() default 1;

    /** The API level to require */
    int api() default 1;
}
