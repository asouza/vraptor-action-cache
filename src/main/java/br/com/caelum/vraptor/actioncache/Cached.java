package br.com.caelum.vraptor.actioncache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cached {

	String key();

    /**
     * The duration the action should be cached for.  Defaults to 0.
     */
    int duration() default 0;

	boolean headers() default true;
}
