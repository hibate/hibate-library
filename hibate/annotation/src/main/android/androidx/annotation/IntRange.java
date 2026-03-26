package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated element should be an int or long in the given range.
 * <p>
 * Example:
 * ```
 * &#064;IntRange(from=0,to=255)
 * public int getAlpha() {
 *     ...
 * }
 * ```
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.FIELD,
        ElementType.LOCAL_VARIABLE,
        ElementType.ANNOTATION_TYPE
})
public @interface IntRange {

    /** Smallest value, inclusive */
    long from() default Long.MIN_VALUE;

    /** Largest value, inclusive */
    long to() default Long.MAX_VALUE;
}
