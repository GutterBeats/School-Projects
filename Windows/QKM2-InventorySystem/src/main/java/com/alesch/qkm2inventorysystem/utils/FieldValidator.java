/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.utils;

import com.alesch.qkm2inventorysystem.models.InHouse;
import com.alesch.qkm2inventorysystem.models.Outsourced;
import com.alesch.qkm2inventorysystem.models.Product;

public class FieldValidator {
    private FieldValidator() {}

    public static String validateProductFields(Product product) {
        if (product.getName().isBlank()) {
            return "Name cannot be blank.";
        }

        if (product.getMax() < 1) {
            return "Maximum must be at least one.";
        }

        if (product.getMin() < 1) {
            return "Minimum must be at least one.";
        }

        if (product.getMin() >= product.getMax()) {
            return "Maximum must be greater than Minimum.";
        }

        var stock = product.getStock();
        if (stock < product.getMin() || stock > product.getMax()) {
            return "Inventory must be between Minimum and Maximum.";
        }

        if (Double.compare(product.getPrice(), 0) <= 0) {
            return "Price must be greater than Zero.";
        }

        return "";
    }

    public static String validatePartFields(InHouse inHouse) {
        return "";
    }

    public static String validatePartFields(Outsourced outsourced) {
        return "";
    }
}
