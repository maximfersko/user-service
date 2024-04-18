package com.aston.shop.users.controller;


import com.aston.shop.users.dto.JwtAuthenticationResponse;
import com.aston.shop.users.dto.SignInRequest;
import com.aston.shop.users.dto.SignUpRequest;
import com.aston.shop.users.service.AuthenticationService;
import com.aston.shop.users.utils.DataUtils;
import com.aston.shop.users.utils.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
	@DisplayName("Test Given Valid SignUp Request When Signing Up Then Return OK Status")
	void givenValidRequest_whenSignUp_thenOkStatus() {
		// given
		SignUpRequest signUpRequest = DataUtils.getSignUpRequest();
		BindingResult bindingResult = new BeanPropertyBindingResult(signUpRequest, "signUpRequest");
		when(authenticationService.signUp(signUpRequest)).thenReturn(new JwtAuthenticationResponse("mockToken"));

		// when
		ResponseEntity<?> responseEntity = authController.signUp(signUpRequest, bindingResult);

		// then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		verify(authenticationService, times(1)).signUp(signUpRequest);
		verifyNoMoreInteractions(authenticationService);
	}

	@Test
	@DisplayName("Test Given Invalid SignUp Request When Signing Up Then Return BAD_REQUEST Status")
	void givenInvalidRequest_whenSignUp_thenBadRequestStatus() {
		// given
		SignUpRequest signUpRequest = DataUtils.getSignUpRequest();
		BindingResult bindingResult = new BeanPropertyBindingResult(signUpRequest, "signUpRequest");
		bindingResult.reject("fieldError", "Invalid data");

		// when
		ResponseEntity<?> responseEntity = authController.signUp(signUpRequest, bindingResult);

		// then
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		verifyNoInteractions(authenticationService);
	}

	@Test
	@DisplayName("Test Given Valid SignIn Request When Signing In Then Return OK Status")
	void givenValidRequest_whenSignIn_thenOkStatus() {
		// given
		SignInRequest signInRequest = new SignInRequest("username", "password");
		BindingResult bindingResult = new BeanPropertyBindingResult(signInRequest, "signInRequest");
		when(authenticationService.signIn(signInRequest)).thenReturn(new JwtAuthenticationResponse("mockToken"));

		// when
		ResponseEntity<?> responseEntity = authController.signIn(signInRequest, bindingResult);

		// then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		verify(authenticationService, times(1)).signIn(signInRequest);
		verifyNoMoreInteractions(authenticationService);
	}

//	@Test
//	@DisplayName("Test Given Authentication Exception When Handling Then Return UNAUTHORIZED Status and Message")
//	void givenAuthException_whenHandling_thenUnauthorizedStatusAndMessage() {
//		// given
//		JwtAuthenticationException exception = new JwtAuthenticationException("Authentication failed");
//
//		// when
//		ResponseEntity<String> responseEntity = authController.handleAuthenticationException(exception);
//
//		// then
//		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
//		assertEquals("Authentication failed", responseEntity.getBody());
//	}

}
