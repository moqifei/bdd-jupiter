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

import com.moqifei.bdd.jupiter.phase.Phase;
import com.moqifei.bdd.jupiter.phase.SubPhase;
import com.moqifei.bdd.jupiter.phase.WhenPhase;
import com.moqifei.bdd.jupiter.modle.And;
import com.moqifei.bdd.jupiter.modle.Given;
import com.moqifei.bdd.jupiter.modle.Then;
import com.moqifei.bdd.jupiter.modle.When;
import com.moqifei.bdd.jupiter.json.Json;
import com.moqifei.bdd.jupiter.phase.GivenPhase;
import com.moqifei.bdd.jupiter.phase.ThenPhase;

import java.util.ArrayList;
import java.util.List;

/**
 * A scene is central to BDD or specification by example style of writing tests.
 * It describes a use case in a story or specification and holds all the
 * information required to execute a scenario.
 */
public class Scene {
	private static final String EMPTY = "";

	private String methodName;
	private String description;
	private String runResult;
	private String key;

	private Phase current;

	private GivenPhase givenPhase;
	private WhenPhase whenPhase;
	private ThenPhase thenPhase;

	/**
	 * Represents the shared state amongst the phases of a scene.
	 */
	private Json state;

	public Scene() {
		this.state = new Json();
	}

	public <T> Json put(String key, T value) {
		return state.val(key, value);
	}

	public <T> T get(String key) {
		return state.val(key);
	}

	public String getMethodName() {
		return methodName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	Scene setMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

	String getDescription() {
		return description;
	}

	Scene setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getRunResult() {
		return runResult;
	}

	public void setRunResult(String runResult) {
		this.runResult = runResult;
	}

	public Scene given(String description, Given given) {
		if (givenPhase != null) {
			throw new IllegalStateException("Cannot define more than one \"given\" phase.");
		}

		givenPhase = new GivenPhase(description, given);
		current = givenPhase;
		return this;
	}

	public Scene when(String description, When when) {
		if (whenPhase != null) {
			throw new IllegalStateException("Cannot define more than one \"when\" phase.");
		}

		whenPhase = new WhenPhase(description, when);
		current = whenPhase;
		return this;
	}

	public Scene then(String description, Then then) {
		if (thenPhase != null) {
			throw new IllegalStateException("Cannot define more than one \"then\" phase.");
		}

		thenPhase = new ThenPhase(description, then);
		current = thenPhase;

		return this;
	}

	public Scene and(String description, And and) {
		if (current == null) {
			throw new IllegalStateException("Start your scene with \"given\" or \"when\" phase.");

		} else if (current instanceof WhenPhase) {
			throw new IllegalStateException("Cannot add \"and\" condition to a \"when\" phase.");
		}

		current.geSubPhases().add(new SubPhase(description, and));

		return this;
	}

	public Scene run() {
		if (givenPhase == null && whenPhase == null) {
			throw new IllegalStateException("Start your scene with a \"given\" or \"when\" phase.");

		} else if (thenPhase == null) {
			throw new IllegalStateException("End your scene with the \"then\" phase before running the scene.");
		}

		try {
			if (givenPhase != null) {
				givenPhase.getGiven().g();
				runAnd(givenPhase);
			}

			if (whenPhase != null) {
				whenPhase.getWhen().w();
			}

			thenPhase.getThen().t();
			runAnd(thenPhase);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

		return this;
	}

	public String given() {
		return givenPhase != null ? givenPhase.getDescription() : EMPTY;
	}

	public List<String> givenAnds() {
		return phaseAnds(givenPhase);
	}

	public String when() {
		return whenPhase != null ? whenPhase.getDescription() : EMPTY;
	}

	public String then() {
		return thenPhase != null ? thenPhase.getDescription() : EMPTY;
	}

	public List<String> thenAnds() {
		return phaseAnds(thenPhase);
	}

	private void runAnd(Phase phase) throws Exception {
		for (SubPhase conditionHolder : phase.geSubPhases()) {
			conditionHolder.getCondition().condition();
		}
	}

	private List<String> phaseAnds(Phase phase) {
		List<String> ands = new ArrayList<>();

		if (phase != null) {
			phase.geSubPhases().forEach(ah -> ands.add(ah.getDescription()));
		}

		return ands;
	}
}
