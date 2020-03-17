package com.moqifei.bdd.jupiter.provider;

import org.junit.platform.commons.JUnitException;

public class JsonParsingException extends JUnitException {

	private static final long serialVersionUID = 1L;

	public JsonParsingException(String message) {
		super(message);
	}

	public JsonParsingException(String message, Throwable cause) {
		super(message, cause);
	}

}
