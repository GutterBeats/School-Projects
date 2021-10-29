/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;

public final class Inventory {
    private static final ObservableList<Part> allParts;
    private static final ObservableList<Product> allProducts;

    static {
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
    }

    /**
     * Add a new part to the Inventory. Will exit early if new Part already exists in Inventory.
     * @param newPart The new part to add to the Inventory.
     */
    public static void addPart(Part newPart) {
        Part existingPart = lookupPart(newPart.getId());

        if (existingPart != null) {
            return;
        }

        allParts.add(newPart);
    }

    /**
     * Add a new product to the Inventory. Will exit early if new Product is already in the Inventory.
     * @param newProduct The new product to add to the Inventory
     */
    public static void addProduct(Product newProduct) {
        Product existingProduct = lookupProduct(newProduct.getId());

        if (existingProduct != null) {
            return;
        }

        allProducts.add(newProduct);
    }

    /**
     * Look up a part in the Inventory. Will return the first matched Part.
     * @param partId The ID of the Part to find.
     * @return A Part with the ID passed in.
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts) {
            if (part.getId() != partId) {
                continue;
            }

            // Product was found, return the Part
            return part;
        }

        // Part was not found, return null
        return null;
    }

    /**
     * Look up a part in the Inventory based on its name. Will return all parts with similar names.
     * @param partNameOrId The name or id of the part to look for.
     * @return An ObservableList of Part objects that match the given part name.
     */
    public static ObservableList<Part> lookupPart(String partNameOrId) {
        try {
            int id = Integer.parseInt(partNameOrId);
            Part part = lookupPart(id);
            String name = part != null ? part.getName() : "___unknown_part___";

            return allParts.filtered(filtered -> filtered.getName().equals(name));
        }
        catch (Exception ignored) { }

        return allParts.filtered(part -> part.getName().toLowerCase(Locale.ROOT).contains(partNameOrId.toLowerCase(Locale.ROOT)));
    }

    /**
     * Look up a Product in the Inventory. Will return the first matched Product.
     * @param productId The ID of the Product to find.
     * @return A Product instance from the Inventory or null if no match was found.
     */
    public static Product lookupProduct(int productId) {
        // Loop through all Products to find a matching ProductID
        for (Product product : allProducts) {
            if (product.getId() != productId) {
                continue;
            }

            // Product found, return the product
            return product;
        }

        // Product was not found, return null
        return null;
    }

    /**
     * Look up a product in the Inventory based on its name. Will return all products with similar names.
     * @param productNameOrId The name or id of the product to look for.
     * @return An ObservableList of Product objects that match the given product name.
     */
    public static ObservableList<Product> lookupProduct(String productNameOrId) {
        try {
            int id = Integer.parseInt(productNameOrId);
            Product product = lookupProduct(id);
            String name = product != null ? product.getName() : "___unknown_product___";

            return allProducts.filtered(filtered -> filtered.getName().equals(name));
        }
        catch (Exception ignored) { }

        return allProducts.filtered(product -> product.getName().toLowerCase(Locale.ROOT).contains(productNameOrId.toLowerCase(Locale.ROOT)));
    }

    /**
     * Updates the selected part at the given index.
     * @param index The index of the part to update.
     * @param selectedPart The new part to set at the index.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates the selected product at the given index.
     * @param index The index of the product to update.
     * @param selectedProduct The new product to set at the index.
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Deletes the given part from the Inventory and returns if it was found or not.
     * @param selectedPart The selected part to remove from the Inventory.
     * @return A boolean to indicate if the Part was found when deleting.
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes the given Product from the Inventory and returns if it was found or not.
     * @param selectedProduct The selected product to remove from the Inventory.
     * @return A boolean to indicate if the Product was found when deleting.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Get all the Parts within the Inventory.
     * @return An ObservableList of Part objects.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Get all the Products within the Inventory.
     * @return An ObservableList of Product objects.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
