package androidx.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Typedef for the [VisibleForTesting.otherwise] attribute. */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        VisibleForTesting.PRIVATE,
        VisibleForTesting.PACKAGE_PRIVATE,
        VisibleForTesting.PROTECTED,
        VisibleForTesting.NONE
})
@RestrictTo(RestrictTo.Scope.LIBRARY)
public @interface ProductionVisibility {
}
