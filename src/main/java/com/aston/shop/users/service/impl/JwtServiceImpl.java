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

	/**
	 * Значение секретного ключа для подписи JWT, получаемое из конфигурации.
	 */
	@Value("${jwt.token.secret}")
	private String secret;

	/**
	 * Время жизни JWT в миллисекундах, получаемое из конфигурации.
	 */
	@Value("${jwt.token.lifetime}")
	private long lifetime;

	/**
	 * Извлекает имя пользователя из токена.
	 *
	 * @param token Токен для извлечения информации.
	 * @return Имя пользователя, связанное с токеном.
	 */
	@Override
	public String extractUserName(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	/**
	 * Извлекает все утверждения из токена.
	 *
	 * @param token Токен для извлечения информации.
	 * @return Объект Claims, содержащий все утверждения из токена.
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	/**
	 * Получает ключ для подписи токена из секретного ключа.
	 *
	 * @return Ключ для подписи токена.
	 */
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Извлекает информацию из токена с использованием заданного резольвера.
	 *
	 * @param token     Токен для извлечения информации.
	 * @param resolvers Функция резольвера для обработки утверждений из токена.
	 * @param <T>       Тип возвращаемого значения резольвера.
	 * @return Результат обработки токена резольвером.
	 */
	@Override
	public <T> T extractClaims(String token, Function<Claims, T> resolvers) {
		Claims claims = extractAllClaims(token);
		return resolvers.apply(claims);
	}

	/**
	 * Генерирует JWT на основе информации о пользователе.
	 *
	 * @param userDetails Детали пользователя, для которого генерируется токен.
	 * @return Сгенерированный токен JWT.
	 */
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

	/**
	 * Генерирует JWT на основе дополнительных утверждений и информации о пользователе.
	 *
	 * @param extraClaims Дополнительные утверждения, которые будут добавлены к токену.
	 * @param userDetails Детали пользователя, для которого генерируется токен.
	 * @return Сгенерированный токен JWT.
	 */
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

	/**
	 * Проверяет действительность токена по сравнению с информацией о пользователе.
	 *
	 * @param token       Токен для проверки действительности.
	 * @param userDetails Детали пользователя для сравнения с токеном.
	 * @return true, если токен действителен для данного пользователя, в противном случае - false.
	 */
	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return userName.equals(userDetails.getUsername());
	}
}
