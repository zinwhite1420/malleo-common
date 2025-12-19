package com.malleo.common.security.jwt;

public interface JwtVerifier {

	JwtPrincipal verify(String token) throws JwtVerificationException;
}
