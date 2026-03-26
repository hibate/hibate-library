package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated element, while not disallowed or deprecated, is one that programmers
 * are generally discouraged from using.
 * <p>
 * Example:
 * ```
 * &#064;Discouraged(message  = "It is much more efficient to retrieve "
 *                        + "resources by identifier than by name.")
 * public void getValue(String name) {
 *     ...
 * }
 * ```
 */
@Retention(RetentionPolicy.SOURCE)
@Target({
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE,
        ElementType.TYPE
})
public @interface Discouraged {

    /**
     * Defines the message to display when an element marked with this annotation is used. An
     * alternative should be provided in the message.
     */
    String message();
}
