/*
 * Copyright 2018 Contributors

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.moqifei.bdd.jupiter.provider.ScenarioArgumentsProvider;

/**
 * 
 *
 */
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
