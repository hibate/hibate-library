package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated method or field can only be accessed when holding the referenced lock.
 * <p>
 * Example:
 * ```
 * final Object objectLock = new Object();
 * <p>
 * &#064;GuardedBy("objectLock")
 * volatile Object object;
 * <p>
 * Object getObject() {
 *     synchronized (objectLock) {
 *         if (object == null) {
 *             object = new Object();
 *         }
 *     }
 *     return object;
 * }
 * ```
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.FIELD,
        ElementType.METHOD
})
public @interface GuardedBy {

    String value();
}
