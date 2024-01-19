package com.aston.shop.users.config;

import com.aston.shop.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final UserService userService;
	private final AuthenticationFilter authenticationFilter;

	/**
	 * Настройка цепочки фильтров безопасности HTTP для обеспечения защиты ресурсов приложения.
	 *
	 * @param http Конфигурация HttpSecurity для определения правил доступа и других настроек безопасности.
	 * @return Объект SecurityFilterChain, представляющий цепочку фильтров безопасности.
	 * @throws Exception Возникает, если произошла ошибка в процессе настройки безопасности.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.cors(cors -> cors.configurationSource(request -> {
					CorsConfiguration configuration = new CorsConfiguration();
					configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
					return configuration;
				}))
				.authorizeHttpRequests(request -> request
						.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()
						.requestMatchers("/api/users/**").authenticated() // Specify the path pattern for UserController
						.anyRequest().authenticated())
				.sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	/**
	 * Создает бин для кодирования паролей с использованием BCryptPasswordEncoder.
	 *
	 * @return Реализация PasswordEncoder для шифрования паролей.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Создает бин для настройки AuthenticationProvider, использующий сервис пользователей и шифрование паролей.
	 *
	 * @return Объект AuthenticationProvider для обеспечения аутентификации.
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService.userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * Создает бин для настройки AuthenticationManager, используя AuthenticationConfiguration.
	 *
	 * @param config Конфигурация аутентификации для получения AuthenticationManager.
	 * @return Объект AuthenticationManager для обработки аутентификационных запросов.
	 * @throws Exception Возникает, если произошла ошибка при получении AuthenticationManager.
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
