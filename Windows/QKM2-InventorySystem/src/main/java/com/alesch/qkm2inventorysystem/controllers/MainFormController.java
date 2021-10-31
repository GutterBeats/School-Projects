/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.models.*;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for FXML form that shows the available Part
 * and Product inventory.
 *
 * FUTURE ENHANCEMENT: Could create a superclass
 * that has common elements used across forms to
 * cut down on code duplication, error label, close, etc.
 */
public final class MainFormController {

    //<editor-fold desc="FXML Control Declarations">

    /**
     * Text field used to search for parts; either by ID or name.
     */
    @FXML
    private TextField partSearchField;

    /**
     * Text field used to search products; either by ID or name.
     */
    @FXML
    private TextField productSearchField;

    /**
     * Table view used to display available parts.
     */
    @FXML
    private TableView<Part> partsTableView;

    /**
     * Table column in Parts table for Part ID.
     */
    @FXML
    private TableColumn partId;

    /**
     * Table column in Parts table for Part Name.
     */
    @FXML
    private TableColumn partName;

    /**
     * Table column in Parts table for Part Stock level.
     */
    @FXML
    private TableColumn partInventory;

    /**
     * Table column in Parts table for Part Price.
     */
    @FXML
    private TableColumn partPrice;

    /**
     * Table view used to display available products.
     */
    @FXML
    private TableView<Product> productTableView;

    /**
     * Table column in Products table for Product ID.
     */
    @FXML
    private TableColumn productId;

    /**
     * Table column in Products table for Product Name.
     */
    @FXML
    private TableColumn productName;

    /**
     * Table column in Products table for Product Stock level.
     */
    @FXML
    private TableColumn productInventory;

    /**
     * Table column in Products table for Product Price.
     */
    @FXML
    private TableColumn productPrice;

    /**
     * Label used to display any errors encountered.
     */
    @FXML
    private Label errorLabel;

    //</editor-fold>

    //<editor-fold desc="FXML Initialize">

    /**
     * Used to initialize FXML fields.
     */
    public void initialize() {
        initializePartsTable();
        initializeProductsTable();
    }

    /**
     * Initializes parts table and sets up table columns.
     */
    private void initializePartsTable() {
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        partsTableView.setItems(Inventory.getAllParts());
        partsTableView.setPlaceholder(new Label("No Parts Available"));
    }

    /**
     * Initializes Products table and sets up table columns.
     */
    private void initializeProductsTable() {
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        productTableView.setItems(Inventory.getAllProducts());
        productTableView.setPlaceholder(new Label("No Products Available"));
    }

    //</editor-fold>

    //<editor-fold desc="FXML Event Handlers">

    /**
     * Closes program.
     */
    @FXML
    private void exitButtonClicked() {
        System.exit(0);
    }

    /**
     * Used to open the Part Detail Form to add a new part.
     * <p>
     *     RUNTIME ERROR: Logic is wrapped in try/catch due to NullReferenceException
     *     that was thrown when trying to load form. This was fixed by correcting the
     *     path to the FXML files. Left try/catch just in case any other errors were
     *     caught.
     * </p>
     */
    @FXML
    private void addPartsClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(Resources.getPartDetailFormFXML());
            loader.setController(new PartDetailFormController());

