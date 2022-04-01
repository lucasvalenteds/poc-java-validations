package com.example;

import org.junit.jupiter.params.provider.Arguments;

import java.util.UUID;
import java.util.stream.Stream;

public interface TestCases {

    void creatingCustomerWithLastName();

    void creatingCustomerWithoutLastName();

    static Stream<Arguments> invalidCustomerId() {
        return Stream.of(
            Arguments.of(null, "Customer ID must be an UUID")
        );
    }

    void handlingInvalidCustomerId(UUID customerId, String expectedExceptionMessage);

    static Stream<Arguments> invalidFirstName() {
        return Stream.of(
            Arguments.of(null, "First name is required"),
            Arguments.of("", "First name is too short"),
            Arguments.of("a".repeat(31), "First name is too long")
        );
    }

    void handlingInvalidFirstName(String firstName, String expectedExceptionMessage);

    static Stream<Arguments> invalidLastName() {
        return Stream.of(
            Arguments.of("a".repeat(51), "Last name is too long")
        );
    }

    void handlingInvalidLastName(String lastName, String expectedExceptionMessage);
}
