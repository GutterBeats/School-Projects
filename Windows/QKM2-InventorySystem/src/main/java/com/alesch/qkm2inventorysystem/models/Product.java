/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a Product object with all of its attributes and associated Part objects.
 */
public class Product {

    //<editor-fold desc="Fields">

    private final ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    //</editor-fold>

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;

        associatedParts = FXCollections.observableArrayList();
    }

    /**
     * Adds an associated part to the Products list of parts.
     * @param part Part to add to the list.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes an associated part from the Products list of parts.
     * @param selectedAssociatedPart Part to delete from the list.
     * @return Will return whether deleting a part was successful.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Gets all the Products associated parts.
     * @return A list of parts associated with the Product.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * Gets the Product ID.
     * @return The id of the product.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the Product ID.
     * @param id The new ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the Product name.
     * @return The name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Product Name.
     * @param name The new Name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the Product Price.
     * @return The price of the Product.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the Product Price.
     * @param price The new Price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the Product Stock level.
     * @return The Stock level of the Product.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the Product Stock level.
     * @param stock The new Stock level to set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the Minimum Product amount.
     * @return The Minimum Product amount.
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the Minimum Product amount.
     * @param min The new Minimum Product amount.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Gets the Maximum Product amount.
     * @return The Maximum Product amount.
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the Maximum Product amount.
     * @param max The new Maximum Product amount.
     */
    public void setMax(int max) {
        this.max = max;
    }
}
