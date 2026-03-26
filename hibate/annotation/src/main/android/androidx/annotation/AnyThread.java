package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated method can be called from any thread (e.g. it is "thread safe".) If
 * the annotated element is a class, then all methods in the class can be called from any thread.
 * <p>
 * The main purpose of this method is to indicate that you believe a method can be called from any
 * thread; static tools can then check that nothing you call from within this method or class have
 * more strict threading requirements.
 * <p>
 * Example:
 * ```
 * &#064;AnyThread
 * public void deliverResult(D data) { ... }
 * ```
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.ANNOTATION_TYPE,
        ElementType.TYPE,
        ElementType.PARAMETER
})
public @interface AnyThread {
}
