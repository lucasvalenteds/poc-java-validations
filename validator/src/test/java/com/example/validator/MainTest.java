package com.example.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    public void creatingCustomerWithLastName() {
        final var customer = new Customer(
            new CustomerId(UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492")),
            new FirstName("John"),
            Optional.of(new LastName("Smith"))
        );

        final var validCustomer = CustomerValidator.customerId()
            .and(CustomerValidator.firstName())
            .and(CustomerValidator.lastName())
            .apply(customer)
            .<Customer>getOrElseThrow();

        assertEquals("John", validCustomer.firstName().value());
        assertEquals("Smith", validCustomer.lastName().orElseThrow().value());
        assertEquals("John Smith", validCustomer.fullName());
        assertEquals("15d5650e-82b3-4239-abac-de48e65e3492", validCustomer.id().value().toString());
    }

    @Test
    public void creatingCustomerWithoutLastName() {
        final var customer = new Customer(
            new CustomerId(UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492")),
            new FirstName("John"),
            Optional.empty()
        );

        final var validCustomer = CustomerValidator.customerId()
            .and(CustomerValidator.firstName())
            .and(CustomerValidator.lastName())
            .apply(customer)
            .<Customer>getOrElseThrow();

        assertEquals("John", validCustomer.firstName().value());
        assertTrue(validCustomer.lastName().isEmpty());
        assertEquals("John", validCustomer.fullName());
        assertEquals("15d5650e-82b3-4239-abac-de48e65e3492", validCustomer.id().value().toString());
    }

    static Stream<Arguments> invalidCustomerId() {
        return Stream.of(
            Arguments.of(null, "Customer ID must be an UUID")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidCustomerId")
    public void handlingInvalidCustomerId(UUID customerId, String expectedExceptionMessage) {
        final var customer = new Customer(
            new CustomerId(customerId),
            new FirstName("John"),
            Optional.empty()
        );

        final var validator = CustomerValidator.customerId()
            .and(CustomerValidator.firstName())
            .and(CustomerValidator.lastName())
            .apply(customer);

        final var exception = assertThrows(CustomerException.class, validator::getOrElseThrow);

        assertEquals(expectedExceptionMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    static Stream<Arguments> invalidFirstName() {
        return Stream.of(
            Arguments.of(null, "First name is required"),
            Arguments.of("", "First name is too short"),
            Arguments.of("a".repeat(31), "First name is too long")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidFirstName")
    public void handlingInvalidFirstName(String firstName, String expectedExceptionMessage) {
        final var customer = new Customer(
            new CustomerId(UUID.randomUUID()),
            new FirstName(firstName),
            Optional.empty()
        );

        final var validator = CustomerValidator.customerId()
            .and(CustomerValidator.firstName())
            .and(CustomerValidator.lastName())
            .apply(customer);

        final var exception = assertThrows(CustomerException.class, validator::getOrElseThrow);

        assertEquals(expectedExceptionMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    static Stream<Arguments> invalidLastName() {
        return Stream.of(
            Arguments.of("a".repeat(51), "Last name is too long")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidLastName")
    public void handlingInvalidLastName(String lastName, String expectedExceptionMessage) {
        final var customer = new Customer(
            new CustomerId(UUID.randomUUID()),
            new FirstName("John"),
            Optional.of(new LastName(lastName))
        );

        final var validator = CustomerValidator.customerId()
            .and(CustomerValidator.firstName())
            .and(CustomerValidator.lastName())
            .apply(customer);

        final var exception = assertThrows(CustomerException.class, validator::getOrElseThrow);

        assertEquals(expectedExceptionMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
}
