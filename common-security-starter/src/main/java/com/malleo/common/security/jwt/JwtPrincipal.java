package com.malleo.common.security.jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public record JwtPrincipal(
	String userId,
	List<String> roles,
	Map<String, Object> claims
) {

	public Collection<? extends GrantedAuthority> toAuthorities() {
		if (roles == null || roles.isEmpty()) {
			return List.of();
		}

		return roles.stream()
			.filter(role -> role != null && !role.isBlank())
			.map(SimpleGrantedAuthority::new)
			.toList();
	}
}
