package com.aston.shop.users.service.impl;

import com.aston.shop.users.dto.JwtAuthenticationResponse;
import com.aston.shop.users.dto.SignInRequest;
import com.aston.shop.users.dto.SignUpRequest;
import com.aston.shop.users.entity.Role;
import com.aston.shop.users.entity.User;
import com.aston.shop.users.security.JwtUserFactory;
import com.aston.shop.users.service.AuthenticationService;
import com.aston.shop.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	private final UserService userService;
	private final JwtServiceImpl jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;

	@Transactional
	@Override
	public JwtAuthenticationResponse signUp(SignUpRequest request) {
		var user = User.builder()
				.username(request.username())
				.email(request.email())
				.password(passwordEncoder.encode(request.password()))
				.firstname(request.firstname())
				.role(Role.ROLE_USER)
				.address(request.address())
				.build();

		userService.save(user);

		UserDetails userDetails = JwtUserFactory.create(user);
		String token = jwtTokenProvider.generateToken(userDetails);
		return new JwtAuthenticationResponse(token);
	}

	@Transactional
	@Override
	public JwtAuthenticationResponse signIn(SignInRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				request.username(),
				request.password()
		));

		UserDetails userDetails = userService.userDetailsService().loadUserByUsername(request.username());
		String token = jwtTokenProvider.generateToken(userDetails);
		return new JwtAuthenticationResponse(token);
	}
}
