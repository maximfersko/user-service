package com.aston.shop.users.utils;

import com.aston.shop.users.dto.SignInRequest;
import com.aston.shop.users.dto.SignUpRequest;
import com.aston.shop.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
	private final UserService userService;

	@Autowired
	public UserValidator(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return SignUpRequest.class.equals(clazz) || SignInRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (target instanceof SignUpRequest request) {
			if (userService.existsByEmail(request.email())) {
				errors.rejectValue("Email", "", "This email is already taken !");
			}
			if (userService.existsByUsername(request.username())) {
				errors.rejectValue("Username", "", "This username is already taken !");
			}
		} else if (target instanceof SignInRequest request) {

		}

	}
}
