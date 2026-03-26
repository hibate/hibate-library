package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated element should only be called if the given extension is at least the
 * given version.
 * <p>
 * This annotation is repeatable, and if specified multiple times, you are required to have one of
 * (not all of) the specified extension versions.
 * <p>
 * Example:
 * ```
 * &#064;RequiresExtension(extension  = Build.VERSION_CODES.R, version = 3)
 * fun methodUsingApisFromR() { ... }
 * ```
 * <p>
 * For the special case of [extension] == 0, you can instead use the [RequiresApi] annotation;
 * `@RequiresApi(30)` is equivalent to `@RequiresExtension(extension=0, version=30)`.
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.ANNOTATION_TYPE,
        ElementType.TYPE,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.PACKAGE
})
public @interface RequiresExtension {

    /**
     * The extension SDK ID. This corresponds to the extension id's allowed by
     * [android.os.ext.SdkExtensions.getExtensionVersion], and for id values less than 1_000_000 is
     * one of the [android.os.Build.VERSION_CODES].
     */
    @IntRange(from = 1) int extension();

    /** The minimum version to require */
    @IntRange(from = 1) int version();
}
