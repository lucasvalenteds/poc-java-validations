package com.example.result;

public record LastName(String value) {

    public static Result<LastName, CustomerException> of(String value) {
        if (value != null && value.length() > 50) {
            return new Result.Failure<>(new CustomerException("Last name is too long"));
        }

        return new Result.Success<>(new LastName(value));
    }
}
