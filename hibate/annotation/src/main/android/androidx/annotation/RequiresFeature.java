package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated element requires one or more features. This is used to auto-generate
 * documentation, and more importantly: to ensure correct usage in application code, where lint and
 * Android Studio can check that calls marked with this annotation is surrounded by has-feature
 * calls, referenced via the [RequiresFeature.enforcement] attribute.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({
        ElementType.ANNOTATION_TYPE,
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR
})
public @interface RequiresFeature {

    /** The name of the feature that is required. */
    String name();

    /**
     * Defines the name of the method that should be called to check whether the feature is
     * available, using the same signature format as javadoc. The feature checking method can have
     * multiple parameters, but the feature name parameter must be of type String and must also be
     * the first String-type parameter.
     */
    String enforcement();
}
