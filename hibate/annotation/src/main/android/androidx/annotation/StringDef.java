package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated String element, represents a logical type and that its value should be
 * one of the explicitly named constants.
 * <p>
 * Example:
 * ```
 * &#064;Retention(SOURCE)
 * &#064;StringDef(
 *     POWER_SERVICE,
 *     WINDOW_SERVICE,
 *     LAYOUT_INFLATER_SERVICE
 * })
 * public @interface ServiceName {}
 * public static final String POWER_SERVICE = "power";
 * public static final String WINDOW_SERVICE = "window";
 * public static final String LAYOUT_INFLATER_SERVICE = "layout_inflater";
 * ...
 * public abstract Object getSystemService(@ServiceName String name);
 * ```
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface StringDef {

    /** Defines the allowed constants for this element */
    String[] value() default {};

    /**
     * Whether any other values are allowed. Normally this is not the case, but this allows you to
     * specify a set of expected constants, which helps code completion in the IDE and documentation
     * generation and so on, but without flagging compilation warnings if other values are
     * specified.
     */
    boolean open() default false;
}
