package com.aston.shop.users.security;

import com.aston.shop.users.entity.User;

public class JwtUserFactory {
	private JwtUserFactory() {

	}

	public static JwtUserDetails create(User user) {
		return JwtUserDetails.builder()
				.id(user.getId())
				.address(user.getAddress())
				.email(user.getEmail())
				.role(user.getRole())
				.username(user.getUsername())
				.password(user.getPassword())
				.firstName(user.getFirstname())
				.build();
	}
}
