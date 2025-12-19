package com.malleo.common.security.jwt;

import jakarta.servlet.http.HttpServletRequest;

public interface AccessTokenResolver {

	String resolve(HttpServletRequest request);
}
