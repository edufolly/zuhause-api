package zuhause.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Eduardo Folly
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {

    String value();
}
