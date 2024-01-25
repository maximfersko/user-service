package com.aston.shop.users.controller;


import com.aston.shop.users.controller.UserController;
import com.aston.shop.users.dto.UserDto;
import com.aston.shop.users.entity.User;
import com.aston.shop.users.mapper.UserMapper;
import com.aston.shop.users.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class UserControllerTest {

	@InjectMocks
	private UserController userController;

	@Mock
	private UserServiceImpl userService;

	@Mock
	private UserMapper userMapper;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetAllUsers() {
		User user = User.builder()
				.id(1L)
				.username("testUser")
				.firstname("John")
				.email("john@example.com")
				.address("123 Main St")
				.build();
		UserDto userDto = new UserDto(1L, "testUser", "John", "john@example.com", "123 Main St");
		when(userService.findAll()).thenReturn(Arrays.asList(user));
		when(userMapper.toDto(Arrays.asList(user))).thenReturn(Arrays.asList(userDto));

		ResponseEntity<List<UserDto>> responseEntity = userController.getAllUsers();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(1, responseEntity.getBody().size());
		assertEquals(userDto, responseEntity.getBody().get(0));
		verify(userService, times(1)).findAll();
		verify(userMapper, times(1)).toDto(Arrays.asList(user));
	}

	@Test
	void testGetUserByIdFound() {
		User user = User.builder()
				.id(1L)
				.username("testUser")
				.firstname("John")
				.email("john@example.com")
				.address("123 Main St")
				.build();
		UserDto userDto = new UserDto(1L, "testUser", "John", "john@example.com", "123 Main St");
		when(userService.findById(1L)).thenReturn(Optional.of(user));
		when(userMapper.toDto(user)).thenReturn(userDto);

		ResponseEntity<UserDto> responseEntity = userController.getUserById(1L);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(userDto, responseEntity.getBody());
		verify(userService, times(1)).findById(1L);
		verify(userMapper, times(1)).toDto(user);
	}

	@Test
	void testGetUserByIdNotFound() {
		when(userService.findById(1L)).thenReturn(Optional.empty());

		ResponseEntity<UserDto> responseEntity = userController.getUserById(1L);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
		verify(userService, times(1)).findById(1L);
		verifyNoInteractions(userMapper);
	}

	@Test
	void testUpdateUserFound() {
		User updatedUser = User.builder()
				.id(1L)
				.username("testUser")
				.firstname("UpdatedJohn")
				.email("john@example.com")
				.address("123 Main St")
				.build();
		when(userService.findById(1L)).thenReturn(Optional.of(User.builder().build()));

		ResponseEntity<String> responseEntity = userController.updateUser(1L, updatedUser);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("User updated successfully", responseEntity.getBody());
		verify(userService, times(1)).findById(1L);
		verify(userService, times(1)).update(updatedUser);
	}


	@Test
	void testDeleteUserFound() {

		when(userService.findById(1L)).thenReturn(Optional.of(User.builder().build()));

		ResponseEntity<String> responseEntity = userController.deleteUser(1L);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("User deleted successfully", responseEntity.getBody());
		verify(userService, times(1)).findById(1L);
		verify(userService, times(1)).deleteById(1L);
	}

}
