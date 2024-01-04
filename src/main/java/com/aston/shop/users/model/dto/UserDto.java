package com.aston.shop.users.model.dto;

public record UserDto(Long id, String username, String password, int birthday, String mail) {
}
