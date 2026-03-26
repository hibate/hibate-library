package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated element should be a float or double in the given range
 * <p>
 * Example:
 * ```
 * &#064;FloatRange(from=0.0,to=1.0)
 * public float getAlpha() {
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
public @interface FloatRange {

    /** Smallest value. Whether it is inclusive or not is determined by [.fromInclusive] */
    double from() default Double.NEGATIVE_INFINITY;

    /** Largest value. Whether it is inclusive or not is determined by [.toInclusive] */
    double to() default Double.POSITIVE_INFINITY;

    /** Whether the from value is included in the range */
    boolean fromInclusive() default true;

    /** Whether the to value is included in the range */
    boolean toInclusive() default true;
}
