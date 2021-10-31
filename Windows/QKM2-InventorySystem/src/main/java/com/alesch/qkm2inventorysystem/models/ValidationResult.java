/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.models;

/**
 * Class is used to package an error message and validated object.
 * @param <T> Generic type of the object being returned for validation.
 */
public class ValidationResult<T> {
    private final String errorMessage;
    private final T validatedObject;

    public ValidationResult(String errorMessage, T validatedObject) {
        this.errorMessage = errorMessage;
        this.validatedObject = validatedObject;
    }

    /**
     * Tests whether there is an error message present to determine if the validated object is valid or not.
     * @return A boolean to determine if the validated object is valid.
     */
    public boolean isNotValid() {
        return !errorMessage.equals("");
    }

    /**
     * Gets the error message for the object.
     * @return A String containing the error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Gets the object that was validated.
     * @return An object of the type determined when creating an instance of this class.
     */
    public T getValidatedObject() {
        return validatedObject;
    }
}
