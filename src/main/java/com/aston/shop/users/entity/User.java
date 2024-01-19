package com.aston.shop.users.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Username should not be empty")
	@Size(min = 2, max = 50, message = "Username should be between 2 and 40 characters")
	@Column(name = "username", unique = true)
	private String username;

	@NotEmpty(message = "Password should not be empty")
	@Column(name = "password")
	private String password;

	@NotEmpty(message = "Firstname should not be empty")
	@Size(min = 2, max = 50, message = "Username should be between 2 and 40 characters")
	@Column(name = "firstname")
	private String firstname;

	@NotEmpty(message = "Username should not be empty")
	@Email(message = "Email should be valid")
	@Column(name = "email")
	private String email;

	@Size(min = 2, max = 100, message = "Address should be between 2 and 100 characters")
	@Column(name = "address")
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

}
