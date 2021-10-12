package com.alesch.qkm2inventorysystem.models;

import com.alesch.qkm2inventorysystem.InventorySystem;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.Objects;

public final class Resources {

    private Resources() { }

    public static URL getMainFormFXML() {
        return Objects.requireNonNull(InventorySystem.class.getResource("main-form.fxml"));
    }

    public static URL getPartDetailFormFXML() {
        return Objects.requireNonNull(InventorySystem.class.getResource("part-detail-form.fxml"));
    }

    public static Image getInventoryImage() {
        return new Image(String.valueOf(InventorySystem.class.getResource("images/inventory.png")));
    }

    public static Image getPartImage() {
        return new Image(String.valueOf(InventorySystem.class.getResource("images/parts.png")));
    }

    public static Image getProductImage() {
        return new Image(String.valueOf(InventorySystem.class.getResource("images/products.png")));
    }
}
