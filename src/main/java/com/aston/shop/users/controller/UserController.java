package com.aston.shop.users.controller;

import com.aston.shop.users.dto.UserDto;
import com.aston.shop.users.entity.User;
import com.aston.shop.users.service.UserService;
import com.aston.shop.users.utils.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
	private final UserService userService;
	private final UserValidator userValidator;

	@Autowired
	public UserController(UserService userService, UserValidator userValidator) {
		this.userService = userService;
		this.userValidator = userValidator;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidator);
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = new ArrayList<>(bindingResult.getErrorCount());
			for (var error : bindingResult.getFieldErrors()) {
				errors.add(error.getField() + ": " + error.getDefaultMessage());
			}
			String errorMsg = "Validation Failed: \n" + String.join("\n ", errors);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
		}
		userService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
		Optional<UserDto> user = userService.findByUsername(username);

		if (user.isPresent()) {
			UserDto userDto = user.get();

			if (password.equals(userDto.password())) {
				return ResponseEntity.ok("Login successful");
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
		}
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> users = userService.findAll();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
		Optional<UserDto> user = userService.findById(id);
		return user.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
		Optional<UserDto> existingUser = userService.findById(id);

		if (existingUser.isPresent()) {
			userService.update(updatedUser);
			return ResponseEntity.ok("User updated successfully");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		Optional<UserDto> user = userService.findById(id);

		if (user.isPresent()) {
			userService.deleteById(id);
			return ResponseEntity.ok("User deleted successfully");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}


