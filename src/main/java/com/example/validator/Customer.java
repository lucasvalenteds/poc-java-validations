package com.example.validator;

import java.util.Optional;

public record Customer(CustomerId id,
                       FirstName firstName,
                       Optional<LastName> lastName
) {
    public String fullName() {
        return lastName()
            .map(lastName -> firstName.value() + " " + lastName.value())
            .orElseGet(firstName::value);
    }
}
