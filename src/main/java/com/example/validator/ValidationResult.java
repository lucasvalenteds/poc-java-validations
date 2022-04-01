package com.example.validator;

public sealed interface ValidationResult permits ValidationResult.Invalid, ValidationResult.Valid {

    record Valid<V>(V value) implements ValidationResult {
    }

    record Invalid<E extends RuntimeException>(E exception) implements ValidationResult {
    }

    @SuppressWarnings("unchecked")
    default <V> V getOrElseThrow() throws RuntimeException {
        return switch (this) {
            case Valid valid -> (V) valid.value();
            case Invalid invalid -> throw invalid.exception();
        };
    }
}
