//package com.aston.shop.users.controller;
//
//import com.aston.shop.users.dto.UserDto;
//import com.aston.shop.users.entity.User;
//import com.aston.shop.users.service.impl.UserServiceImpl;
//import com.aston.shop.users.utils.UserValidator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class UserControllerTest {
//
//	@Mock
//	private UserServiceImpl userService;
//
//	@Mock
//	private UserValidator userValidator;
//
//	@InjectMocks
//	private UserController userController;
//
//	@BeforeEach
//	void setUp() {
//		MockitoAnnotations.openMocks(this);
//	}
//
//	@Test
//	void register_invalidUser_returnBadRequest() {
//
//		User user = new User(1L, "username", "password", null, "user@mail.com", "Address");
//		BindingResult bindingResult = mock(BindingResult.class);
//		when(bindingResult.hasErrors()).thenReturn(true);
//		when(bindingResult.getErrorCount()).thenReturn(1);
//		when(bindingResult.getFieldErrors()).thenReturn(List.of());
//
//		ResponseEntity<String> response = userController.register(user, bindingResult);
//
//		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//		assertTrue(response.getBody().contains("Validation Failed"));
//	}
//
//	@Test
//	void login_validCredentials_returnOk() {
//		String username = "username";
//		String password = "password";
//		UserDto userDto = new UserDto(1L, "username", "password", null, "user@mail.com", "Address");
//		when(userService.findByUsername(username)).thenReturn(Optional.of(userDto));
//
//		ResponseEntity<String> response = userController.login(username, password);
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals("Login successful", response.getBody());
//	}
//
//	@Test
//	void login_invalidPassword_returnUnauthorized() {
//		String username = "username";
//		String password = "wrong_password";
//		UserDto userDto = new UserDto(1L, "username", "password", null, "user@mail.com", "Address");
//		when(userService.findByUsername(username)).thenReturn(Optional.of(userDto));
//
//		ResponseEntity<String> response = userController.login(username, password);
//
//		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//		assertTrue(response.getBody().contains("Invalid password"));
//	}
//
//	@Test
//	void login_userNotFound_returnUnauthorized() {
//		String username = "non_existing_user";
//		String password = "password";
//		when(userService.findByUsername(username)).thenReturn(Optional.empty());
//
//		ResponseEntity<String> response = userController.login(username, password);
//
//		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//		assertTrue(response.getBody().contains("User not found"));
//	}
//
//	@Test
//	void getAllUsers_returnListOfUsers() {
//		List<UserDto> users = new ArrayList<>();
//		users.add(new UserDto(1L, "username1", "password1", null, "user1@mail.com", "Address 1"));
//		users.add(new UserDto(2L, "username2", "password2", null, "user2@mail.com", "Address 2"));
//		when(userService.findAll()).thenReturn(users);
//
//		ResponseEntity<List<UserDto>> response = userController.getAllUsers();
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals(users, response.getBody());
//	}
//
//	@Test
//	void getUserById_validUserId_returnUserDto() {
//		long userId = 1L;
//		UserDto userDto = new UserDto(1L, "username", "password", null, "user@mail.com", "Address");
//		when(userService.findById(userId)).thenReturn(Optional.of(userDto));
//
//		ResponseEntity<UserDto> response = userController.getUserById(userId);
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals(userDto, response.getBody());
//	}
//
//	@Test
//	void getUserById_invalidUserId_returnNotFound() {
//		long userId = 999L;
//		when(userService.findById(userId)).thenReturn(Optional.empty());
//
//		ResponseEntity<UserDto> response = userController.getUserById(userId);
//
//		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//		assertNull(response.getBody());
//	}
//
//
//	@Test
//	void updateUser_invalidUserId_returnNotFound() {
//		long userId = 999L;
//		User updatedUser = new User(userId, "new_username", "new_password", null, "new_user@mail.com", "New Address");
//		when(userService.findById(userId)).thenReturn(Optional.empty());
//
//		ResponseEntity<String> response = userController.updateUser(userId, updatedUser);
//
//		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//		assertNull(response.getBody());
//	}
//
//
//	@Test
//	void deleteUser_invalidUserId_returnNotFound() {
//		long userId = 999L;
//		when(userService.findById(userId)).thenReturn(Optional.empty());
//
//		ResponseEntity<String> response = userController.deleteUser(userId);
//
//		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//		assertNull(response.getBody());
//	}
//}
