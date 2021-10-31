/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.models;

/**
 * Concrete class extending the abstract Part class. Represents an InHouse Part object type.
 */
public class InHouse extends Part {
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);

        this.machineId = machineId;
    }

    /**
     * Get the Machine ID of the InHouse Part.
     * @return The Machine ID
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Set the Machine ID of the InHouse Part.
     * @param machineId The Machine ID to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
