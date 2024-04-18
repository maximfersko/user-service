package com.aston.shop.users.service;

import com.aston.shop.users.entity.User;
import com.aston.shop.users.excetions.UserNotFoundException;
import com.aston.shop.users.repository.UserRepository;
import com.aston.shop.users.service.impl.UserServiceImpl;
import com.aston.shop.users.utils.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	@DisplayName("Test Check existence by username for existing and non-existing users")
	void givenExistingAndNonExistingUser_whenCheckExistsByUsername_thenVerifyBehavior() {
		// given
		String existingUser = "existingUser";
		String nonExistingUser = "nonExistingUser";
		when(userRepository.existsByUsername(existingUser)).thenReturn(true);
		when(userRepository.existsByUsername(nonExistingUser)).thenThrow(UsernameNotFoundException.class);

		// when
		boolean exists = userService.existsByUsername(existingUser);

		// then
		assertTrue(exists);
		assertThrows(UsernameNotFoundException.class, () -> userService.existsByUsername(nonExistingUser));
		verify(userRepository).existsByUsername(existingUser);
		verify(userRepository).existsByUsername(nonExistingUser);
	}

	@Test
	@DisplayName("Test Check existence by email for existing and non-existing emails")
	void givenExistingAndNonExistingEmail_whenCheckExistsByEmail_thenVerifyBehavior() {
		// given
		String existingEmail = "existing@example.com";
		String nonExistingEmail = "nonExisting@example.com";
		when(userRepository.existsByEmail(existingEmail)).thenReturn(true);
		when(userRepository.existsByEmail(nonExistingEmail)).thenThrow(UserNotFoundException.class);

		// when
		boolean emailExists = userService.existsByEmail(existingEmail);

		// then
		assertTrue(emailExists);
		assertThrows(UserNotFoundException.class, () -> userService.existsByEmail(nonExistingEmail));
		verify(userRepository).existsByEmail(existingEmail);
		verify(userRepository).existsByEmail(nonExistingEmail);
	}

	@Test
	@DisplayName("Test Retrieve all users with USER role")
	void whenFindAllUserRole_thenCorrectUsersRetrieved() {
		// given
		User user = DataUtils.getMichaelTransient();
		User johnTransient = DataUtils.getJohnTransient();
		when(userRepository.findAll()).thenReturn(List.of(user, johnTransient));

		// when
		List<User> users = userService.findAllUserRole();

		// then
		assertEquals(2, users.size());
		assertSame(user, users.get(0));
		assertSame(johnTransient, users.get(1));
		verify(userRepository).findAll();
	}

	@Test
	@DisplayName("Test Find user by ID and handle user not found")
	void givenUserId_whenFindById_thenCorrectUserFoundOrHandled() throws UserNotFoundException {
		// given
		User user = DataUtils.getMichaelPersisted();
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(userRepository.findById(2L)).thenReturn(Optional.empty());

		// when
		Optional<User> foundUser = userService.findById(1L);
		Optional<User> notFoundUser = userService.findById(2L);

		// then
		assertFalse(notFoundUser.isPresent());
		verify(userRepository).findById(1L);
		verify(userRepository).findById(2L);
		assertTrue(foundUser.isPresent());
		assertSame(user, foundUser.get());
	}

	@Test
	@DisplayName("Test Find existing user by username and handle non-existing user error")
	void givenUsername_whenFindByUsername_thenCorrectUserDetailsOrError() {
		// given
		User user = DataUtils.getAnnaTransient();
		when(userRepository.findOneByUsername(user.getUsername())).thenReturn(Optional.of(user));
		when(userRepository.findOneByUsername("nonExistingUser")).thenReturn(Optional.empty());

		// when
		UserDetails userDetails = userService.findByUsername(user.getUsername());
		assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername("nonExistingUser"));

		// then
		assertNotNull(userDetails);
		verify(userRepository).findOneByUsername(user.getUsername());
		verify(userRepository).findOneByUsername("nonExistingUser");
	}

}
