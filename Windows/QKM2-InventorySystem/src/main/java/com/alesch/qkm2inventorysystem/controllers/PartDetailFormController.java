/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.InventorySystem;
import com.alesch.qkm2inventorysystem.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Optional;

public class PartDetailFormController {

    private Part temporaryPart;
    private boolean isNewPart = false;

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private RadioButton outsourcedRadioButton;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField inventoryTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField maximumTextField;

    @FXML
    private TextField minimumTextField;

    @FXML
    private Label extraLabel;

    @FXML
    private TextField extraTextField;

    @FXML
    private Button cancelButton;

    public PartDetailFormController() {
        temporaryPart = null;
    }

    public PartDetailFormController(Part part) {
        copyPart(part);
    }

    public void initialize() {
        if (temporaryPart == null) {
            temporaryPart = getNewInHousePart();
            isNewPart = true;
        }

        var isInHouse = temporaryPart.getClass() == InHouse.class;

        inHouseRadioButton.setSelected(isInHouse);
        inHouseRadioButton.setDisable(!isNewPart);

        outsourcedRadioButton.setSelected(!isInHouse);
        outsourcedRadioButton.setDisable(!isNewPart);

        idTextField.setText(String.valueOf(temporaryPart.getId()));
        nameTextField.setText(temporaryPart.getName());
        inventoryTextField.setText(String.valueOf(temporaryPart.getStock()));
        priceTextField.setText(String.valueOf(temporaryPart.getPrice()));
        maximumTextField.setText(String.valueOf(temporaryPart.getMax()));
        minimumTextField.setText(String.valueOf(temporaryPart.getMax()));

        initializeExtraFields(isInHouse, temporaryPart);
    }

    @FXML
    private void inHouseRadioButtonSelected(ActionEvent actionEvent) {
        outsourcedRadioButton.setSelected(false);
        temporaryPart = getNewInHousePart();

        initializeExtraFields(true, temporaryPart);
    }

    @FXML
    private void outsourcedRadioButtonSelected(ActionEvent actionEvent) {
        inHouseRadioButton.setSelected(false);
        temporaryPart = getNewOutsourcePart();

        initializeExtraFields(false, temporaryPart);
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

        if (isNewPart) {
            Inventory.addPart(temporaryPart);
            return;
        }

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

    private void initializeExtraFields(boolean isInHousePart, Part part) {
        if (isInHousePart) {
            var inHouse = (InHouse)part;

            extraLabel.setText("Machine ID");
            extraTextField.setText(String.valueOf(inHouse.getMachineId()));
        }
        else {
            var outsourced = (Outsourced)part;

            extraLabel.setText("Company Name");
            extraTextField.setText(outsourced.getCompanyName());
        }
    }

    private InHouse getNewInHousePart() {
        return new InHouse(getNextPartId(), "", 0.0, 0, 0, 0, 0);
    }

    private Outsourced getNewOutsourcePart() {
        return new Outsourced(getNextPartId(), "", 0.0, 0, 0, 0, "");
    }
}
