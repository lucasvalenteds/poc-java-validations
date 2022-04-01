package com.example.layered;

import java.io.Serial;

public final class CustomerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6696314332836065506L;

    public CustomerException(String message) {
        super(message);
    }
}
