package com.example.demo.DTO;

import com.example.demo.Enums.ProjectStatus;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        ProjectStatus status
) {
}
