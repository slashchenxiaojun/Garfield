package org.hacker.module.cas.exception;

public class CredentialsNotAuthenticationException extends RuntimeException {
	private static final long serialVersionUID = -6656994484983715966L;

	public CredentialsNotAuthenticationException() {}

	public CredentialsNotAuthenticationException(String message) {
		super(message);
	}

	public CredentialsNotAuthenticationException(Throwable cause) {
		super(cause);
	}

	public CredentialsNotAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CredentialsNotAuthenticationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}