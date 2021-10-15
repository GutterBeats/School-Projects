/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.models.Inventory;
import com.alesch.qkm2inventorysystem.models.Part;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailFormController {

    //<editor-fold desc="Fields">

    private final int productId;

    //</editor-fold>

    //<editor-fold desc="FXML Controls">

    @FXML
    private Label titleLabel;

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
    private TextField partSearchTextField;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn partPartIdColumn;

    @FXML
    private TableColumn partPartNameColumn;

    @FXML
    private TableColumn partInventoryLevelColumn;

    @FXML
    private TableColumn partPriceColumn;

    @FXML
    private Label errorLabel;

    @FXML
    private TableView<Part> productPartTableView;

    @FXML
    private TableColumn productPartIdColumn;

    @FXML
    private TableColumn productPartNameColumn;

    @FXML
    private TableColumn productPartInventoryColumn;

    @FXML
    private TableColumn productPartPriceColumn;

    //</editor-fold>

    public ProductDetailFormController() {
        productId = -1;
    }

    public ProductDetailFormController(int productId) {
        this.productId = productId;
    }

    public void initialize() {

    }

    //<editor-fold desc="FXML Event Handlers">

    @FXML
    private void partSearchTextField_KeyTyped(KeyEvent keyEvent) {
        Object source = keyEvent.getSource();
        if (source.getClass() != TextField.class) {
            return;
        }

        String partName = ((TextField)source).getText().trim();
        var items = partName.length() == 0 ? Inventory.getAllParts() : Inventory.lookupPart(partName);

        partTableView.setItems(items);
    }

    @FXML
    private void cancelButtonClicked(ActionEvent event) {
        discardChanges();
        exit(event);
    }

    //</editor-fold>

    //<editor-fold desc="Helpers">

    private void discardChanges() {

    }

    private void exit(ActionEvent event) {
        Object source = event.getSource();
        if (source.getClass() != Button.class) {
            return;
        }

        Stage stage = (Stage)((Button)source).getScene().getWindow();
        stage.close();
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
