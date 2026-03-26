package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated element should only be accessed from within a specific scope (as
 * defined by [Scope]).
 * <p>
 * Example of restricting usage within a library (based on Gradle group ID):
 * ```
 * &#064;RestrictTo(GROUP_ID)
 * public void resetPaddingToInitialValues() { ...
 * ```
 * <p>
 * Example of restricting usage to tests:
 * ```
 * &#064;RestrictTo(Scope.TESTS)
 * public abstract int getUserId();
 * ```
 * <p>
 * Example of restricting usage to subclasses:
 * ```
 * &#064;RestrictTo(Scope.SUBCLASSES)
 * public void onDrawForeground(Canvas canvas) { ...
 * ```
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.ANNOTATION_TYPE,
        ElementType.TYPE,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD
})
public @interface RestrictTo {

    /** The scope(s) to which usage should be restricted. */
    Scope value();

    enum Scope {
        /**
         * Restrict usage to code within the same library (e.g. the same Gradle group ID and
         * artifact ID).
         */
        LIBRARY,

        /**
         * Restrict usage to code within the same group of libraries.
         * <p>
         * This corresponds to the Gradle group ID.
         */
        LIBRARY_GROUP,

        /**
         * Restrict usage to code within packages whose Gradle group IDs share the same prefix up to
         * the last `.` separator.
         * <p>
         * For example, libraries `foo.bar:lib1` and `foo.baz:lib2` share the `foo.` prefix and can
         * therefore use each other's APIs that are restricted to this scope. Similar applies to
         * libraries `com.foo.bar:lib1` and `com.foo.baz:lib2`, which share the `com.foo.` prefix.
         * <p>
         * Library `com.bar.qux:lib3`, however, will not be able to use the restricted API because
         * it only shares the prefix `com.` and not all the way until the last `.` separator.
         */
        LIBRARY_GROUP_PREFIX,

        /**
         * Restrict usage to code within the same group ID (based on Gradle group ID).
         * <p>
         * This is an alias for [LIBRARY_GROUP_PREFIX].
         */
        @Deprecated
        GROUP_ID,

        /**
         * Restrict usage to test source sets or code annotated with the [TESTS] restriction scope.
         * <p>
         * This is equivalent to `@VisibleForTesting(NONE)`.
         */
        TESTS,

        /**
         * Restrict usage to subclasses of the enclosing class.
         * <p>
         * **Note:** This scope should not be used to annotate packages.
         */
        SUBCLASSES,
    }
}
