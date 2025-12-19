package com.malleo.common.security.jwt;
@SuppressWarnings("unused")
public class JwtVerificationException extends RuntimeException {

	public JwtVerificationException(String message) {
		super(message);
	}

	public JwtVerificationException(String message, Throwable cause) {
		super(message, cause);
	}
}
