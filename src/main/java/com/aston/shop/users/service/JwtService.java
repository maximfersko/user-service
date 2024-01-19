package com.aston.shop.users.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {
	String extractUserName(String token);

	<T> T extractClaims(String token, Function<Claims, T> resolvers);

	String generateToken(UserDetails userDetails);

	boolean isTokenValid(String token, UserDetails userDetails);
}
