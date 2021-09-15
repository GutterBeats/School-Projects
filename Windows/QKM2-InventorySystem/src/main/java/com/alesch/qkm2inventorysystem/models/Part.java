/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.models;

/**
 * Abstract class used to define different Parts.
 */
public abstract class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Creates new Part object using the specified inputs.
     * @param id The ID of the Part.
     * @param name The Name of the Part.
     * @param price The Price of the Part.
     * @param stock The amount of Stock available for the Part.
     * @param min The Minimum amount of stock to keep on hand for the Part.
     * @param max The Maximum amount of stock to keep on hand for the Part.
     */
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Get the Part ID.
     * @return The Part ID
     */
    public int getId() {
        return id;
    }

    /**
     * Set the Part ID.
     * @param id Sets the Part ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the Name of the Part.
     * @return The Name of the Part
     */
    public String getName() {
        return name;
    }

    /**
     * Set the Name of the Part.
     * @param name The Name of the Part
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the Price of the Part.
     * @return The Price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set the Price of the Part.
     * @param price The Price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Get the amount of the Part in Stock.
     * @return The Stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Set how much of the Part is in Stock.
     * @param stock The Stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Get the Minimum amount of the Part to keep in stock.
     * @return The Minimum
     */
    public int getMin() {
        return min;
    }

    /**
     * Set the Minimum amount of the Part to keep in stock.
     * @param min The Minimum to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Get the Maximum amount of the Part to keep in stock.
     * @return The Maximum
     */
    public int getMax() {
        return max;
    }

    /**
     * Set the Maximum amount of the Part to keep in stock.
     * @param max The Maximum to set
     */
    public void setMax(int max) {
        this.max = max;
    }
}
