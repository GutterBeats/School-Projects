/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem;

import com.alesch.qkm2inventorysystem.models.Resources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class of the program. Used to initialize and run.
 */
public final class InventorySystem extends Application {

    /**
     * Method used by Application class to launch the FXML Stage to start the program.
     *
     * @param stage Initial stage to show.
     * @throws IOException Will throw this if the FXML file is not found when loading.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Resources.getMainFormFXML());
        Scene scene = new Scene(root);

        stage.getIcons().add(Resources.getInventoryImage());
        stage.setTitle("Inventory System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Entry point of the program.
     * @param args Arguments to pass to the program.
     */
    public static void main(String[] args) {
        launch(args);
    }
}