package com.moqifei.bdd.jupiter.provider;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.PreconditionViolationException;
import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.commons.util.ReflectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.io.CharStreams;
import com.moqifei.bdd.jupiter.extension.Scene;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioJsonSource;

public class ScenarioJsonArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<ScenarioJsonSource> {

	private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;

	private ScenarioJsonSource annotation;
	private String[] resources;
	private Charset charset;
	private String key;
	@SuppressWarnings("rawtypes")
	private Class clazzClass;

	ScenarioJsonArgumentsProvider() {
		this(Class::getResourceAsStream);
	}

	ScenarioJsonArgumentsProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
		this.inputStreamProvider = inputStreamProvider;
	}

	@Override
	public void accept(ScenarioJsonSource scenarioJsonSource) {
		this.annotation = scenarioJsonSource;
		resources = scenarioJsonSource.resources();
		clazzClass = scenarioJsonSource.instance();
		key = scenarioJsonSource.key();
		try {
			this.charset = Charset.forName(scenarioJsonSource.encoding());
		} catch (Exception ex) {
			throw new PreconditionViolationException("The charset supplied in " + this.annotation + " is invalid", ex);
		}

	}

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		// @formatter:off
		return Arrays.stream(resources).map(resource -> openInputStream(context, resource))
				.map(this::parseJsonInputStream).map(this::toInstance).map(this::toScene)
				.map(ScenarioJsonArgumentsProvider::toArguments);
	}

	@SuppressWarnings("unchecked")
	private Object toInstance(String jsonStr) {
		return JSON.parseObject(jsonStr, clazzClass);
	}

	private Scene toScene(Object object) {
		Scene scene = ReflectionUtils.newInstance(Scene.class);
		scene.put(key, object);
		return scene;
	}

	private InputStream openInputStream(ExtensionContext context, String resource) {
		Preconditions.notBlank(resource, "Classpath resource [" + resource + "] must not be null or blank");
		Class<?> testClass = context.getRequiredTestClass();
		return Preconditions.notNull(inputStreamProvider.apply(testClass, resource),
				() -> "Classpath resource [" + resource + "] does not exist");
	}

	private String parseJsonInputStream(InputStream inputStream) {
		String lineString = "";
		try {
			lineString = CharStreams.toString(new InputStreamReader(inputStream, charset));
		} catch (Throwable throwable) {
			throw new JsonParsingException("Failed to parse Json input configured via " + annotation, throwable);
		}
		return lineString;

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
