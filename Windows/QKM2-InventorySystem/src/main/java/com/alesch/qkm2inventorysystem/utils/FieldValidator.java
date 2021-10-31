/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.utils;

import com.alesch.qkm2inventorysystem.models.InHouse;
import com.alesch.qkm2inventorysystem.models.Outsourced;
import com.alesch.qkm2inventorysystem.models.Part;
import com.alesch.qkm2inventorysystem.models.Product;

/**
 * Static utility class used to help with field validation of Product & Part objects.
 */
public final class FieldValidator {
    private FieldValidator() {}

    /**
     * Used to validate the fields of a Product object.
     *
     * @param product the Product object to validate fields for.
     * @return a String containing the first error encountered when validating.
     */
    public static String validateProductFields(Product product) {
        if (product.getName().isBlank()) {
            return "Name cannot be blank.";
        }

        if (product.getMax() < 1) {
            return "Maximum must be at least One.";
        }

        if (product.getMin() < 1) {
            return "Minimum must be at least One.";
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

    /**
     * Used to validate the fields of a Part object; both InHouse and Outsourced.
     *
     * @param part the Part object to validate fields for.
     *             Will check whether it is an InHouse or Outsource object and
     *             validated accordingly.
     *
     * @return a String contain the first error encountered when validating.
     */
    public static String validatePartFields(Part part) {
        if (part.getName().isBlank()) {
            return "Name cannot be blank.";
        }

        if (part.getMax() < 1) {
            return "Maximum must be at least One.";
        }

        if (part.getMin() < 1) {
            return "Minimum must be at least One.";
        }

        if (part.getMin() >= part.getMax()) {
            return "Maximum must be greater than Minimum.";
        }

        var stock = part.getStock();
        if (stock < part.getMin() || stock > part.getMax()) {
            return "Inventory must be between Minimum and Maximum.";
        }

        if (Double.compare(part.getPrice(), 0) <= 0) {
            return "Price must be greater than Zero.";
        }

        if (part.getClass() == InHouse.class) {
            return ((InHouse)part).getMachineId() < 1 ? "Machine ID must be greater than Zero." : "";
        }

        return ((Outsourced)part).getCompanyName().isBlank() ? "Company name cannot be blank." : "";
    }
}
