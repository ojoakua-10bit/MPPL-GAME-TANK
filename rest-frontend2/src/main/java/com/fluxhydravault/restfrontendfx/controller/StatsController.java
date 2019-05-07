package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Stat;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class StatsController {
    private Stat stat;
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
        alert.setHeaderText("Delete Stat");
        alert.setContentText("Are you sure to delete this stat? \nThis action cannot be undone.");

        alert.showAndWait();

        System.out.println("Delete stat.");

        ButtonType result = alert.getResult();
        if (result == ButtonType.OK) {
            // TODO: deletion code here
            System.out.println("Yes, delete this stat");
        }
    }
}
