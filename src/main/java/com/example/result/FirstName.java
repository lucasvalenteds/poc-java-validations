package com.example.result;

public record FirstName(String value) {

    public static Result<FirstName, CustomerException> of(String value) {
        if (value == null) {
            return new Result.Failure<>(new CustomerException("First name is required"));
        }

        if (value.length() == 0) {
            return new Result.Failure<>(new CustomerException("First name is too short"));
        }

        if (value.length() > 30) {
            return new Result.Failure<>(new CustomerException("First name is too long"));
        }

        return new Result.Success<>(new FirstName(value));
    }
}
