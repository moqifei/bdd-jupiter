package com.moqifei.bdd.jupiter.extension;

import static java.util.Collections.singletonList;

import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;


public class ScenarioTestInvocationContext implements TestTemplateInvocationContext{
	private final ScenarioTestNameFormatter formatter;
	private final ScenarioTestMethodContext methodContext;
	private final Object[] arguments;

	ScenarioTestInvocationContext(ScenarioTestNameFormatter formatter,
			ScenarioTestMethodContext methodContext, Object[] arguments) {
		this.formatter = formatter;
		this.methodContext = methodContext;
		this.arguments = arguments;
	}

	@Override
	public String getDisplayName(int invocationIndex) {
		return this.formatter.format(invocationIndex, this.arguments);
	}

	@Override
	public List<Extension> getAdditionalExtensions() {
		return singletonList(new ScenarioTestParameterResolver(this.methodContext, this.arguments));
	}
}
