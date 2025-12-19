package com.malleo.common.security.jwt;

public class JwtVerificationException extends RuntimeException {
	@SuppressWarnings("unused")
	public JwtVerificationException(String message) {
		super(message);
	}

	public JwtVerificationException(String message, Throwable cause) {
		super(message, cause);
	}
}
