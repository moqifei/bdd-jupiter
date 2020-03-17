package com.moqifei.bdd.jupiter.provider;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.junit.jupiter.params.provider.ArgumentsSource;

import com.moqifei.bdd.jupiter.extension.Scene;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "5.0")
@ArgumentsSource(ScenarioArgumentsProvider.class)
public @interface ScenarioSource {
	/**
	 * The enum type that serves as the source of the enum constants.
	 *
	 * @see #names
	 * @see #mode
	 */
	Class<? extends Scene>[] value() default {};
}
