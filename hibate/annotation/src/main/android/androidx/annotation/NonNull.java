package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that a parameter, field or method return value can never be null.
 * <p>
 * This is a marker annotation and it has no specific attributes.
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.FIELD,
        ElementType.LOCAL_VARIABLE,
        ElementType.ANNOTATION_TYPE,
        ElementType.PACKAGE
})
public @interface NonNull {
}
