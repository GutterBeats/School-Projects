module com.alesch.qkm2inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.alesch.qkm2inventorysystem to javafx.fxml;
    exports com.alesch.qkm2inventorysystem;
    exports com.alesch.qkm2inventorysystem.controllers;
    exports com.alesch.qkm2inventorysystem.models;
    opens com.alesch.qkm2inventorysystem.controllers to javafx.fxml;
    opens com.alesch.qkm2inventorysystem.models to javafx.fxml;
}