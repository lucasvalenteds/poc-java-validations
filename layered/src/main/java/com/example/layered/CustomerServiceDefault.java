package com.example.layered;

import java.util.Optional;
import java.util.UUID;

public final class CustomerServiceDefault implements CustomerService {

    @Override
    public Customer create(final UUID id,
                           final String firstName,
                           final String lastName) throws CustomerException {
        this.validateId(id);
        this.validateFirstName(firstName);
        this.validateLastName(lastName);

        final var optionalLastName = Optional.ofNullable(lastName)
            .map(LastName::new);

        return new Customer(new CustomerId(id), new FirstName(firstName), optionalLastName);
    }

    private void validateId(final UUID id) {
        if (id == null) {
            throw new CustomerException("Customer ID must be an UUID");
        }
    }

    private void validateFirstName(final String firstName) {
        if (firstName == null) {
            throw new CustomerException("First name is required");
        }

        if (firstName.length() == 0) {
            throw new CustomerException("First name is too short");
        }

        if (firstName.length() > 30) {
            throw new CustomerException("First name is too long");
        }
    }

    private void validateLastName(final String lastName) {
        if (lastName != null && lastName.length() > 50) {
            throw new CustomerException("Last name is too long");
        }
    }
}
