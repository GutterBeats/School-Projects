/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Optional;

public class PartDetailFormController {

    private Part temporaryPart;

    @FXML
    private Button cancelButton;

    public PartDetailFormController() {
        temporaryPart = null;
    }

    public PartDetailFormController(Part part) {
        copyPart(part);
    }

    public void initialize() {

    }

    @FXML
    private void saveButtonClicked() {
        saveChanges();
    }

    @FXML
    private void cancelButtonClicked() {
        closeWindow();
    }

    private void copyPart(Part copyFrom) {
        var partClass = copyFrom.getClass();

        if (partClass == InHouse.class) {
            InHouse inHouse = (InHouse)copyFrom;

            temporaryPart = new InHouse(inHouse.getId(), inHouse.getName(), inHouse.getPrice(), inHouse.getStock(), inHouse.getMin(), inHouse.getMax(), inHouse.getMachineId());
            return;
        }

        if (partClass == Outsourced.class) {
            Outsourced outsourced = (Outsourced)copyFrom;

            temporaryPart = new Outsourced(outsourced.getId(), outsourced.getName(), outsourced.getPrice(), outsourced.getStock(), outsourced.getMin(), outsourced.getMax(), outsourced.getCompanyName());
        }
    }

    private void saveChanges() {
        assert temporaryPart != null;

        Inventory.updatePart(temporaryPart.getId(), temporaryPart);
    }

    private void closeWindow() {
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        stage.close();
    }

    private int getNextPartId() {
        Optional<Part> oldMax = Inventory.getAllParts().stream().max(Comparator.comparingInt(Part::getId));

        return oldMax.map(part -> part.getId() + 1).orElse(1);
    }
}
