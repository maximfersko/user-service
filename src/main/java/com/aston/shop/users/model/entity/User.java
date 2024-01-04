package com.aston.shop.users.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Username should not be empty")
	@Size(min = 2, max = 40, message = "Username should be between 2 and 40 characters")
	@Column(name = "username")
	private String username;

	@NotEmpty(message = "password should not be empty")
	@Column(name = "password")
	private String password;

	@Column(name = "birthday")
	private int birthday;

	@NotEmpty(message = "Username should not be empty")
	@Email(message = "Email should be valid")
	@Column(name = "mail")
	private String mail;
}
