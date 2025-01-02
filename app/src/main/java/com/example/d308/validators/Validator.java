package com.example.d308.validators;

public interface Validator<T> {
    boolean isValid(T object);
    String getErrorMessage();
}
