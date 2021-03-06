package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
import com.fluxhydravault.restfrontendfx.model.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class MainMenuController {
    private Config config;
    private Admin admin;
    private Stage primaryStage;
    private Scene initialScene;
    @FXML
    private BorderPane rootPanel;
    @FXML
    private GridPane homePanel;
    @FXML
    private Label adminMenu;
    @FXML
    private Label welcomeMessage;
    @FXML
    private ImageView adminAvatar;


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setInitialScene(Scene initialScene) {
        this.initialScene = initialScene;
    }

    public void setAdminAvatarImage(File image) {
        try {
            adminAvatar.setImage(new Image(new FileInputStream(image)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
        if (admin != null) {
            adminMenu.setText(admin.getUsername());
            welcomeMessage.setText("Hi " + admin.getAdminName() + "! Welcome to War Tanks - Admin Portal");
        }
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        config = Config.getConfig();
        admin = config.getCurrentAdmin();
        adminMenu.setText(admin.getUsername());
        welcomeMessage.setText("Hi "+ admin.getAdminName() +"! Welcome to War Tanks - Admin Portal");
        try {
            System.out.println("Downloading avatar...");
            File imageLocation = new File(config.getConfigLocation() + Defaults.getImageLocation());
            FileUtils.copyURLToFile(new URL(config.getFileServerUri() + admin.getAvatar()),
                    imageLocation, 15000, 15000);
            adminAvatar.setImage(new Image(new FileInputStream(imageLocation)));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(primaryStage);
            alert.setTitle("Error");
            alert.setHeaderText("Connection Error");
            alert.setContentText("An error has occurred while connecting to server.");

            alert.showAndWait();
        }
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
    private void statsMenuClicked() {
        // show stat view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Stats.fxml"));
            AnchorPane panel = loader.load();
            ((StatsController) loader.getController()).setPrimaryStage(primaryStage);
            rootPanel.setCenter(panel);
            System.out.println("Stats");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void adminMenuClicked() {
        // show admin view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
            AnchorPane panel = loader.load();
            AdminController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setMainMenuController(this);
            rootPanel.setCenter(panel);
            System.out.println("Admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logoutMenuClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Logout");
        alert.setContentText("Are you sure to end your session?");

        alert.showAndWait();

        System.out.println("Logout");

        ButtonType result = alert.getResult();

        if (result == ButtonType.OK) {
            config.setUserToken(null);
            config.setCurrentAdmin(null);
            config.saveConfig();

            if (initialScene == null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Initial.fxml"));
                    Parent root = loader.load();

                    Scene scene = new Scene(root, 600, 400);
                    new JMetro(JMetro.Style.LIGHT).applyTheme(scene);

                    InitialController controller = loader.getController();
                    controller.setPrimaryStage(primaryStage);
                    controller.setInitialScene(scene);

                    primaryStage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                primaryStage.setScene(initialScene);
            }

            System.out.println("Yes, log me out");
        }
    }
}
