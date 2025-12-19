package com.malleo.common.security.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.malleo.common.security.jwt.AccessTokenResolver;
import com.malleo.common.security.jwt.JwtPrincipal;
import com.malleo.common.security.jwt.JwtVerificationException;
import com.malleo.common.security.jwt.JwtVerifier;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final AccessTokenResolver accessTokenResolver;
	private final JwtVerifier jwtVerifier;

	public JwtAuthenticationFilter(
		AccessTokenResolver accessTokenResolver,
		JwtVerifier jwtVerifier
	) {
		this.accessTokenResolver = accessTokenResolver;
		this.jwtVerifier = jwtVerifier;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String token = accessTokenResolver.resolve(request);

			if (token != null && !token.isBlank()) {
				try {
					JwtPrincipal principal = jwtVerifier.verify(token);

					UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
							principal,
							null,
							principal.toAuthorities()
						);

					authentication.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
					);

					SecurityContextHolder.getContext().setAuthentication(authentication);
				} catch (JwtVerificationException ex) {
					SecurityContextHolder.clearContext();
					response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer error=\"invalid_token\"");
				}
			}
		}

		filterChain.doFilter(request, response);
	}
}
