/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.controllers;

import com.alesch.qkm2inventorysystem.models.InHouse;
import com.alesch.qkm2inventorysystem.models.Inventory;
import com.alesch.qkm2inventorysystem.models.Part;
import com.alesch.qkm2inventorysystem.models.Product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

public final class MainFormController {

    private Timer searchTimer = new Timer();

    @FXML
    public TextField partSearchField;

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

        productTableView.setItems(Inventory.getAllProducts());
        productTableView.setPlaceholder(new Label("No Products Available"));
    }

    @FXML
    private void exitButtonClicked() {
        System.exit(0);
    }

    @FXML
    private void addPartsClicked(ActionEvent actionEvent) {

    }

    @FXML
    private void modifyPartsClicked(ActionEvent actionEvent) {

    }

    @FXML
    private void deletePartsClicked(ActionEvent actionEvent) {

    }

    public void partSearchField_KeyTyped(KeyEvent keyEvent) {
        searchTimer.cancel();
        searchTimer = new Timer();

        searchTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                String partName = partSearchField.getText();
                System.out.println("Searching for..." + partName);

                var items = partName.length() == 0 ? Inventory.getAllParts() : Inventory.lookupPart(partName);

                partsTableView.setItems(items);
                searchTimer.cancel();
            }
        }, 500);
    }
}