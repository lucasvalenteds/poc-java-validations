package com.example.validator;

import java.util.function.Function;
import java.util.function.Predicate;

public interface CustomerValidator extends Function<Customer, ValidationResult> {

    private static CustomerValidator check(Predicate<Customer> predicate, String exceptionMessage) {
        return customer -> predicate.test(customer)
            ? new ValidationResult.Invalid<>(new CustomerException(exceptionMessage))
            : new ValidationResult.Valid<>(customer);
    }

    default CustomerValidator and(CustomerValidator validator) {
        return customer -> {
            final var result = this.apply(customer);
            return switch (result) {
                case ValidationResult.Valid ignored -> validator.apply(customer);
                case ValidationResult.Invalid ignored -> result;
            };
        };
    }

    static CustomerValidator customerId() {
        return check(customer -> customer.id().value() == null, "Customer ID must be an UUID");
    }

    static CustomerValidator firstName() {
        return check(customer -> customer.firstName().value() == null, "First name is required")
            .and(check(customer -> customer.firstName().value().length() == 0, "First name is too short"))
            .and(check(customer -> customer.firstName().value().length() > 30, "First name is too long"));
    }

    static CustomerValidator lastName() {
        return check(
            customer -> customer.lastName()
                .filter(lastName -> lastName.value().length() > 50)
                .isPresent(),
            "Last name is too long"
        );
    }
}
