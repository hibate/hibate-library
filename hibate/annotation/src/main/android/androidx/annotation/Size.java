package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated element should have a given size or length. Note that "-1" means
 * "unset". Typically used with a parameter or return value of type array or collection.
 * <p>
 * Example:
 * ```
 * public void getLocationInWindow(@Size(2) int[] location) {
 *     ...
 * }
 * ```
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE,
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE
})
public @interface Size {

    /** An exact size (or -1 if not specified) */
    long value() default -1;

    /** A minimum size, inclusive */
    long min() default Long.MIN_VALUE;

    /** A maximum size, inclusive */
    long max() default Long.MAX_VALUE;

    /** The size must be a multiple of this factor */
    long multiple() default 1;
}
