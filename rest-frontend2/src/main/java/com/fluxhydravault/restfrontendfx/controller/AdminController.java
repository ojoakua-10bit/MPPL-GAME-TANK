package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.model.Admin;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AdminController {
    private Admin admin;
    private Stage primaryStage;
    @FXML
    private ImageView avatarPreview;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField rePasswordField;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        admin = Config.getConfig().getCurrentAdmin();
        usernameField.setText(admin.getUsername());
        nicknameField.setText(admin.getAdmin_name());
    }

    @FXML
    private void chooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Supported image files (*.jpg, *.jpeg, *.png)",
                        "*.jpg", "*.jpeg", "*.png"));

        System.out.println("Choosing image.");

        File file = chooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                avatarPreview.setImage(new Image(new FileInputStream(file)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
