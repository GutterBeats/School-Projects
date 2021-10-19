/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.models.*;
import com.alesch.qkm2inventorysystem.utils.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.*;

public class PartDetailFormController {

    //<editor-fold desc="Fields">

    private final int partId;

    //</editor-fold>

    //<editor-fold desc="FXML Controls">

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

    //</editor-fold>

    public PartDetailFormController() {
        this.partId = -1;
    }

    public PartDetailFormController(int partId) {
        this.partId = partId;
    }

    //<editor-fold desc="FXML initialization">

    public void initialize() {
        initializeNumberFields();

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

    private void initializeNumberFields() {
        inventoryTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        maximumTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        minimumTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        priceTextField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), 0.0, new DoubleFilter()));
    }

    //</editor-fold>

    //<editor-fold desc="FXML event handlers">

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

    //</editor-fold>

    //<editor-fold desc="Helpers">

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
            nameTextField.requestFocus();
            return false;
        }

        if (extraTextField.getText().isBlank()) {
            showErrorText(errorLabel.getText() + " cannot be blank.");
            extraTextField.requestFocus();
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

    //</editor-fold>
}
