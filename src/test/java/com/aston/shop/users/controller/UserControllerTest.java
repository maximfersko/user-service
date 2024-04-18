package com.aston.shop.users.controller;


import com.aston.shop.users.dto.UserDto;
import com.aston.shop.users.entity.User;
import com.aston.shop.users.mapper.UserMapper;
import com.aston.shop.users.service.impl.UserServiceImpl;
import com.aston.shop.users.utils.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
	@DisplayName("Test Given User List When GetAllUsers Called Then Return OK Status with User List")
	void testGetAllUsers() {
		// given
		User user = DataUtils.getJohnPersisted();
		UserDto userDto = DataUtils.getJohnDto();
		when(userService.findAllUserRole()).thenReturn(Arrays.asList(user));
		when(userMapper.toDto(Arrays.asList(user))).thenReturn(Arrays.asList(userDto));

		// when
		ResponseEntity<List<UserDto>> responseEntity = userController.getAllUsers();

		// then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(1, responseEntity.getBody().size());
		assertEquals(userDto, responseEntity.getBody().get(0));
		verify(userService, times(1)).findAllUserRole();
		verify(userMapper, times(1)).toDto(Arrays.asList(user));
	}

	@Test
	@DisplayName("Test Given User ID When User Found Then Return OK Status with UserDto")
	void testGetUserByIdFound() {
		// given
		User user = DataUtils.getAnnaPersisted();
		UserDto userDto = DataUtils.getAnnaDto();
		when(userService.findById(1L)).thenReturn(Optional.of(user));
		when(userMapper.toDto(user)).thenReturn(userDto);

		// when
		ResponseEntity<UserDto> responseEntity = userController.getUserById(1L);

		// then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(userDto, responseEntity.getBody());
		verify(userService, times(1)).findById(1L);
		verify(userMapper, times(1)).toDto(user);
	}

	@Test
	@DisplayName("Test Given User ID When User Not Found Then Return NOT_FOUND Status")
	void testGetUserByIdNotFound() {
		// given
		when(userService.findById(1L)).thenReturn(Optional.empty());

		// when
		ResponseEntity<UserDto> responseEntity = userController.getUserById(1L);

		// then
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
		verify(userService, times(1)).findById(1L);
		verifyNoInteractions(userMapper);
	}

	@Test
	@DisplayName("Test Given User ID and User Details When User Found Then Return OK Status with Success Message")
	void testUpdateUserFound() {
		// given
		User updatedUser = DataUtils.getMichaelPersisted();
		when(userService.findById(1L)).thenReturn(Optional.of(User.builder().build()));

		// when
		ResponseEntity<String> responseEntity = userController.updateUser(1L, updatedUser);

		// then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("User updated successfully", responseEntity.getBody());
		verify(userService, times(1)).findById(1L);
		verify(userService, times(1)).update(updatedUser);
	}

	@Test
	@DisplayName("Test Given User ID When User Found Then Return OK Status with Deletion Message")
	void testDeleteUserFound() {
		// given
		when(userService.findById(1L)).thenReturn(Optional.of(User.builder().build()));

		// when
		ResponseEntity<String> responseEntity = userController.deleteUser(1L);

		// then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("User deleted successfully", responseEntity.getBody());
		verify(userService, times(1)).findById(1L);
		verify(userService, times(1)).deleteById(1L);
	}


}
