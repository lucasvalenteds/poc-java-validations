package com.example.jakarta;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FirstName(
    @NotNull(message = "First name is required")
    @NotEmpty(message = "First name is too short")
    @Size(max = 30, message = "First name is too long")
    String value
) {
}
