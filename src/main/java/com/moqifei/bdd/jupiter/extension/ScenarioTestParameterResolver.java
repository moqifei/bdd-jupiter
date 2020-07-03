package com.moqifei.bdd.jupiter.extension;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import com.moqifei.bdd.jupiter.modle.StoryDetails;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;

public class ScenarioTestParameterResolver implements ParameterResolver, TestWatcher {

	private static final Namespace NAMESPACE = Namespace.create(StoryExtension.class);

	private final ScenarioTestMethodContext methodContext;
	private final Object[] arguments;

	ScenarioTestParameterResolver(ScenarioTestMethodContext methodContext, Object[] arguments) {
		this.methodContext = methodContext;
		this.arguments = arguments;
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		Executable declaringExecutable = parameterContext.getDeclaringExecutable();
		Method testMethod = extensionContext.getTestMethod().orElse(null);

		// Not a @ParameterizedTest method?
		if (!declaringExecutable.equals(testMethod)) {
			return false;
		}

		// Current parameter is an aggregator?
		if (this.methodContext.isAggregator(parameterContext.getIndex())) {
			return true;
		}

		// Ensure that the current parameter is declared before aggregators.
		// Otherwise, a different ParameterResolver should handle it.
		if (this.methodContext.indexOfFirstAggregator() != -1) {
			return parameterContext.getIndex() < this.methodContext.indexOfFirstAggregator();
		}

		// Else fallback to behavior for parameterized test methods without aggregators.
		return parameterContext.getIndex() < this.arguments.length;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {

		Object injectParameter = this.methodContext.resolve(parameterContext, this.arguments);
		if (Scene.class.isAssignableFrom(injectParameter.getClass())) {
			Scene scene = (Scene) injectParameter;
			scene.setMethodName(extensionContext.getRequiredTestMethod().getName())
					.setDescription(extensionContext.getRequiredTestMethod().getAnnotation(ScenarioTest.class).value());
			getStoryDetails(extensionContext).getStore().put(scene.getMethodName(), scene);

		}

		return injectParameter;
	}

	private static StoryDetails getStoryDetails(ExtensionContext context) {
		Class<?> clazz = context.getRequiredTestClass();
		return context.getStore(NAMESPACE).get(clazz.getName(), StoryDetails.class);
	}

	@Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        // do something
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
    	// do something
    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
    	
    	Class<?> clazz = extensionContext.getRequiredTestClass();
    	StoryDetails storyDetails = extensionContext.getStore(NAMESPACE).get(clazz.getName(), StoryDetails.class);
    	Scene scene = (Scene) storyDetails.getStore().get(extensionContext.getRequiredTestMethod().getName());
    	scene.setRunResult("failed");
    	getStoryDetails(extensionContext).getStore().put(scene.getMethodName(), scene);
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
    	
    	Class<?> clazz = extensionContext.getRequiredTestClass();
    	StoryDetails storyDetails = extensionContext.getStore(NAMESPACE).get(clazz.getName(), StoryDetails.class);
    	Scene scene = (Scene) storyDetails.getStore().get(extensionContext.getRequiredTestMethod().getName());
    	scene.setRunResult("succeed");
    	getStoryDetails(extensionContext).getStore().put(scene.getMethodName(), scene);
    }
}
