package com.moqifei.bdd.jupiter.provider;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.JUnitException;
import org.junit.platform.commons.util.ReflectionUtils;

import com.moqifei.bdd.jupiter.extension.Scene;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioSource;

public class ScenarioArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<ScenarioSource> {

	private Class<? extends Scene>[] sceneArguments;
	
	@Override
	public void accept(ScenarioSource scenarioSource) {
		sceneArguments = scenarioSource.value();
		
	}

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {		
		return Arrays.stream(this.sceneArguments)
				.map(this::instantiateSceneProvider)
				.map(ScenarioArgumentsProvider::toArguments);
		
	}

	private Scene instantiateSceneProvider(Class<? extends Scene> clazz) {
		try {
			return ReflectionUtils.newInstance(clazz);
		}
		catch (Exception ex) {
			if (ex instanceof NoSuchMethodException) {
				String message = String.format("Failed to find a no-argument constructor for ArgumentsProvider [%s]. "
						+ "Please ensure that a no-argument constructor exists and "
						+ "that the class is either a top-level class or a static nested class",
					clazz.getName());
				throw new JUnitException(message, ex);
			}
			throw ex;
		}
	}

	private static Arguments toArguments(Object item) {

		// Nothing to do except cast.
		if (item instanceof Arguments) {
			return (Arguments) item;
		}

		// Pass all multidimensional arrays "as is", in contrast to Object[].
		// See https://github.com/junit-team/junit5/issues/1665
		if (ReflectionUtils.isMultidimensionalArray(item)) {
			return arguments(item);
		}

		// Special treatment for one-dimensional reference arrays.
		// See https://github.com/junit-team/junit5/issues/1665
		if (item instanceof Object[]) {
			return arguments((Object[]) item);
		}

		// Pass everything else "as is".
		return arguments(item);
	}
}
