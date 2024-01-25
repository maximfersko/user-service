package com.aston.shop.users.service;

import com.aston.shop.users.entity.User;
import com.aston.shop.users.repository.UserRepository;
import com.aston.shop.users.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
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
	void testExistsByUsername() {
		when(userRepository.existsByUsername("existingUser")).thenReturn(true);
		when(userRepository.existsByUsername("nonExistingUser")).thenReturn(false);

		assertTrue(userService.existsByUsername("existingUser"));
		assertFalse(userService.existsByUsername("nonExistingUser"));

		verify(userRepository, times(1)).existsByUsername("existingUser");
		verify(userRepository, times(1)).existsByUsername("nonExistingUser");
	}

	@Test
	void testExistsByEmail() {
		when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
		when(userRepository.existsByEmail("nonExisting@example.com")).thenReturn(false);

		assertTrue(userService.existsByEmail("existing@example.com"));
		assertFalse(userService.existsByEmail("nonExisting@example.com"));

		verify(userRepository, times(1)).existsByEmail("existing@example.com");
		verify(userRepository, times(1)).existsByEmail("nonExisting@example.com");
	}

	@Test
	void testFindAll() {
		User user = new User();
		when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

		List<User> result = userService.findAll();
		assertEquals(1, result.size());
		assertSame(user, result.get(0));

		verify(userRepository, times(1)).findAll();
	}

	@Test
	void testFindById() {
		User user = new User();
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(userRepository.findById(2L)).thenReturn(Optional.empty());

		Optional<User> result1 = userService.findById(1L);
		assertTrue(result1.isPresent());
		assertSame(user, result1.get());

		Optional<User> result2 = userService.findById(2L);
		assertFalse(result2.isPresent());

		verify(userRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).findById(2L);
	}

	@Test
	void testFindByUsername() {
		User user = new User();
		when(userRepository.findOneByUsername("existingUser")).thenReturn(Optional.of(user));
		when(userRepository.findOneByUsername("nonExistingUser")).thenReturn(Optional.empty());

		UserDetails result1 = userService.findByUsername("existingUser");
		assertNotNull(result1);

		assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername("nonExistingUser"));

		verify(userRepository, times(1)).findOneByUsername("existingUser");
		verify(userRepository, times(1)).findOneByUsername("nonExistingUser");
	}

	@Test
	void testUserDetailsService() {
		User user = new User();
		when(userRepository.findOneByUsername("existingUser")).thenReturn(Optional.of(user));

		UserDetails result = userService.userDetailsService().loadUserByUsername("existingUser");
		assertNotNull(result);

		verify(userRepository, times(1)).findOneByUsername("existingUser");
	}

	@Test
	void testUpdate() {
		User user = new User();
		userService.update(user);

		verify(userRepository, times(1)).saveAndFlush(user);
	}

	@Test
	void testSave() {
		User user = new User();
		userService.save(user);

		verify(userRepository, times(1)).save(user);
	}

	@Test
	void testDeleteById() {
		userService.deleteById(1L);

		verify(userRepository, times(1)).deleteById(1L);
	}
}
