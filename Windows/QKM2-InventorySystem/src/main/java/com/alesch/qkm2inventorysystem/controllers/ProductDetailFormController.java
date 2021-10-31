/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.models.*;
import com.alesch.qkm2inventorysystem.utils.DoubleFilter;
import com.alesch.qkm2inventorysystem.utils.FieldValidator;
import com.alesch.qkm2inventorysystem.utils.IntegerFilter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.*;

/**
 * Controller for FXML form that allows for creation and
 * editing of Product objects.
 *
 * FUTURE ENHANCEMENT: Could create a superclass
 * that has common elements used across forms to
 * cut down on code duplication.
 *
 * FUTURE ENHANCEMENT: Come up with better solution for
 * validating the fields.
 */
public class ProductDetailFormController {

    //<editor-fold desc="Fields">

    /**
     * Used to determine whether this form is being used to edit an
     * existing product or create a new one. Will be null if creating
     * a new Product.
     */
    private final Product existingProduct;

    //</editor-fold>

    //<editor-fold desc="FXML Controls">

    /**
     * A label for the title of the form.
     */
    @FXML
    private Label titleLabel;

    /**
     * Text field used for the ID of the Product.
     * Cannot be edited.
     */
    @FXML
    private TextField idTextField;

    /**
     * Text field used for the Name of the Product.
     */
    @FXML
    private TextField nameTextField;

    /**
     * Text field used for the Stock level of the Product.
     * Will only allow Integer input.
     */
    @FXML
    private TextField inventoryTextField;

    /**
     * Text field used for the Price of the Product.
     * Will only allow Double input.
     */
    @FXML
    private TextField priceTextField;

    /**
     * Text field used for the Maximum level
     * of the Product. Will only allow Integer
     * input.
     */
    @FXML
    private TextField maximumTextField;

    /**
     * Text field used for the Minimum level
     * of the Product. Will only allow Integer
     * input.
     */
    @FXML
    private TextField minimumTextField;

    /**
     * Text field used to search for available parts
     * to add to the product.
     */
    @FXML
    private TextField partSearchTextField;

    /**
     * Table view used to show the available parts
     * to associate with a product.
     */
    @FXML
    private TableView<Part> partTableView;

    /**
     * Part ID column in the Part Table View.
     */
    @FXML
    private TableColumn partPartIdColumn;

    /**
     * Part Name column in the Part Table View.
     */
    @FXML
    private TableColumn partPartNameColumn;

    /**
     * Part Inventory column in the Part Table View.
     */
    @FXML
    private TableColumn partInventoryLevelColumn;

    /**
     * Part Price column in the Part Table View.
     */
    @FXML
    private TableColumn partPriceColumn;

    /**
     * Label used to show errors encountered.
     */
    @FXML
    private Label errorLabel;

    /**
     * Table view used to show the associated parts
     * of the product.
     */
    @FXML
    private TableView<Part> productPartTableView;

    /**
     * Part ID column in the Product Part Table View.
     */
    @FXML
    private TableColumn productPartIdColumn;

    /**
     * Part Name column in the Product Part Table View.
     */
    @FXML
    private TableColumn productPartNameColumn;

    /**
     * Part Inventory column in the Product Part Table View.
     */
    @FXML
    private TableColumn productPartInventoryColumn;

    /**
     * Part Price column in the Product Part Table View.
     */
    @FXML
    private TableColumn productPartPriceColumn;

    //</editor-fold>

    //<editor-fold desc="Construction & Initialization">


    /**
     * Creates new ProductDetailFormController.
     */
    public ProductDetailFormController() {
        this(-1);
    }

    /**
     * Creates new ProductDetailFormController and looks up by product Id.
     * @param productId Id to use to initialize ProductDetailFormController.
     */
    public ProductDetailFormController(int productId) {
        this.existingProduct = Inventory.lookupProduct(productId);
    }

    /**
     * Initializes FXML fields.
     */
    public void initialize() {
        loadProductPartsTable(existingProduct);
        loadAllPartsTable();
        initializeNumberFields();

        if (existingProduct == null) return;

        idTextField.setText(String.valueOf(existingProduct.getId()));
        nameTextField.setText(existingProduct.getName());
        inventoryTextField.setText(String.valueOf(existingProduct.getStock()));
        priceTextField.setText(String.valueOf(existingProduct.getPrice()));
        maximumTextField.setText(String.valueOf(existingProduct.getMax()));
        minimumTextField.setText(String.valueOf(existingProduct.getMin()));
    }