            showStage(loader.load(), Resources.getPartImage(), "Add New Part");
            refreshTableViews();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Used to open the Part Detail Form to modify the selected part.
     * Will display an error if there are no selected parts.
     * <p>
     *     RUNTIME ERROR: Logic is wrapped in try/catch due to NullReferenceException
     *     that was thrown when trying to load form. This was fixed by correcting the
     *     path to the FXML files. Left try/catch just in case any other errors were
     *     caught.
     * </p>
     */
    @FXML
    private void modifyPartsClicked() {
        try {
            if (partsTableView.getItems().isEmpty()) {
                showErrorText("There are no parts to modify.");
                return;
            }

            Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();

            if (selectedPart == null) {
                showErrorText("Please select a part to modify.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(Resources.getPartDetailFormFXML());
            loader.setController(new PartDetailFormController(selectedPart.getId()));

            showStage(loader.load(), Resources.getPartImage(), "Modify " + selectedPart.getName());
            refreshTableViews();
        }
        catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Used to delete the selected part. Will display an error if there are no parts
     * selected or if there was an issue deleted the part.
     */
    @FXML
    private void deletePartsClicked() {
        if (partsTableView.getItems().isEmpty()) {
            showErrorText("There are no parts to delete.");
            return;
        }

        var selectedPart = partsTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            showErrorText("Please select a part to delete.");
            return;
        }

        String confirmationMessage = "Are you sure you want to delete the following part?" +
                System.lineSeparator() + System.lineSeparator() + selectedPart.getName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, confirmationMessage, ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response != ButtonType.YES) return;

            if (!Inventory.deletePart(selectedPart)) {
                showErrorText("There was an error deleting the selected part.");
            }
        });
    }

    /**
     * Used to open the Product Detail Form to add a new product.
     * <p>
     *     RUNTIME ERROR: Logic is wrapped in try/catch due to NullReferenceException
     *     that was thrown when trying to load form. This was fixed by correcting the
     *     path to the FXML files. Left try/catch just in case any other errors were
     *     caught.
     * </p>
     */
    @FXML
    private void addProductClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(Resources.getProductDetailFormFXML());
            loader.setController(new ProductDetailFormController());

            showStage(loader.load(), Resources.getProductImage(), "Add Product");
            refreshTableViews();
        }
        catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Used to open the Product Detail Form to modify the selected product.
     * Will display an error if there are no selected products.
     * <p>
     *     RUNTIME ERROR: Logic is wrapped in try/catch due to NullReferenceException
     *     that was thrown when trying to load form. This was fixed by correcting the
     *     path to the FXML files. Left try/catch just in case any other errors were
     *     caught.
     * </p>
     */
    @FXML
    private void modifyProductClicked() {
        try {
            if (productTableView.getItems().isEmpty()) {
                showErrorText("There are no products to modify.");
                return;
            }

            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

            if (selectedProduct == null) {
                showErrorText("Please select a product to modify.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(Resources.getProductDetailFormFXML());
            loader.setController(new ProductDetailFormController(selectedProduct.getId()));

            showStage(loader.load(), Resources.getProductImage(), "Modify " + selectedProduct.getName());
            refreshTableViews();
        }
        catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Used to delete the selected product. Will display an error if there are no products
     * selected or if there was an issue deleted the product.
     * <p>
     *     Checks to see if there are associated parts before removing product due to requirement G:2.
     * </p>
     */
    @FXML
    private void deleteProductClicked() {
        if (productTableView.getItems().isEmpty()) {
            showErrorText("There are no products to delete.");
            return;
        }

        var selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showErrorText("Please select a product to delete.");
            return;
        }

        if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
            showErrorText("Remove associated parts before deleting product.");
            return;
        }

        String confirmationMessage = "Are you sure you want to delete the following product?" +
                System.lineSeparator() + System.lineSeparator() + selectedProduct.getName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, confirmationMessage, ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response != ButtonType.YES) return;

            if (!Inventory.deleteProduct(selectedProduct)) {
                showErrorText("There was an error deleting the product.");
            }
        });
    }

    /**
     * FXML event handler for the KeyTyped event on the partSearchField.
     * Can be used to search by either part name or id.
     *
     * @param keyEvent Used to get the source of the event.
     */
    @FXML
    private void partSearchField_KeyTyped(KeyEvent keyEvent) {
        Object source = keyEvent.getSource();
        if (source.getClass() != TextField.class) {
            return;
        }

        String partName = ((TextField)source).getText().trim();
        var items = partName.length() == 0 ? Inventory.getAllParts() : Inventory.lookupPart(partName);

        partsTableView.setItems(items);
    }

    /**
     * FXML event handler for the KeyTyped event on the productSearchField.
     * Can be used to search by either product name or id.
     *
     * @param keyEvent Used to get the source of the event.
     */
    @FXML
    private void productSearchField_KeyTyped(KeyEvent keyEvent) {
        Object source = keyEvent.getSource();
        if (source.getClass() != TextField.class) {
            return;
        }

        String productName = ((TextField)source).getText().trim();
        var items = productName.length() == 0 ? Inventory.getAllProducts() : Inventory.lookupProduct(productName);

        productTableView.setItems(items);
    }

    //</editor-fold>

    //<editor-fold desc="Helpers">

    /**
     * Refreshes table view to show any changes or additions made to the parts and products.
     */
    private void refreshTableViews() {
        partsTableView.refresh();
        productTableView.refresh();
    }

    /**
     * Helper method for showing an alert when an exception is thrown.
     *
     * @param e Exception to show in an alert.
     */
    private void handleException(Exception e) {
        e.printStackTrace();

        Alert alert = new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage());
        alert.showAndWait();
    }

    /**
     * Helper method for showing a new stage (Form).
     *
     * @param parent The parent object of the new Scene.
     * @param stageImage The image to use for the stage icon.
     * @param title The title of the stage.
     */
    private void showStage(Parent parent, Image stageImage, String title) {
        Stage stage = new Stage();
        Scene scene = new Scene(parent);

        stage.getIcons().add(stageImage);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.showAndWait();
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