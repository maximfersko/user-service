package com.aston.shop.users.controller;


import com.aston.shop.users.dto.JwtAuthenticationResponse;
import com.aston.shop.users.dto.SignInRequest;
import com.aston.shop.users.dto.SignUpRequest;
import com.aston.shop.users.excetions.JwtAuthenticationException;
import com.aston.shop.users.service.AuthenticationService;
import com.aston.shop.users.utils.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class AuthControllerTest {

	@InjectMocks
	private AuthController authController;

	@Mock
	private UserValidator userValidator;

	@Mock
	private AuthenticationService authenticationService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testSignUpValidRequest() {
		SignUpRequest signUpRequest = new SignUpRequest("username", "password", "firstname", "user@example.com", "address");
		BindingResult bindingResult = new BeanPropertyBindingResult(signUpRequest, "signUpRequest");

		when(authenticationService.signUp(signUpRequest)).thenReturn(new JwtAuthenticationResponse("mockToken"));

		ResponseEntity<?> responseEntity = authController.signUp(signUpRequest, bindingResult);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		verify(authenticationService, times(1)).signUp(signUpRequest);
		verifyNoMoreInteractions(authenticationService);
	}

	@Test
	void testSignUpInvalidRequest() {
		SignUpRequest signUpRequest = new SignUpRequest("username", "password", "firstname", "invalidEmail", "address");
		BindingResult bindingResult = new BeanPropertyBindingResult(signUpRequest, "signUpRequest");
		bindingResult.reject("fieldError", "Invalid data");

		ResponseEntity<?> responseEntity = authController.signUp(signUpRequest, bindingResult);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

		verifyNoInteractions(authenticationService);
	}

	@Test
	void testSignInValidRequest() {
		SignInRequest signInRequest = new SignInRequest("username", "password");
		BindingResult bindingResult = new BeanPropertyBindingResult(signInRequest, "signInRequest");

		when(authenticationService.signIn(signInRequest)).thenReturn(new JwtAuthenticationResponse("mockToken"));

		ResponseEntity<?> responseEntity = authController.signIn(signInRequest, bindingResult);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		verify(authenticationService, times(1)).signIn(signInRequest);
		verifyNoMoreInteractions(authenticationService);
	}

	@Test
	void testHandleAuthenticationException() {
		JwtAuthenticationException exception = new JwtAuthenticationException("Authentication failed");

		ResponseEntity<String> responseEntity = authController.handleAuthenticationException(exception);

		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
		assertEquals("Authentication failed", responseEntity.getBody());
	}
}
