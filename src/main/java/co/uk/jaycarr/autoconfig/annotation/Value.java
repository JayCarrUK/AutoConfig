package co.uk.jaycarr.autoconfig.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a configurable value which can be retrieved and
 * stored using the supplied configuration value path.
 *
 * @author Jay Carr
 * @since 1.0-SNAPSHOT
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Value {

    String path();
}