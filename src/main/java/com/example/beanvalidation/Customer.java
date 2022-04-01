package com.example.beanvalidation;

import jakarta.validation.Valid;

import java.util.Optional;

public record Customer(
    @Valid CustomerId id,
    @Valid FirstName firstName,
    @Valid Optional<LastName> lastName
) {
}
