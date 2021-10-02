/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.models.Part;

public class PartDetailFormController {

    private Part part;

    public void initialize() {
        if (part == null) return;


    }

    public void setPart(Part part) {
        this.part = part;
    }
}
