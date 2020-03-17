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

package com.moqifei.bdd.jupiter.modle;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import com.moqifei.bdd.jupiter.extension.ScenarioTestExtension;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a use case in a {@link Story}.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@TestTemplate
@ExtendWith(ScenarioTestExtension.class)
public @interface ScenarioTest {

	/**
	 * Placeholder for the {@linkplain org.junit.jupiter.api.TestInfo#getDisplayName
	 * display name} of a {@code @ParameterizedTest} method: <code>{displayName}</code>
	 *
	 * @see #name
	 * @since 5.3
	 */
	String DISPLAY_NAME_PLACEHOLDER = "{displayName}";

	/**
	 * Placeholder for the current invocation index of a {@code @ParameterizedTest}
	 * method (1-based): <code>{index}</code>
	 *
	 * @see #name
	 * @since 5.3
	 */
	String INDEX_PLACEHOLDER = "{index}";

	/**
	 * Placeholder for the complete, comma-separated arguments list of the
	 * current invocation of a {@code @ParameterizedTest} method:
	 * <code>{arguments}</code>
	 *
	 * @see #name
	 * @since 5.3
	 */
	String ARGUMENTS_PLACEHOLDER = "{arguments}";

	/**
	 * Default display name pattern for the current invocation of a
	 * {@code @ParameterizedTest} method: {@value}
	 *
	 * <p>Note that the default pattern does <em>not</em> include the
	 * {@linkplain #DISPLAY_NAME_PLACEHOLDER display name} of the
	 * {@code @ParameterizedTest} method.
	 *
	 * @see #name
	 * @see #DISPLAY_NAME_PLACEHOLDER
	 * @see #INDEX_PLACEHOLDER
	 * @see #ARGUMENTS_PLACEHOLDER
	 * @since 5.3
	 */
	String DEFAULT_DISPLAY_NAME = "[" + INDEX_PLACEHOLDER + "] " + ARGUMENTS_PLACEHOLDER;
	
    /**
     * @return representation of the use case in a plain human language.
     */
	String name() default DEFAULT_DISPLAY_NAME;

	String value();
}
