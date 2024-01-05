package com.aston.shop.users.service;

import com.aston.shop.users.mapper.UserMapper;
import com.aston.shop.users.model.dto.UserDto;
import com.aston.shop.users.model.entity.User;
import com.aston.shop.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserDto> findAll() {
		return userRepository.findAll()
				.stream()
				.map(UserMapper.INSTANCE::toDto)
				.toList();
	}

	public Optional<UserDto> findById(Long id) {
		return userRepository.findById(id)
				.map(UserMapper.INSTANCE::toDto);
	}

	public Optional<UserDto> findByPassword(String password) {
		return userRepository.findByPassword(password).map(UserMapper.INSTANCE::toDto);
	}

	public Optional<UserDto> findByEmail(String email) {
		return userRepository.findByEmail(email).map(UserMapper.INSTANCE::toDto);
	}

	public Optional<UserDto> findByUsername(String username) {
		return userRepository.findByUsername(username).map(UserMapper.INSTANCE::toDto);
	}

	@Transactional
	public void update(User user) {
		userRepository.saveAndFlush(user);
	}

	@Transactional
	public UserDto save(User user) {
		return UserMapper.INSTANCE.toDto(userRepository.save(user));
	}

	@Transactional
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Transactional
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

}
