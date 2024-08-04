package com.evergreen_hotels.bmg.whms1.exception;

import java.io.Serializable;

public class InternalErrorException extends RuntimeException implements Serializable
{
	private static final long serialVersionUID = 3359273794322876526L;

	public InternalErrorException() {
	}

	public InternalErrorException(Throwable t) {
		super(t);
	}

	public InternalErrorException(String message) {
		super(message);
	}

	public InternalErrorException(String message, Throwable t) {
		super(message, t);
	}
}
