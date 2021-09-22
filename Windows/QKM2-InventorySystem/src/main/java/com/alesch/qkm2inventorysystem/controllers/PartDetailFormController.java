/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.models.Part;

public class PartDetailFormController {

    private final Part part;

    public PartDetailFormController() {
        part = null;
    }

    public PartDetailFormController(Part part) {
        this.part = part;
    }

    public void initialize() {

    }
}
