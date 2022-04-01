package com.example.layered;

import java.util.UUID;

public interface CustomerService {

    Customer create(final UUID id,
                    final String firstName,
                    final String lastName) throws CustomerException;
}
