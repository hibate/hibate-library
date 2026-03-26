package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that any overriding methods should **not** invoke this method, since it is defined to be
 * empty (or perhaps contain other code not intended to be run when overridden).
 * <p>
 * Example:
 * ```
 * &#064;EmptySuper
 * public void onFocus() { }
 * ```
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface EmptySuper {
}
