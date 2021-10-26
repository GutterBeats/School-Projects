/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.models;

public class ValidationResult<T> {
    private final String errorMessage;
    private final T validatedObject;

    public ValidationResult(String errorMessage, T validatedObject) {
        this.errorMessage = errorMessage;
        this.validatedObject = validatedObject;
    }

    public boolean isValid() {
        return errorMessage.equals("");
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public T getValidatedObject() {
        return validatedObject;
    }
}
