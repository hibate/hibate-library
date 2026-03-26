package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that this class or method is only `open` to allow unit testing. Any subclass or override
 * is only allowed from tests.
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
public @interface OpenForTesting {
}
