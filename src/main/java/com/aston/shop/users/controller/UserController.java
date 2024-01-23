package com.aston.shop.users.controller;


import com.aston.shop.users.dto.UserDto;
import com.aston.shop.users.entity.User;
import com.aston.shop.users.mapper.UserMapper;
import com.aston.shop.users.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
	private final UserServiceImpl userService;
	private final UserMapper userMapper;


	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> users = userMapper.toDto(userService.findAll());
		return ResponseEntity.ok(users);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
		Optional<UserDto> user = userService.findById(id).map(userMapper::toDto);
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


