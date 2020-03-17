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
