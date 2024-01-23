package com.aston.shop.users.service.impl;


import com.aston.shop.users.entity.User;
import com.aston.shop.users.repository.UserRepository;
import com.aston.shop.users.security.JwtUserFactory;
import com.aston.shop.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public UserDetails findByUsername(String username) {
		Optional<User> userOptional = userRepository.findOneByUsername(username);
		User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return JwtUserFactory.create(user);
	}

	@Override
	public UserDetailsService userDetailsService() {
		return this::findByUsername;
	}

	@Transactional
	public void update(User user) {
		userRepository.saveAndFlush(user);
	}

	@Transactional
	public void save(User user) {
		userRepository.save(user);
	}

	@Transactional
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

}
