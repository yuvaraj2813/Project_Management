package com.example.demo.DTO;

public record ApiError(
        int status,
        String message
) {
}
