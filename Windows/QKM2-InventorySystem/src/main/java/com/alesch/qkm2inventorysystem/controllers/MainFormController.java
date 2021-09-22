/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.InventorySystem;
import com.alesch.qkm2inventorysystem.models.InHouse;
import com.alesch.qkm2inventorysystem.models.Inventory;
import com.alesch.qkm2inventorysystem.models.Part;
import com.alesch.qkm2inventorysystem.models.Product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

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
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(InventorySystem.class.getResource("part-detail-form.fxml")));

            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void modifyPartsClicked(ActionEvent actionEvent) {

    }

    @FXML
    private void deletePartsClicked(ActionEvent actionEvent) {

    }

    @FXML
    private void addProductClicked(ActionEvent actionEvent) {

    }

    @FXML
    private void modifyProductClicked(ActionEvent actionEvent) {

    }

    @FXML
    private void deleteProductClicked(ActionEvent actionEvent) {

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
                String productName = productSearchField.getText();
                var items = productName.length() == 0 ? Inventory.getAllProducts() : Inventory.lookupProduct(productName);

                productTableView.setItems(items);
                productSearchTimer.cancel();
            }
        }, 500);
    }
}