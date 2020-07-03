package com.moqifei.bdd.jupiter.modle.annotations;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.junit.jupiter.params.provider.ArgumentsSource;

import com.moqifei.bdd.jupiter.extension.Scene;
import com.moqifei.bdd.jupiter.provider.ScenarioJsonArgumentsProvider;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "5.0")
@ArgumentsSource(ScenarioJsonArgumentsProvider.class)
public @interface ScenarioJsonSource {
	/**
	 * The json classpath resources to use as the sources of arguments; must not be
	 * empty.
	 */
	String[] resources();
	
	Class<?> instance() default Scene.class;

	/**
	 * The encoding to use when reading the json files; must be a valid charset.
	 *
	 * <p>Defaults to {@code "UTF-8"}.
	 *
	 * @see java.nio.charset.StandardCharsets
	 */
	String encoding() default "UTF-8";

	String key() default "";
}
