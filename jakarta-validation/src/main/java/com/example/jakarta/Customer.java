package com.example.jakarta;

import jakarta.validation.Valid;

import java.util.Optional;

public record Customer(
    @Valid CustomerId id,
    @Valid FirstName firstName,
    @Valid Optional<LastName> lastName
) {
}
