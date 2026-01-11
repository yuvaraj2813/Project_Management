package com.example.demo.DTO;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank(message = "project name required")
        String name,
        @NotBlank(message = "give the description")
        String description
) {
}
