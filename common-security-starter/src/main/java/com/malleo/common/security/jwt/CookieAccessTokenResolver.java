package com.malleo.common.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieAccessTokenResolver implements AccessTokenResolver {

	private final String cookieName;

	public CookieAccessTokenResolver(String cookieName) {
		this.cookieName = cookieName;
	}

	@Override
	public String resolve(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (cookieName.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}
}
