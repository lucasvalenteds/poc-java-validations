package com.example.result;

import java.util.Optional;

public record Customer(CustomerId id,
                       FirstName firstName,
                       Optional<LastName> lastName
) {

    public static Result<Customer, CustomerException> of(CustomerId id, FirstName firstName, LastName lastName) {
        if (id == null)
            return new Result.Failure<>(new CustomerException("Customer must have an ID"));

        if (firstName == null)
            return new Result.Failure<>(new CustomerException("Customer must have a name"));

        return new Result.Success<>(new Customer(id, firstName, Optional.ofNullable(lastName)));
    }

    public String fullName() {
        return lastName()
            .map(lastName -> firstName.value() + " " + lastName.value())
            .orElseGet(firstName::value);
    }
}
