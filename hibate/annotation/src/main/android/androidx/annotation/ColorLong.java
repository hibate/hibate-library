package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated element represents a packed color long. If applied to a long array,
 * every element in the array represents a color long. For more information on how colors are packed
 * in a long, please refer to the documentation of the [android.graphics.Color] class.
 * <p>
 * Example:
 * ```
 * public void setFillColor(@ColorLong long color);
 * ```
 * <p>
 * android.graphics.Color
 */
@Retention(RetentionPolicy.SOURCE)
@Target({
        ElementType.PARAMETER,
        ElementType.METHOD,
        ElementType.LOCAL_VARIABLE,
        ElementType.FIELD
})
public @interface ColorLong {
}
