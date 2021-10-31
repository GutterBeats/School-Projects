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

/**
 * Controller for FXML form that allows for creation and
 * editing of Part objects.
 *
 * FUTURE ENHANCEMENT: Could create a superclass
 * that has common elements used across forms to
 * cut down on code duplication.
 *
 * FUTURE ENHANCEMENT: Come up with better solution for
 * validating the fields.
 */
public class PartDetailFormController {

    //<editor-fold desc="Fields">

    /**
     * Used to determine whether this form is being used to edit an
     * existing part or create a new one. Will be null if creating
     * a new Part.
     */
    private final Part existingPart;

    //</editor-fold>

    //<editor-fold desc="FXML Controls">

    /**
     * A label for the title of the form.
     */
    @FXML
    private Label formLabel;

    /**
     * Radio button used to change the part type to
     * an InHouse part.
     */
    @FXML
    private RadioButton inHouseRadioButton;

    /**
     * Radio button used to change the part type to
     * an Outsourced part.
     */
    @FXML
    private RadioButton outsourcedRadioButton;

    /**
     * Text field used for the ID of the Part.
     * Cannot be edited.
     */
    @FXML
    private TextField idTextField;

    /**
     * Text field used for the Name of the Part.
     */
    @FXML
    private TextField nameTextField;

    /**
     * Text field used for the Stock level of the Part.
     * Will only allow Integer input.
     */
    @FXML
    private TextField inventoryTextField;

    /**
     * Text field used for the Price of the Part.
     * Will only allow Double input.
     */
    @FXML
    private TextField priceTextField;

    /**
     * Text field used for the Maximum level
     * of the Part. Will only allow Integer
     * input.
     */
    @FXML
    private TextField maximumTextField;

    /**
     * Text field used for the Minimum level
     * of the Part. Will only allow Integer
     * input.
     */
    @FXML
    private TextField minimumTextField;

    /**
     * Extra label used for either the Machine ID
     * or Company Name properties.
     */
    @FXML
    private Label extraLabel;

    /**
     * Extra text field used to input either the
     * Machine ID or Company Name of a Part.
     * Will only allow Integer input if InHouse
     * part type is selected.
     */
    @FXML
    private TextField extraTextField;

    /**
     * Label used to show errors encountered.
     */
    @FXML
    private Label errorLabel;

    //</editor-fold>

    /**
     * Creates a new PartDetailFormController.
     */
    public PartDetailFormController() {
        this(-1);
    }

    /**
     * Creates a new PartDetailFormController.
     * Will used passed in partId to lookup an existing Part.
     *
     * @param partId id of the part to lookup.
     */
    public PartDetailFormController(int partId) {
        this.existingPart = Inventory.lookupPart(partId);
    }

    //<editor-fold desc="FXML initialization">

    /**
     * Initializes the FXML fields.
     */
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

    /**
     * Helper method to initialize the number fields with
     * TextFormatter to limit input depending on what they're
     * used for.
     *
     * RUNTIME ERROR: Was trying to reuse TextFormatter objects but got an exception.
     * Fixed by creating new TextFormatter objects for each TextField.
     */
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

    /**
     * Handles event for when the InHouse radio button is selected.
     * Will toggle selected property of Outsource radio button
     * so that only one is selected at a time.
     */
    @FXML
    private void inHouseRadioButtonSelected() {
        outsourcedRadioButton.setSelected(false);
        extraLabel.setText("Machine ID");
        extraTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
    }

    /**
     * Handles event for when the Outsourced radio button is selected.
     * Will toggle selected property of InHouse radio button
     * so that only one is selected at a time.
     */
    @FXML
    private void outsourcedRadioButtonSelected() {
        inHouseRadioButton.setSelected(false);
        extraLabel.setText("Company Name");
        extraTextField.setTextFormatter(null);
        extraTextField.clear();
    }

    /**
     * Handles event for when save button is clicked.
     * Validates the fields used to set the properties on
     * the Part object. Will show an error if there are
     * any issues. After validating, will save edits and
     * close the window.
     *
     * @param event used to pass to the closeWindow() method.
     */
    @FXML
    private void saveButtonClicked(ActionEvent event) {
        var result = validatePartFields();
        if (result.isNotValid()) {
            showErrorText(result.getErrorMessage());
            return;
        }

        saveChanges(result.getValidatedObject());
        closeWindow(event);
    }

    /**
     * Handles event for when the cancel button is clicked.
     * Cancels any edits and closes the window.
     *
     * @param event used to pass to the closeWindow() method.
     */
    @FXML
    private void cancelButtonClicked(ActionEvent event) {
        closeWindow(event);
    }

    //</editor-fold>

    //<editor-fold desc="Helpers">

    /**
     * Saves the changes made to the new or existing Part
     * object.
     *
     * @param newPart the part object to save.
     */
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

    /**
     * Closes the window.
     *
     * @param event used to get the source of the event
     *              to use in closing the window.
     */
    private void closeWindow(ActionEvent event) {
        Object source = event.getSource();
        if (source.getClass() != Button.class) {
            return;
        }

        Stage stage = (Stage)((Button)source).getScene().getWindow();
        stage.close();
    }

    /**
     * Utility method used to get a random part id.
     * Will check existing product id's to make sure there
     * is not a duplicate.
     *
     * @return the next part id.
     */
    private int getNextPartId() {
        Optional<Part> oldMax = Inventory.getAllParts().stream().max(Comparator.comparingInt(Part::getId));
        Random random = new Random();
        int nextPossible = oldMax.map(part -> part.getId() + random.nextInt(100 - part.getId()) + part.getId()).orElse(random.nextInt(100));

        return Inventory.getAllParts().stream().map(part -> part.getId()).anyMatch(n -> n == nextPossible) ? getNextPartId() : nextPossible;
    }

    /**
     * Utility method used to validate the fields for the
     * part.
     *
     * @return a result object that contains the validated object or an error.
     */
    private ValidationResult<Part> validatePartFields() {
        Part part = getPartFromFields();
        String result = FieldValidator.validatePartFields(part);

        return new ValidationResult<>(result, part);
    }

    /**
     * Utility method used to get a new part object from the
     * fields of the form.
     *
     * @return a new Part object.
     */
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

    /**
     * Helper method for showing an error. Will show the error for five seconds, then clear it.
     * <p>
     *     RUNTIME ERROR: Exception was being thrown when trying to clear the error on the
     *     TimerTask background thread. Solved by using Platform.runLater() to jump to the
     *     UI thread before clearing the error.
     * </p>
     *
     * @param message Message to show in the error label.
     */
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
