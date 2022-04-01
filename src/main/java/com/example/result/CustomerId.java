package com.example.result;

import java.util.UUID;

public record CustomerId(UUID value) {

    public static Result<CustomerId, CustomerException> of(UUID value) {
        if (value == null) {
            return new Result.Failure<>(new CustomerException("Customer ID must be an UUID"));
        }

        return new Result.Success<>(new CustomerId(value));
    }
}
