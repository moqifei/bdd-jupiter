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

package com.moqifei.bdd.jupiter.phase;

import com.moqifei.bdd.jupiter.modle.Condition;
import com.moqifei.bdd.jupiter.modle.Given;
import com.moqifei.bdd.jupiter.modle.Then;

/**
 * Represents an operation condition that is part of a {@link Given} or
 * {@link Then} phase in a scene.
 */
public class SubPhase extends Phase {

	private Condition condition;

	public SubPhase(String description, Condition condition) {
		super(description);
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}

}
