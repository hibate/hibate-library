package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated method or constructor should only be called on the UI thread. If the
 * annotated element is a class, then all methods in the class should be called on the UI thread.
 * <p>
 * Example:
 * ```
 * &#064;UiThread
 * public abstract void setText(@NonNull String text) { ... }
 * ```
 * <p>
 * **Note:** Ordinarily, an app's UI thread is also the main thread. However, under special
 * circumstances, an app's UI thread might not be its main thread; for more information, see
 * [Thread annotations](https://developer.android.com/studio/write/annotations#thread-annotations).
 *
 * @see androidx.annotation.MainThread
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.ANNOTATION_TYPE,
        ElementType.TYPE,
        ElementType.PARAMETER
})
public @interface UiThread {
}
