package com.example.result;

import com.example.TestCases;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Result test cases")
class MainTest implements TestCases {

    @Test
    public void creatingCustomerWithLastName() {
        final var result = CustomerId.of(UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492"))
            .flatMap(customerId -> FirstName.of("John")
                .flatMap(firstName -> LastName.of("Smith")
                    .flatMap(lastName -> Customer.of(customerId, firstName, lastName))));

        assertEquals(Result.Success.class, result.getClass());

        final var customer = result.value();
        assertEquals("15d5650e-82b3-4239-abac-de48e65e3492", customer.id().value().toString());
        assertEquals("John", customer.firstName().value());
        assertEquals("Smith", customer.lastName().orElseThrow().value());
        assertEquals("John Smith", customer.fullName());
    }

    @Test
    public void creatingCustomerWithoutLastName() {
        final var result = CustomerId.of(UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492"))
            .flatMap(customerId -> FirstName.of("John")
                .flatMap(firstName -> Customer.of(customerId, firstName, null)));

        assertEquals(Result.Success.class, result.getClass());

        final var customer = result.value();
        assertEquals("15d5650e-82b3-4239-abac-de48e65e3492", customer.id().value().toString());
        assertEquals("John", customer.firstName().value());
        assertTrue(customer.lastName().isEmpty());
        assertEquals("John", customer.fullName());
    }

    @ParameterizedTest
    @MethodSource("invalidCustomerId")
    public void handlingInvalidCustomerId(UUID customerId, String expectedExceptionMessage) {
        final var result = CustomerId.of(customerId)
            .flatMap(id -> FirstName.of("John")
                .flatMap(firstName -> Customer.of(id, firstName, null)));

        assertEquals(Result.Failure.class, result.getClass());
        assertEquals(expectedExceptionMessage, result.exception().getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidFirstName")
    public void handlingInvalidFirstName(String firstName, String expectedExceptionMessage) {
        final var result = CustomerId.of(UUID.randomUUID())
            .flatMap(customerId -> FirstName.of(firstName)
                .flatMap(name -> Customer.of(customerId, name, null)));

        assertEquals(Result.Failure.class, result.getClass());
        assertEquals(expectedExceptionMessage, result.exception().getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidLastName")
    public void handlingInvalidLastName(String lastName, String expectedExceptionMessage) {
        final var result = CustomerId.of(UUID.randomUUID())
            .flatMap(customerId -> FirstName.of("John")
                .flatMap(firstName -> LastName.of(lastName)
                    .flatMap(name -> Customer.of(customerId, firstName, null))));

        assertEquals(Result.Failure.class, result.getClass());
        assertEquals(expectedExceptionMessage, result.exception().getMessage());
    }
}
