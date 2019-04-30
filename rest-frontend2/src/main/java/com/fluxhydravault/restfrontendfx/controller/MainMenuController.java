package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.model.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class MainMenuController {
    private Admin admin;

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
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/view/Player.fxml"));
            rootPanel.setCenter(panel);
            System.out.println("Player");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void itemMenuClicked() {
        // show item view
        System.out.println("Item");
    }

    @FXML
    private void adminMenuClicked() {
        // show admin view
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/view/Admin.fxml"));
            rootPanel.setCenter(panel);
            System.out.println("Admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logoutMenuClicked() {
        // logout
        System.out.println("Logout");
    }
}
