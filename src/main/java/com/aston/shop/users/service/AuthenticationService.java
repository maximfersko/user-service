package com.aston.shop.users.service;

import com.aston.shop.users.dto.JwtAuthenticationResponse;
import com.aston.shop.users.dto.SignInRequest;
import com.aston.shop.users.dto.SignUpRequest;

public interface AuthenticationService {
	JwtAuthenticationResponse signUp(SignUpRequest request);

	JwtAuthenticationResponse signIn(SignInRequest request);
}
