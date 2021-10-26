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

public final class MainFormController {

    //<editor-fold desc="FXML Control Declarations">

    @FXML
    private TextField partSearchField;

    @FXML
    private TextField productSearchField;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn partId;

    @FXML
    private TableColumn partName;

    @FXML
    private TableColumn partInventory;

    @FXML
    private TableColumn partPrice;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn productId;

    @FXML
    private TableColumn productName;

    @FXML
    private TableColumn productInventory;

    @FXML
    private TableColumn productPrice;

    @FXML
    private Label errorLabel;

    //</editor-fold>

    //<editor-fold desc="FXML Initialize">

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

        partsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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

        productTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        productTableView.setItems(Inventory.getAllProducts());
        productTableView.setPlaceholder(new Label("No Products Available"));
    }

    //</editor-fold>

    //<editor-fold desc="FXML Event Handlers">

    @FXML
    private void exitButtonClicked() {
        System.exit(0);
    }

    @FXML
    private void addPartsClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(Resources.getPartDetailFormFXML());
            loader.setController(new PartDetailFormController());

            showStage(loader.load(), Resources.getPartImage(), "Add New Part");
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

            FXMLLoader loader = new FXMLLoader(Resources.getPartDetailFormFXML());
            loader.setController(new PartDetailFormController(selectedPart.getId()));

            showStage(loader.load(), Resources.getPartImage(), "Update " + selectedPart.getName());
        }
        catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    private void deletePartsClicked(ActionEvent actionEvent) {
        var selectedPart = partsTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            showErrorText("Please select a part to delete.");
            return;
        }

        String builder = "Are you sure you want to delete the following part?" +
                System.lineSeparator() +
                System.lineSeparator() +
                selectedPart.getName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, builder, ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response != ButtonType.YES) return;

            Inventory.deletePart(selectedPart);
        });
    }

    @FXML
    private void addProductClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(Resources.getProductDetailFormFXML());
            loader.setController(new ProductDetailFormController());

            showStage(loader.load(), Resources.getProductImage(), "Add Product");
        }
        catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    private void modifyProductClicked(ActionEvent actionEvent) {
        try {
            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

            if (selectedProduct == null) {
                showErrorText("Please select a product to modify.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(Resources.getProductDetailFormFXML());
            loader.setController(new ProductDetailFormController(selectedProduct.getId()));

            showStage(loader.load(), Resources.getProductImage(), "Update " + selectedProduct.getName());
        }
        catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    private void deleteProductClicked(ActionEvent actionEvent) {
        var selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showErrorText("Please select a product to delete.");
            return;
        }

        String confirmationMessage = "Are you sure you want to delete the following product?" +
                System.lineSeparator() + System.lineSeparator() + selectedProduct.getName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, confirmationMessage, ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response != ButtonType.YES) return;

            Inventory.deleteProduct(selectedProduct);
        });
    }

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

    private void handleException(Exception e) {
        e.printStackTrace();

        Alert alert = new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage());
        alert.showAndWait();
    }

    private void showStage(Parent parent, Image stageImage, String title) {
        Stage stage = new Stage();
        Scene scene = new Scene(parent);

        stage.getIcons().add(stageImage);
        stage.setTitle(title);
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

    //</editor-fold>
}