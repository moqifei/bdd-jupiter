package com.moqifei.bdd.jupiter.extension;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumerInitializer;
import org.junit.platform.commons.JUnitException;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.commons.util.ReflectionUtils;

import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;

import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;
import static org.junit.platform.commons.util.AnnotationUtils.findRepeatableAnnotations;
import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;

public class ScenarioTestExtension implements TestTemplateInvocationContextProvider {

	private static final String METHOD_CONTEXT_KEY = "context";
	
	@Override
	public boolean supportsTestTemplate(ExtensionContext context) {
		if (!context.getTestMethod().isPresent()) {
			return false;
		}

		Method testMethod = context.getTestMethod().get();
		if (!isAnnotated(testMethod, ScenarioTest.class)) {
			return false;
		}

		ScenarioTestMethodContext methodContext = new ScenarioTestMethodContext(testMethod);

		Preconditions.condition(methodContext.hasPotentiallyValidSignature(),
				() -> String.format(
						"@ScenarioTest method [%s] declares formal parameters in an invalid order: "
								+ "argument aggregators must be declared after any indexed arguments "
								+ "and before any arguments resolved by another ParameterResolver.",
						testMethod.toGenericString()));

		getStore(context).put(METHOD_CONTEXT_KEY, methodContext);

		return true;
	}

	@Override
	public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
			ExtensionContext extensionContext) {
		Method templateMethod = extensionContext.getRequiredTestMethod();
		String displayName = extensionContext.getDisplayName();
		ScenarioTestMethodContext methodContext = getStore(extensionContext)//
				.get(METHOD_CONTEXT_KEY, ScenarioTestMethodContext.class);
		ScenarioTestNameFormatter formatter = createNameFormatter(templateMethod, displayName);
		AtomicLong invocationCount = new AtomicLong(0);

		// @formatter:off
		return findRepeatableAnnotations(templateMethod, ArgumentsSource.class).stream().map(ArgumentsSource::value)
				.map(this::instantiateArgumentsProvider)
				.map(provider -> AnnotationConsumerInitializer.initialize(templateMethod, provider))
				.flatMap(provider -> arguments(provider, extensionContext)).map(Arguments::get)
				.map(arguments -> consumedArguments(arguments, methodContext))
				.map(arguments -> createInvocationContext(formatter, methodContext, arguments))
				.peek(invocationContext -> invocationCount.incrementAndGet())
				.onClose(() -> Preconditions.condition(invocationCount.get() > 0,
						"Configuration error: You must configure at least one set of arguments for this @ScenarioTest"));
		// @formatter:on
	}

	private ArgumentsProvider instantiateArgumentsProvider(Class<? extends ArgumentsProvider> clazz) {
		try {
			return ReflectionUtils.newInstance(clazz);
		} catch (Exception ex) {
			if (ex instanceof NoSuchMethodException) {
				String message = String.format(
						"Failed to find a no-argument constructor for ArgumentsProvider [%s]. "
								+ "Please ensure that a no-argument constructor exists and "
								+ "that the class is either a top-level class or a static nested class",
						clazz.getName());
				throw new JUnitException(message, ex);
			}
			throw ex;
		}
	}

	private ExtensionContext.Store getStore(ExtensionContext context) {
		return context.getStore(Namespace.create(ScenarioTestExtension.class, context.getRequiredTestMethod()));
	}

	private TestTemplateInvocationContext createInvocationContext(ScenarioTestNameFormatter formatter,
			ScenarioTestMethodContext methodContext, Object[] arguments) {
		return new ScenarioTestInvocationContext(formatter, methodContext, arguments);
	}

	private ScenarioTestNameFormatter createNameFormatter(Method templateMethod, String displayName) {
		ScenarioTest ScenarioTest = findAnnotation(templateMethod, ScenarioTest.class).get();
		String pattern = Preconditions.notBlank(ScenarioTest.name().trim(),
				() -> String.format(
						"Configuration error: @ScenarioTest on method [%s] must be declared with a non-empty name.",
						templateMethod));
		return new ScenarioTestNameFormatter(pattern, displayName);
	}

	protected static Stream<? extends Arguments> arguments(ArgumentsProvider provider, ExtensionContext context) {
		try {
			return provider.provideArguments(context);
		} catch (Exception e) {
			throw ExceptionUtils.throwAsUncheckedException(e);
		}
	}

	private Object[] consumedArguments(Object[] arguments, ScenarioTestMethodContext methodContext) {
		int parameterCount = methodContext.getParameterCount();
		return methodContext.hasAggregator() ? arguments
				: (arguments.length > parameterCount ? Arrays.copyOf(arguments, parameterCount) : arguments);
	}
	
}
