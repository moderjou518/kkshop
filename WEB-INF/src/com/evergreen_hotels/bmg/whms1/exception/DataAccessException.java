package com.evergreen_hotels.bmg.whms1.exception;

import java.io.Serializable;

public class DataAccessException extends InternalErrorException implements Serializable {
	private static final long serialVersionUID = 4433131730047087071L;

	public DataAccessException() {
	}

	public DataAccessException(Throwable t) {
		super(t);
	}

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(String message, Throwable t) {
		super(message, t);
	}
}
