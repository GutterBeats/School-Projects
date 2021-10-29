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

public class ProductDetailFormController {

    //<editor-fold desc="Fields">

    private final Product existingProduct;

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

    //<editor-fold desc="Construction & Initialization">

    public ProductDetailFormController() {
        this(-1);
    }

    public ProductDetailFormController(int productId) {
        this.existingProduct = Inventory.lookupProduct(productId);
    }

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

    private void initializeNumberFields() {
        inventoryTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        maximumTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        minimumTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, new IntegerFilter()));
        priceTextField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), 0.0, new DoubleFilter()));
    }

    private void loadAllPartsTable() {
        partPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        partTableView.setPlaceholder(new Label("No Parts Available"));
        partTableView.setItems(Inventory.getAllParts());
    }

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
    private void addPartButtonClicked(ActionEvent event) {
        var selectedItem = partTableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showErrorText("Please select a part to add.");
            return;
        }

        productPartTableView.getItems().add(selectedItem);
    }

    @FXML
    private void removeAssociatedPartButtonClicked(ActionEvent event) {
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
            if (response != ButtonType.YES) return;

            productPartTableView.getItems().remove(selectedItem);
        });
    }

    @FXML
    private void saveButtonClicked(ActionEvent event) {
        var validationResult = validateChanges();
        if (!validationResult.isValid()) {
            showErrorText(validationResult.getErrorMessage());
            return;
        }

        saveChanges(validationResult.getValidatedObject());
        exit(event);
    }

    @FXML
    private void cancelButtonClicked(ActionEvent event) {
        exit(event);
    }

    //</editor-fold>

    //<editor-fold desc="Helpers">

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

    private int getNextProductId() {
        Optional<Product> oldMax = Inventory.getAllProducts().stream().max(Comparator.comparingInt(Product::getId));
        Random random = new Random();
        int nextPossible = oldMax.map(product -> product.getId() + random.nextInt(5000 - product.getId()) + product.getId()).orElse(random.nextInt(5000));

        return Inventory.getAllProducts().stream().map(pr -> pr.getId()).anyMatch(n -> n == nextPossible) ? getNextProductId() : nextPossible;
    }

    //</editor-fold>
}
