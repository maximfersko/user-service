package com.aston.shop.users.service.impl;

import com.aston.shop.users.entity.User;
import com.aston.shop.users.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {


	@Value("${jwt.token.secret}")
	private String secret;


	@Value("${jwt.token.lifetime}")
	private long lifetime;


	@Override
	public String extractUserName(String token) {
		return extractClaims(token, Claims::getSubject);
	}


	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token).getBody();
	}


	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}


	@Override
	public <T> T extractClaims(String token, Function<Claims, T> resolvers) {
		Claims claims = extractAllClaims(token);
		return resolvers.apply(claims);
	}


	@Override
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();

		if (userDetails instanceof User customUserDetails) {
			claims.put("id", customUserDetails.getId());
			claims.put("email", customUserDetails.getEmail());
			claims.put("role", customUserDetails.getRole());
		}

		return generateToken(claims, userDetails);
	}


	private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		Date now = new Date(System.currentTimeMillis());
		Date expiration = new Date(now.getTime() + lifetime);

		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(now)
				.setExpiration(expiration)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}


	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return userName.equals(userDetails.getUsername());
	}
}
