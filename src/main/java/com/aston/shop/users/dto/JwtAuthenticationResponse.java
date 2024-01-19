package com.aston.shop.users.dto;

import jakarta.validation.constraints.NotEmpty;

public record JwtAuthenticationResponse(@NotEmpty String token) {
}
