package com.aston.shop.users.config;

import com.aston.shop.users.service.JwtService;
import com.aston.shop.users.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

	/**
	 * Префикс Bearer токена.
	 */
	public static final String BEARER_PREFIX = "Bearer ";

	/**
	 * Имя заголовка для токена Authorization.
	 */
	public static final String HEADER_NAME = "Authorization";

	private final JwtService provider;
	private final UserService userService;

	/**
	 * @param request     Входящий HTTP-запрос.
	 * @param response    Исходящий HTTP-ответ.
	 * @param filterChain Цепочка фильтров для обработки запроса.
	 * @throws ServletException Если возникает исключение во время обработки сервлета.
	 * @throws IOException      Если возникает ошибка ввода-вывода во время обработки сервлета.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader(HEADER_NAME);

		if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(BEARER_PREFIX.length());
		processToken(token, request);

		filterChain.doFilter(request, response);
	}

	/**
	 * Обрабатывает предоставленный JWT-токен и аутентифицирует пользователя.
	 *
	 * @param token   JWT-токен, извлеченный из заголовка Authorization.
	 * @param request HTTP-запрос, связанный с обработкой токена.
	 */
	private void processToken(String token, HttpServletRequest request) {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String username = provider.extractUserName(token);

			if (StringUtils.isNotBlank(username)) {
				UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

				if (provider.isTokenValid(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
					);

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
	}
}
