package com.example.demo.DTO;

public record LoginResponse(
        String token,
        String role
) {
}
