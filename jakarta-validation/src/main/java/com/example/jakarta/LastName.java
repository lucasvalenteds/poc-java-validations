package com.example.jakarta;

import jakarta.validation.constraints.Size;

public record LastName(
    @Size(max = 50, message = "Last name is too long")
    String value
) {
}
