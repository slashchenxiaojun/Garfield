package org.hacker.cas.sso.exception;

public class CasServiceException extends RuntimeException {
	private static final long serialVersionUID = -6656994484983715966L;

	public CasServiceException() {}

	public CasServiceException(String message) {
		super(message);
	}

	public CasServiceException(Throwable cause) {
		super(cause);
	}

	public CasServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CasServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}