package com.aston.shop.users.utils;

import com.aston.shop.users.entity.User;
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
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;

		if (userService.findByEmail(user.getEmail()).isPresent()) {
			errors.rejectValue("Email", "", "This email is already taken !");
		}
		if (userService.findByPassword(user.getPassword()).isPresent()) {
			errors.rejectValue("Password", "", "This password is already taken !");
		}
		if (userService.findByUsername(user.getUsername()).isPresent()) {
			errors.rejectValue("username", "", "This username is already taken!");
		}
	}
}
