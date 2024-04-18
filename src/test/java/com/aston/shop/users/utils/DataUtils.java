package com.aston.shop.users.utils;

import com.aston.shop.users.dto.SignUpRequest;
import com.aston.shop.users.dto.UserDto;
import com.aston.shop.users.entity.Role;
import com.aston.shop.users.entity.User;

public class DataUtils {

	public static final String JWT_TOKEN_FOR_TEST = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJiMDhmODZhZi0zNWRhLTQ4ZjItOGZhYi1jZWYzOTA0NjYwYmQifQ.-xN_h82PHVTCMA9vdoHrcZxH-x5mb11y1537t3rGzcM";


	public static User getMichaelTransient() {
		return User.builder()
				.username("ferskoss")
				.firstname("Michael")
				.email("michael566@gmail.com")
				.address("street Pert 67")
				.password("@#$$$#TG$TW")
				.role(Role.ROLE_USER)
				.build();
	}

	public static User getMichaelPersisted() {
		return User.builder()
				.id(1L)
				.username("ferskoss")
				.firstname("Michael")
				.email("michael566@gmail.com")
				.address("street Pert 67")
				.password("@#$$$#TG$TW")
				.role(Role.ROLE_USER)
				.build();
	}


	public static User getJohnTransient() {
		return User.builder()
				.username("johny")
				.firstname("John")
				.email("john.doe@example.com")
				.address("Sunset Boulevard 123")
				.password("Pass1234!")
				.role(Role.ROLE_USER)
				.build();
	}

	public static User getJohnPersisted() {
		return User.builder()
				.id(2L)
				.username("johny")
				.firstname("John")
				.email("john.doe@example.com")
				.address("Sunset Boulevard 123")
				.password("Pass1234!")
				.role(Role.ROLE_USER)
				.build();
	}

	public static User getAnnaTransient() {
		return User.builder()
				.username("anna_b")
				.firstname("Anna")
				.email("anna.banana@example.com")
				.address("Broadway Avenue 45")
				.password("AnnaBan@#2024")
				.role(Role.ROLE_USER)
				.build();
	}


	public static User getAnnaPersisted() {
		return User.builder()
				.id(3L)
				.username("anna_b")
				.firstname("Anna")
				.email("anna.banana@example.com")
				.address("Broadway Avenue 45")
				.password("AnnaBan@#2024")
				.role(Role.ROLE_USER)
				.build();
	}

	public static UserDto getMichaelDto() {
		return new UserDto(
				1L,
				"ferskoss",
				"Michael",
				"michael566@gmail.com",
				"street Pert 67"
		);
	}

	public static UserDto getJohnDto() {
		return new UserDto(
				2L,
				"johny",
				"John",
				"john.doe@example.com",
				"Sunset Boulevard 123"
		);
	}

	public static UserDto getAnnaDto() {
		return new UserDto(
				3L,
				"anna_b",
				"Anna",
				"anna.banana@example.com",
				"Broadway Avenue 45"
		);
	}


	public static SignUpRequest getSignUpRequest() {
		return new SignUpRequest(
				"username",
				"password",
				"firstname",
				"user@example.com",
				"address"
		);
	}
}