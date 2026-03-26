package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated method should only be called on the main thread. If the annotated
 * element is a class, then all methods in the class should be called on the main thread.
 * <p>
 * Example:
 * ```
 * &#064;MainThread
 * public void deliverResult(D data) { ... }
 * ```
 * <p>
 * **Note:** Ordinarily, an app's main thread is also the UI thread. However, under special
 * circumstances, an app's main thread might not be its UI thread; for more information, see
 * [Thread annotations](https://developer.android.com/studio/write/annotations#thread-annotations).
 *
 * @see androidx.annotation.UiThread
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.ANNOTATION_TYPE,
        ElementType.TYPE,
        ElementType.PARAMETER
})
public @interface MainThread {
}
