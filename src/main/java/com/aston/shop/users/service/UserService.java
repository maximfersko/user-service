package com.aston.shop.users.service;

import com.aston.shop.users.dto.UserDto;
import com.aston.shop.users.entity.User;
import com.aston.shop.users.mapper.UserMapper;
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
	private final UserMapper userMapper;

	@Autowired
	public UserService(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	public List<UserDto> findAll() {
		return userMapper.toDto(userRepository.findAll());
	}

	public Optional<UserDto> findById(Long id) {
		return userRepository.findById(id)
				.map(userMapper::toDto);
	}

	public Optional<UserDto> findByPassword(String password) {
		return userRepository.findByPassword(password).map(userMapper::toDto);
	}

	public Optional<UserDto> findByEmail(String email) {
		return userRepository.findByEmail(email).map(userMapper::toDto);
	}

	public Optional<UserDto> findByUsername(String username) {
		return userRepository.findByUsername(username).map(userMapper::toDto);
	}

	@Transactional
	public void update(User user) {
		userRepository.saveAndFlush(user);
	}

	@Transactional
	public UserDto save(User user) {
		return userMapper.toDto(userRepository.save(user));
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
