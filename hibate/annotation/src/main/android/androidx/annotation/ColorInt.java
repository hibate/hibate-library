package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated element represents a packed color int, `AARRGGBB`. If applied to an
 * int array, every element in the array represents a color integer.
 * <p>
 * Example:
 * ```
 * public abstract void setTextColor(@ColorInt int color)
 * ```
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.PARAMETER,
        ElementType.METHOD,
        ElementType.LOCAL_VARIABLE,
        ElementType.FIELD
})
public @interface ColorInt {
}
