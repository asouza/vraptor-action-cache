package br.com.caelum.vraptor.actioncache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

@Qualifier
@Target({ PARAMETER, FIELD,TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EhCacheVersion {

}