    /**
     * Helper method to initialize the number fields with
     * TextFormatter to limit input depending on what they're
     * used for.
     */
    private void initializeNumberFields() {
        inventoryTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        maximumTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        minimumTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        priceTextField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), 0.0, new DoubleFilter()));
    }

    /**
     * Helper method to load the All Parts table view with
     * all available parts. Also sets up table columns and
     * a placeholder label for when there are no parts
     * available.
     */
    private void loadAllPartsTable() {
        partPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        partTableView.setPlaceholder(new Label("No Parts Available"));
        partTableView.setItems(Inventory.getAllParts());
    }

    /**
     * Helper method to load all of the associated parts of the product.
     * Also sets up table columns and a placeholder label for when there
     * are no parts to show.
     *
     * @param product the product to show the associated parts for.
     */
    private void loadProductPartsTable(Product product) {
        productPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productPartTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        productPartTableView.setPlaceholder(new Label("No Parts Available"));

        if (product == null) return;

        ObservableList<Part> productParts = FXCollections.observableArrayList();

        for (Part part : product.getAllAssociatedParts()) {
            productParts.add(part);
        }

        productPartTableView.setItems(productParts);
    }

    //</editor-fold>

    //<editor-fold desc="FXML Event Handlers">

    /**
     * FXML event handler for the KeyTyped event on the partSearchTextField.
     * Can be used to search by either part name or id.
     *
     * @param keyEvent Used to get the source of the event.
     */
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

    /**
     * FXML event handler for when the add part button is clicked.
     * Validates the selected part then adds it to the list of
     * associated part.
     */
    @FXML
    private void addPartButtonClicked() {
        var selectedItem = partTableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showErrorText("Please select a part to add.");
            return;
        }

        productPartTableView.getItems().add(selectedItem);
    }

    /**
     * FXML event handler for when the remove associated part
     * button is clicked. Will validate the selected part
     * and show a confirmation dialog to confirm that you
     * want to remove the part.
     */
    @FXML
    private void removeAssociatedPartButtonClicked() {
        if (productPartTableView.getItems().isEmpty()) {
            showErrorText("There are no parts to remove.");
            return;
        }

        var selectedItem = productPartTableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showErrorText("Please select a part to remove.");
            return;
        }

        String confirmationText = "Are you sure you want to remove the selected part from this product?" +
                System.lineSeparator() + System.lineSeparator() + selectedItem.getName();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, confirmationText, ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response != ButtonType.YES) {
                showErrorText(selectedItem.getName() + " was not removed.");
                return;
            }

            productPartTableView.getItems().remove(selectedItem);
        });
    }

    /**
     * Handles event for when save button is clicked.
     * Validates the fields used to set the properties on
     * the product object. Will show an error if there are
     * any issues. After validating, will save edits and
     * close the window.
     *
     * @param event used to pass to the closeWindow() method.
     */
    @FXML
    private void saveButtonClicked(ActionEvent event) {
        var validationResult = validateChanges();
        if (validationResult.isNotValid()) {
            showErrorText(validationResult.getErrorMessage());
            return;
        }

        saveChanges(validationResult.getValidatedObject());
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
     * Utility method used to validate the fields for the
     * product.
     *
     * @return a result object that contains the validated object or an error.
     */
    private ValidationResult<Product> validateChanges() {
        Product newProduct = new Product(
            existingProduct != null ? existingProduct.getId() : getNextProductId(),
            nameTextField.getText(),
            Double.parseDouble(priceTextField.getText()),
            Integer.parseInt(inventoryTextField.getText()),
            Integer.parseInt(minimumTextField.getText()),
            Integer.parseInt(maximumTextField.getText())
        );

        String result = FieldValidator.validateProductFields(newProduct);

        return new ValidationResult<>(result, newProduct);
    }

    /**
     * Saves the changes made to the new or existing Product
     * object.
     *
     * @param newProduct the product object to save.
     */
    private void saveChanges(Product newProduct) {
        if (existingProduct == null) {
            for (Part part : productPartTableView.getItems()) {
                newProduct.addAssociatedPart(part);
            }

            Inventory.addProduct(newProduct);
            return;
        }

        existingProduct.getAllAssociatedParts().clear();

        for (Part part : productPartTableView.getItems()) {
            existingProduct.addAssociatedPart(part);
        }

        existingProduct.setMax(newProduct.getMax());
        existingProduct.setMin(newProduct.getMin());
        existingProduct.setName(newProduct.getName());
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setStock(newProduct.getStock());
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

    /**
     * Utility method used to get a random product id.
     * Will check existing product id's to make sure there
     * is not a duplicate.
     *
     * @return the next product id.
     */
    private int getNextProductId() {
        Optional<Product> oldMax = Inventory.getAllProducts().stream().max(Comparator.comparingInt(Product::getId));
        Random random = new Random();
        int nextPossible = oldMax.map(product -> product.getId() + random.nextInt(5000 - product.getId()) + product.getId()).orElse(random.nextInt(5000));

        return Inventory.getAllProducts().stream().map(pr -> pr.getId()).anyMatch(n -> n == nextPossible) ? getNextProductId() : nextPossible;
    }

    //</editor-fold>
}
