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

    private final Part existingPart;

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
        this(-1);
    }

    public PartDetailFormController(int partId) {
        this.existingPart = Inventory.lookupPart(partId);
    }

    //<editor-fold desc="FXML initialization">

    public void initialize() {
        initializeNumberFields();

        if (existingPart == null) { return; }

        formLabel.setText("Modify Part");
        idTextField.setText(String.valueOf(existingPart.getId()));
        nameTextField.setText(existingPart.getName());
        inventoryTextField.setText(String.valueOf(existingPart.getStock()));
        priceTextField.setText(String.valueOf(existingPart.getPrice()));
        maximumTextField.setText(String.valueOf(existingPart.getMax()));
        minimumTextField.setText(String.valueOf(existingPart.getMin()));

        boolean isInHouse = existingPart.getClass() == InHouse.class;

        inHouseRadioButton.setSelected(isInHouse);
        outsourcedRadioButton.setSelected(!isInHouse);

        String extra = isInHouse ? String.valueOf(((InHouse)existingPart).getMachineId()) : ((Outsourced)existingPart).getCompanyName();

        extraLabel.setText(isInHouse ? "Machine ID" : "Company Name");
        extraTextField.setText(extra);
    }

    private void initializeNumberFields() {
        inventoryTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        maximumTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        minimumTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        priceTextField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), 0.0, new DoubleFilter()));

        if (existingPart == null) {
            extraTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        }
    }

    //</editor-fold>

    //<editor-fold desc="FXML event handlers">

    @FXML
    private void inHouseRadioButtonSelected(ActionEvent actionEvent) {
        outsourcedRadioButton.setSelected(false);
        extraLabel.setText("Machine ID");
        extraTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
    }

    @FXML
    private void outsourcedRadioButtonSelected(ActionEvent actionEvent) {
        inHouseRadioButton.setSelected(false);
        extraLabel.setText("Company Name");
        extraTextField.setTextFormatter(null);
        extraTextField.clear();
    }

    @FXML
    private void saveButtonClicked(ActionEvent event) {
        var result = validatePartFields();
        if (!result.isValid()) {
            showErrorText(result.getErrorMessage());
            return;
        }

        saveChanges(result.getValidatedObject());
        closeWindow(event);
    }

    @FXML
    private void cancelButtonClicked(ActionEvent event) {
        closeWindow(event);
    }

    private void saveChanges(Part newPart) {
        if (existingPart == null) {
            Inventory.addPart(newPart);
            return;
        }

        existingPart.setMax(newPart.getMax());
        existingPart.setMin(newPart.getMin());
        existingPart.setName(newPart.getName());
        existingPart.setPrice(newPart.getPrice());
        existingPart.setStock(newPart.getStock());
    }

    //</editor-fold>

    //<editor-fold desc="Helpers">

    private void closeWindow(ActionEvent event) {
        Object source = event.getSource();
        if (source.getClass() != Button.class) {
            return;
        }

        Stage stage = (Stage)((Button)source).getScene().getWindow();
        stage.close();
    }

    private int getNextPartId() {
        Optional<Part> oldMax = Inventory.getAllParts().stream().max(Comparator.comparingInt(Part::getId));
        Random random = new Random();

        return oldMax.map(part -> part.getId() + random.nextInt(100 - part.getId()) + part.getId()).orElse(random.nextInt(100));
    }

    private ValidationResult<Part> validatePartFields() {
        Part part = getPartFromFields();
        String result = FieldValidator.validatePartFields(part);

        return new ValidationResult<>(result, part);
    }

    private Part getPartFromFields() {
        int id = existingPart != null ? existingPart.getId() : getNextPartId();
        String name = nameTextField.getText();
        double price = Math.round(Double.parseDouble(priceTextField.getText()) * 100f) / 100f;
        int stock = Integer.parseInt(inventoryTextField.getText());
        int min = Integer.parseInt(minimumTextField.getText());
        int max = Integer.parseInt(maximumTextField.getText());

        return inHouseRadioButton.isSelected()
                ? new InHouse(id, name, price, stock, min, max, Integer.parseInt(extraTextField.getText()))
                : new Outsourced(id, name, price, stock, min, max, extraTextField.getText());
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
