package com.moqifei.bdd.jupiter.extension;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import com.google.common.eventbus.EventBus;
import com.moqifei.bdd.jupiter.modle.StoryDetails;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.report.multiple.JUnitPlatformRunnerListener;

public class ScenarioTestParameterResolver implements ParameterResolver, AfterEachCallback {

	private static final Namespace NAMESPACE = Namespace.create(StoryExtension.class);

	private final ScenarioTestMethodContext methodContext;
	private final Object[] arguments;

	protected ScenarioTestParameterResolver(ScenarioTestMethodContext methodContext, Object[] arguments) {
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
	public void afterEach(ExtensionContext context) throws Exception {
		Scene scene = (Scene)getStoryDetails(context).getStore().get(context.getRequiredTestMethod().getName());
        String key = context.getRequiredTestClass().getName()+"#"+context.getRequiredTestMethod().getName();
        scene.setKey(key);
		
		 // 定义一个EventBus对象，这里的Joker是该对象的id
	    EventBus eventBus = new EventBus("Scene");
	    // 向上述EventBus对象中注册一个监听对象   
	    eventBus.register(new JUnitPlatformRunnerListener());
	    // 使用EventBus发布一个事件，该事件会给通知到所有注册的监听者
	    eventBus.post(scene);
	}
}
