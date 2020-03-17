package com.moqifei.bdd.jupiter.modle;

/**
 * A condition represents operation on a subphanse such as AND OR NOT etc.
 */
public interface Condition {
	default boolean operate() {
		return false;
	}
	void condition() throws Exception;
}
