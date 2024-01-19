package com.aston.shop.users.controller;


import com.aston.shop.users.entity.User;
import com.aston.shop.users.service.impl.UserServiceImpl;
import com.aston.shop.users.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
	private final UserServiceImpl userService;
	private final UserValidator userValidator;

	@Autowired
	public UserController(UserServiceImpl userService, UserValidator userValidator) {
		this.userService = userService;
		this.userValidator = userValidator;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidator);
	}



	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.findAll();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		Optional<User> user = userService.findById(id);
		return user.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}


	@PutMapping("/users/{id}")
	public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
		Optional<User> existingUser = userService.findById(id);

		if (existingUser.isPresent()) {
			userService.update(updatedUser);
			return ResponseEntity.ok("User updated successfully");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		Optional<User> user = userService.findById(id);

		if (user.isPresent()) {
			userService.deleteById(id);
			return ResponseEntity.ok("User deleted successfully");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}


