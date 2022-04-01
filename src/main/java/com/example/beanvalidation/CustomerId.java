package com.example.beanvalidation;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CustomerId(
    @NotNull(message = "Customer ID must be an UUID")
    UUID value
) {
}
