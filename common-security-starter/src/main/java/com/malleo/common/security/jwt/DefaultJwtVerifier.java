package com.malleo.common.security.jwt;

public class DefaultJwtVerifier implements JwtVerifier {

	@Override
	public JwtPrincipal verify(String token) throws JwtVerificationException {
		throw new JwtVerificationException(
			"JwtVerifier bean is not configured. Please provide a JwtVerifier @Bean in the application."
		);
	}
}
