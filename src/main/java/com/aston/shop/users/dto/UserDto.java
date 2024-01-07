package com.aston.shop.users.dto;

import java.time.LocalDate;

public record UserDto(Long id, String username, String password, LocalDate birthday, String email, String address) {
}
