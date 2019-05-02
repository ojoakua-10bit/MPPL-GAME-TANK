package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.model.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    private Admin admin;
    private Stage primaryStage;
    @FXML
    private BorderPane rootPanel;
    @FXML
    private GridPane homePanel;
    @FXML
    private Label homeLabel;
    @FXML
    private Label playerMenu;
    @FXML
    private Label itemMenu;
    @FXML
    private Label adminMenu;
    @FXML
    private Label logoutMenu;
    @FXML
    private Label welcomeMessage;


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        admin = Config.getConfig().getCurrentAdmin();
        adminMenu.setText(admin.getUsername());
        welcomeMessage.setText("Hi "+ admin.getAdmin_name() +"! Welcome to War Tanks - Admin Portal");
    }

    @FXML
    private void homeMenuClicked() {
        rootPanel.setCenter(homePanel);
        System.out.println("Home");
    }

    @FXML
    private void playerMenuClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Player.fxml"));
            AnchorPane panel = loader.load();
            ((PlayerController) loader.getController()).setPrimaryStage(primaryStage);
            rootPanel.setCenter(panel);
            System.out.println("Player");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void itemMenuClicked() {
        // show item view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Item.fxml"));
            AnchorPane panel = loader.load();
            ((ItemController) loader.getController()).setPrimaryStage(primaryStage);
            rootPanel.setCenter(panel);
            System.out.println("Player");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Item");
    }

    @FXML
    private void adminMenuClicked() {
        // show admin view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
            AnchorPane panel = loader.load();
            ((AdminController) loader.getController()).setPrimaryStage(primaryStage);
            rootPanel.setCenter(panel);
            System.out.println("Admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logoutMenuClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Logout");
        alert.setContentText("Are you sure to end your session?");

        alert.showAndWait();

        System.out.println("Logout");

        ButtonType result = alert.getResult();
        if (result == ButtonType.OK) {
            // TODO: logout code here
            System.out.println("Yes, log me out");
        }
    }
}
