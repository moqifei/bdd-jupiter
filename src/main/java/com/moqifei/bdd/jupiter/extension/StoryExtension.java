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

package com.moqifei.bdd.jupiter.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import com.moqifei.bdd.jupiter.modle.StoryDetails;
import com.moqifei.bdd.jupiter.modle.annotations.Story;

import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

import java.util.Optional;

/**
 * A custom extension that allow test authors to create and run behaviors and
 * stories i.e. BDD specification tests.
 */
public class StoryExtension implements BeforeAllCallback, AfterAllCallback{

	private static final Namespace NAMESPACE = Namespace.create(StoryExtension.class);

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {

		if (!isStory(context)) {
			throw new Exception(
					"Use @Story annotation to use StoryExtension. Class: " + context.getRequiredTestClass());
		}

		Class<?> clazz = context.getRequiredTestClass();
		Story story = clazz.getAnnotation(Story.class);

		StoryDetails storyDetails = new StoryDetails().setName(story.name()).setDescription(story.description())
				.setStoryClass(clazz);

		context.getStore(NAMESPACE).put(clazz.getName(), storyDetails);
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		if (!isStory(context))
			return;

		new StoryWriter(getStoryDetails(context)).write();
	}

	private static StoryDetails getStoryDetails(ExtensionContext context) {
		Class<?> clazz = context.getRequiredTestClass();
		return context.getStore(NAMESPACE).get(clazz.getName(), StoryDetails.class);
	}

	private static boolean isStory(ExtensionContext context) {
		return isAnnotated(context.getRequiredTestClass(), Story.class);
	}
	
	

}
