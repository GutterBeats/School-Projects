/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.models.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.*;

public class PartDetailFormController {

    private final int partId;

    @FXML
    private Label formLabel;

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
    private Label errorLabel;

    @FXML
    private Button cancelButton;

    public PartDetailFormController() {
        this.partId = -1;
    }

    public PartDetailFormController(int partId) {
        this.partId = partId;
    }

    public void initialize() {
        Part part = Inventory.lookupPart(partId);

        if (part == null) { return; }

        formLabel.setText("Modify Part");
        idTextField.setText(String.valueOf(part.getId()));
        nameTextField.setText(part.getName());
        inventoryTextField.setText(String.valueOf(part.getStock()));
        priceTextField.setText(String.valueOf(part.getPrice()));
        maximumTextField.setText(String.valueOf(part.getMax()));
        minimumTextField.setText(String.valueOf(part.getMin()));

        boolean isInHouse = part.getClass() == InHouse.class;

        inHouseRadioButton.setSelected(isInHouse);
        outsourcedRadioButton.setSelected(!isInHouse);

        String extra = isInHouse ? String.valueOf(((InHouse)part).getMachineId()) : ((Outsourced)part).getCompanyName();

        extraLabel.setText(isInHouse ? "Machine ID" : "Company Name");
        extraTextField.setText(extra);
    }

    @FXML
    private void inHouseRadioButtonSelected(ActionEvent actionEvent) {
        outsourcedRadioButton.setSelected(false);
        extraLabel.setText("Machine ID");
    }

    @FXML
    private void outsourcedRadioButtonSelected(ActionEvent actionEvent) {
        inHouseRadioButton.setSelected(false);
        extraLabel.setText("Company Name");
    }

    @FXML
    private void saveButtonClicked() {
        saveChanges();
    }

    @FXML
    private void cancelButtonClicked() {
        closeWindow();
    }

    private void saveChanges() {

        if (!validateFields()) { return; }

        Part part = Inventory.lookupPart(partId);

        int id = part == null ? getNextPartId() : partId;
        String name = nameTextField.getText();
        int inventory = Integer.parseInt(inventoryTextField.getText());
        double price = Double.parseDouble(priceTextField.getText());
        int max = Integer.parseInt(maximumTextField.getText());
        int min = Integer.parseInt(minimumTextField.getText());

        Part upsertPart;

        if (inHouseRadioButton.selectedProperty().get()) {
            int machineId = Integer.parseInt(extraTextField.getText());
            upsertPart = new InHouse(id, name, price, inventory, min, max, machineId);
        }
        else {
            String companyName = extraTextField.getText();
            upsertPart = new Outsourced(id, name, price, inventory, min, max, companyName);
        }

        if (part != null) {
            Inventory.deletePart(part);
        }

        Inventory.addPart(upsertPart);

        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        stage.close();
    }

    private int getNextPartId() {
        Optional<Part> oldMax = Inventory.getAllParts().stream().max(Comparator.comparingInt(Part::getId));

        return oldMax.map(part -> part.getId() + 1).orElse(1);
    }

    private boolean validateFields() {

        if (nameTextField.getText().isBlank()) {
            showErrorText("Name cannot be empty.");
            return false;
        }

        if (!validateNumberField(inventoryTextField)) {
            showErrorText("Inventory must be a number.");
            return false;
        }

        if (!validateNumberField(priceTextField)) {
            showErrorText("Price must be a number.");
            return false;
        }

        if (!validateNumberField(maximumTextField)) {
            showErrorText("Maximum must be a number.");
            return false;
        }

        if (!validateNumberField(minimumTextField)) {
            showErrorText("Minimum must be a number.");
            return false;
        }

        if (extraTextField.getText().isBlank()) {
            showErrorText(errorLabel.getText() + " cannot be blank.");
            return false;
        }

        if (Objects.equals(extraTextField.getText(), "Machine ID") && !validateNumberField(extraTextField)) {
            showErrorText("Machine ID must be a number.");
            return false;
        }

        return true;
    }

    private boolean validateNumberField(TextField textField) {
        try {
            Integer.parseInt(textField.getText());
        }
        catch (Exception e) {
            return false;
        }

        return true;
    }

    private void showErrorText(String message) {
        errorLabel.setText(message);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> errorLabel.setText(""));
            }
        }, 5000);
    }
}
