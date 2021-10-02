/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.InventorySystem;
import com.alesch.qkm2inventorysystem.models.InHouse;
import com.alesch.qkm2inventorysystem.models.Inventory;
import com.alesch.qkm2inventorysystem.models.Part;
import com.alesch.qkm2inventorysystem.models.Product;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public final class MainFormController {

    private Timer partSearchTimer = new Timer();
    private Timer productSearchTimer = new Timer();

    @FXML
    public TextField partSearchField;
    public TextField productSearchField;

    @FXML
    public TableView<Part> partsTableView;
    public TableColumn partId;
    public TableColumn partName;
    public TableColumn partInventory;
    public TableColumn partPrice;

    @FXML
    public TableView<Product> productTableView;
    public TableColumn productId;
    public TableColumn productName;
    public TableColumn productInventory;
    public TableColumn productPrice;

    @FXML
    private Label errorLabel;

    public void initialize() {
        initializePartsTable();
        initializeProductsTable();
    }

    private void initializePartsTable() {
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        Inventory.addPart(new InHouse(1, "Brakes", 15.00, 10, 1, 20, 1));
        Inventory.addPart(new InHouse(2, "Wheel", 11.00, 16, 1, 20, 2));
        Inventory.addPart(new InHouse(3, "Seat", 15.00, 10, 1, 20, 3));

        partsTableView.setItems(Inventory.getAllParts());
        partsTableView.setPlaceholder(new Label("No Parts Available"));
    }

    private void initializeProductsTable() {
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        Inventory.addProduct(new Product(1000, "Giant Bike", 299.99, 5, 1, 5));
        Inventory.addProduct(new Product(1001, "Tricycle", 99.99, 3, 1, 5));

        productTableView.setItems(Inventory.getAllProducts());
        productTableView.setPlaceholder(new Label("No Products Available"));
    }

    @FXML
    private void exitButtonClicked() {
        System.exit(0);
    }

    @FXML
    private void addPartsClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = getLoader("part-detail-form.fxml");
            loader.setController(new PartDetailFormController());
            showStage(loader.load());
        } catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    private void modifyPartsClicked(ActionEvent actionEvent) {
        try {
            Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
            if (selectedPart == null) {
                showErrorText("Please select a part to modify.");
                return;
            }

            FXMLLoader loader = getLoader("part-detail-form.fxml");
            loader.setController(new PartDetailFormController(selectedPart));

            showStage(loader.load());
        }
        catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    private void deletePartsClicked(ActionEvent actionEvent) {
        ObservableList<Part> selectedParts = partsTableView.getSelectionModel().getSelectedItems();

        if (selectedParts.isEmpty()) {
            showErrorText("Please select a part to delete.");
            return;
        }

        StringBuilder builder = new StringBuilder("Are you sure you want to delete the following part(s)?")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        for (Part selectedPart : selectedParts) {
            builder.append(selectedPart.getName()).append(System.lineSeparator());
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, builder.toString(), ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response != ButtonType.YES) return;

            for (Part selectedPart : selectedParts) {
                Inventory.deletePart(selectedPart);
            }
        });
    }

    @FXML
    private void addProductClicked(ActionEvent actionEvent) {

    }

    @FXML
    private void modifyProductClicked(ActionEvent actionEvent) {

    }

    @FXML
    private void deleteProductClicked(ActionEvent actionEvent) {
        ObservableList<Product> selectedProducts = productTableView.getSelectionModel().getSelectedItems();

        if (selectedProducts.isEmpty()) {
            showErrorText("Please select a product to delete.");
            return;
        }

        StringBuilder builder = new StringBuilder("Are you sure you want to delete the following product(s)?")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        for (Product selectedProduct : selectedProducts) {
            builder.append(selectedProduct.getName()).append(System.lineSeparator());
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, builder.toString(), ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response != ButtonType.YES) return;

            for (Product selectedProduct : selectedProducts) {
                Inventory.deleteProduct(selectedProduct);
            }
        });
    }

    @FXML
    private void partSearchField_KeyTyped(KeyEvent keyEvent) {
        partSearchTimer.cancel();
        partSearchTimer = new Timer();

        partSearchTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                String partName = partSearchField.getText();
                var items = partName.length() == 0 ? Inventory.getAllParts() : Inventory.lookupPart(partName);

                partsTableView.setItems(items);
                partSearchTimer.cancel();
            }
        }, 500);
    }

    @FXML
    private void productSearchField_KeyTyped(KeyEvent keyEvent) {
        productSearchTimer.cancel();
        productSearchTimer = new Timer();

        productSearchTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                String productName = productSearchField.getText().trim();
                var items = productName.length() == 0 ? Inventory.getAllProducts() : Inventory.lookupProduct(productName);

                productTableView.setItems(items);
                productSearchTimer.cancel();
            }
        }, 500);
    }

    private void handleException(Exception e) {
        e.printStackTrace();

        Alert alert = new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage());
        alert.showAndWait();
    }

    private FXMLLoader getLoader(String resourceFile) {
        return new FXMLLoader(Objects.requireNonNull(InventorySystem.class.getResource(resourceFile)));
    }

    private void showStage(Parent parent) {
        Stage stage = new Stage();
        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.showAndWait();
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