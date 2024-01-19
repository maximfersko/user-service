package com.aston.shop.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
		@NotEmpty
		@Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
		String username,

		@NotEmpty
		@Size(min = 8, max = 255, message = "Password length must be between 8 and 255 characters")
		String password,

		@NotBlank
		@Size(min = 4, max = 50, message = "Firstname must be between 4 and 50 characters")
		String firstname,

		@NotEmpty(message = "Email address cannot be empty")
		@Email(message = "Email address must be in the format user@example.com")
		@Size(min = 5, max = 255, message = "Email address must be between 5 and 255 characters")
		String email
) {
}
