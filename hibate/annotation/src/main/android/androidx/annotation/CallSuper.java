package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that any overriding methods should invoke this method as well.
 * <p>
 * Example:
 * ```
 * &#064;CallSuper
 * public abstract void onFocusLost();
 * ```
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.METHOD
})
public @interface CallSuper {
}
