package com.fluxhydravault.restfrontendfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ItemController {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void newButtonClicked() {
        System.out.println("New item.");
    }

    @FXML
    private void editButtonClicked() {
        System.out.println("Edit item.");
    }

    @FXML
    private void deleteButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Item");
        alert.setContentText("Are you sure to delete this item? \nThis action cannot be undone.");

        alert.showAndWait();

        System.out.println("Delete player.");

        ButtonType result = alert.getResult();
        if (result == ButtonType.OK) {
            // TODO: deletion code here
            System.out.println("Yes, delete this player");
        }
    }
}
