/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.models;

public class Outsourced extends Part {
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);

        this.companyName = companyName;
    }

    /**
     * Get the Company Name associated with the Outsourced Part.
     * @return The Company Name.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set the Company Name associated with the Outsourced Part.
     * @param companyName The Company Name to Set.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
