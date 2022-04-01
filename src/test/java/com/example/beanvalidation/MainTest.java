package com.example.beanvalidation;

import com.example.TestCases;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Bean Validation test cases")
class MainTest implements TestCases {

    private final Validator validator = Validation.buildDefaultValidatorFactory()
        .getValidator();

    @Test
    public void creatingCustomerWithLastName() {
        final var customer = new Customer(
            new CustomerId(UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492")),
            new FirstName("John"),
            Optional.of(new LastName("Smith"))
        );

        final var violations = validator.validate(customer);

        assertThat(violations)
            .isEmpty();
    }

    @Test
    public void creatingCustomerWithoutLastName() {
        final var customer = new Customer(
            new CustomerId(UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492")),
            new FirstName("John"),
            Optional.empty()
        );

        final var violations = validator.validate(customer);

        assertThat(violations)
            .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidCustomerId")
    public void handlingInvalidCustomerId(UUID customerId, String expectedExceptionMessage) {
        final var customer = new Customer(
            new CustomerId(customerId),
            new FirstName("John"),
            Optional.empty()
        );

        final var violations = validator.validate(customer);

        assertThat(violations)
            .isNotEmpty()
            .extracting(ConstraintViolation::getMessage)
            .contains(expectedExceptionMessage);
    }

    @ParameterizedTest
    @MethodSource("invalidFirstName")
    public void handlingInvalidFirstName(String firstName, String expectedExceptionMessage) {
        final var customer = new Customer(
            new CustomerId(UUID.randomUUID()),
            new FirstName(firstName),
            Optional.empty()
        );

        final var violations = validator.validate(customer);

        assertThat(violations)
            .isNotEmpty()
            .extracting(ConstraintViolation::getMessage)
            .contains(expectedExceptionMessage);
    }

    @ParameterizedTest
    @MethodSource("invalidLastName")
    public void handlingInvalidLastName(String lastName, String expectedExceptionMessage) {
        final var customer = new Customer(
            new CustomerId(UUID.randomUUID()),
            new FirstName("John"),
            Optional.of(new LastName(lastName))
        );

        final var violations = validator.validate(customer);

        assertThat(violations)
            .isNotEmpty()
            .extracting(ConstraintViolation::getMessage)
            .contains(expectedExceptionMessage);
    }
}
