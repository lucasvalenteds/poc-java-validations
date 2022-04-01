package com.example.result;

import java.util.NoSuchElementException;
import java.util.function.Function;

public sealed interface Result<V, E extends RuntimeException> permits Result.Success, Result.Failure {

    record Success<V, E extends RuntimeException>(V value) implements Result<V, E> {
    }

    record Failure<V, E extends RuntimeException>(E exception) implements Result<V, E> {
    }

    default V value() {
        throw new NoSuchElementException();
    }

    default E exception() {
        throw new NoSuchElementException();
    }

    @SuppressWarnings("unchecked")
    default <T> Result<T, E> flatMap(Function<V, Result<T, E>> mapper) {
        try {
            return mapper.apply(this.value());
        } catch (NoSuchElementException exception) {
            return (Result<T, E>) this;
        }
    }
}
