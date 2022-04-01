package com.example.layered;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private final CustomerService customerService = new CustomerServiceDefault();

    @Test
    void creatingCustomerWithLastName() {
        final var id = UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492");
        final var firstName = "John";
        final var lastName = "Smith";

        final var validCustomer = customerService.create(id, firstName, lastName);

        assertEquals(firstName, validCustomer.firstName().value());
        assertEquals(lastName, validCustomer.lastName().orElseThrow().value());
        assertEquals("John Smith", validCustomer.fullName());
        assertEquals("15d5650e-82b3-4239-abac-de48e65e3492", validCustomer.id().value().toString());
    }

    @Test
    void creatingCustomerWithoutLastName() {
        final var id = UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492");
        final var firstName = "John";
        final String lastName = null;

        final var validCustomer = customerService.create(id, firstName, lastName);

        assertEquals(firstName, validCustomer.firstName().value());
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
    void handlingInvalidCustomerId(UUID customerId, String expectedExceptionMessage) {
        final var lastName = "Smith";
        final var fistName = "John";

        final var exception = assertThrows(
            CustomerException.class,
            () -> customerService.create(customerId, fistName, lastName)
        );

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
    void handlingInvalidFirstName(String firstName, String expectedExceptionMessage) {
        final var id = UUID.randomUUID();
        final String lastName = null;

        final var exception = assertThrows(
            CustomerException.class,
            () -> customerService.create(id, firstName, lastName)
        );

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
    void handlingInvalidLastName(String lastName, String expectedExceptionMessage) {
        final var id = UUID.randomUUID();
        final var firstName = "John";

        final var exception = assertThrows(
            CustomerException.class,
            () -> customerService.create(id, firstName, lastName)
        );

        assertEquals(expectedExceptionMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
}
