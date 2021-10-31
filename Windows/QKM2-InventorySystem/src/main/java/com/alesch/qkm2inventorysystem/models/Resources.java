package com.alesch.qkm2inventorysystem.models;

import com.alesch.qkm2inventorysystem.InventorySystem;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.Objects;

/**
 * Static utility class used to access different resources without having to type in the path.
 */
public final class Resources {

    private Resources() { }

    /**
     * Gets the URL to the Main Form FXML file.
     * @return A URL pointing to the main-form.fxml file.
     */
    public static URL getMainFormFXML() {
        return Objects.requireNonNull(InventorySystem.class.getResource("main-form.fxml"));
    }

    /**
     * Gets the URL to the Part Detail Form FXML file.
     * @return A URL pointing to the part-detail-form.fxml file.
     */
    public static URL getPartDetailFormFXML() {
        return Objects.requireNonNull(InventorySystem.class.getResource("part-detail-form.fxml"));
    }

    /**
     * Gets the URL to the Product Detail Form FXML file.
     * @return A URL pointing to the product-detail-form.fxml file.
     */
    public static URL getProductDetailFormFXML() {
        return Objects.requireNonNull(InventorySystem.class.getResource("product-detail-form.fxml"));
    }

    /**
     * Gets an Inventory Image object from the image resource folder.
     * @return An Image object.
     */
    public static Image getInventoryImage() {
        return new Image(String.valueOf(InventorySystem.class.getResource("images/inventory.png")));
    }

    /**
     * Gets a Part Image object from the image resource folder.
     * @return An Image object.
     */
    public static Image getPartImage() {
        return new Image(String.valueOf(InventorySystem.class.getResource("images/parts.png")));
    }

    /**
     * Gets a Product Image object from the image resource folder.
     * @return An Image object.
     */
    public static Image getProductImage() {
        return new Image(String.valueOf(InventorySystem.class.getResource("images/products.png")));
    }
}
