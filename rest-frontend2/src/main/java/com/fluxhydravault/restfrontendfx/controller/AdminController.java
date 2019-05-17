package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.ConnectionException;
import com.fluxhydravault.restfrontendfx.UnexpectedResponse;
import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
import com.fluxhydravault.restfrontendfx.model.Admin;
import com.fluxhydravault.restfrontendfx.service.AdminService;
import com.fluxhydravault.restfrontendfx.service.FileUploadService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminController {
    private Admin admin;
    private AdminService adminService;
    private FileUploadService uploadService;
    private File adminAvatar;
    private Stage primaryStage;
    private MainMenuController mainMenuController;
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
        Config config = Config.getConfig();
        admin = config.getCurrentAdmin();
        adminService = AdminService.getInstance();
        uploadService = FileUploadService.getInstance();
        usernameField.setText(admin.getUsername());
        nicknameField.setText(admin.getAdminName());
        adminAvatar = new File(config.getConfigLocation() + Defaults.getImageLocation());
        try {
            avatarPreview.setImage(new Image(new FileInputStream(adminAvatar)));
        } catch (FileNotFoundException e) {
            adminAvatar = null;
            avatarPreview.setImage(new Image(getClass().getResource("/img/image.png").getPath()));
        }
    }

    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void chooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Supported image files (*.jpg, *.jpeg, *.png)",
                        "*.jpg", "*.jpeg", "*.png"));

        System.out.println("Choosing image.");

        adminAvatar = chooser.showOpenDialog(primaryStage);
        if (adminAvatar != null) {
            try {
                avatarPreview.setImage(new Image(new FileInputStream(adminAvatar)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void doDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete account");
        alert.setContentText("Are you sure to delete your account? \nThis action cannot be undone.");

        alert.showAndWait();

        System.out.println("Delete user.");

        ButtonType result = alert.getResult();
        if (result == ButtonType.OK) {
            adminService.deleteAdmin();
            mainMenuController.logoutMenuClicked();
        }
    }

    @FXML
    private void doReset() {
        usernameField.setText(admin.getUsername());
        nicknameField.setText(admin.getAdminName());
        passwordField.setText("");
        rePasswordField.setText("");
    }

    @FXML
    private void doUpdate() {
        String password = passwordField.getText();
        String rePassword = rePasswordField.getText();
        String username = usernameField.getText();
        String nickname = nicknameField.getText();

        if (password.isEmpty() || rePassword.isEmpty()) {
            adminService.editAdmin(username, null, nickname);
            return;
        }
        if (!password.equals(rePassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(primaryStage);
            alert.setTitle("Error");
            alert.setHeaderText("Input Error");
            alert.setContentText("Password is mismatch");

            alert.showAndWait();
            return;
        }
        else {
            adminService.editAdmin(username, password, nickname);
        }

        admin.setUsername(username);
        admin.setAdminName(nickname);
    }

    @FXML
    private void doUpload() {
        if (adminAvatar != null) {
            try {
                uploadService.uploadAvatar(adminAvatar, admin.getAdminId(), true);
                // TODO show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(primaryStage);
                alert.setTitle("Success");
                alert.setHeaderText("Upload image success.");
                alert.setContentText("Avatar uploaded successfully.");

                alert.showAndWait();

                mainMenuController.setAdminAvatarImage(adminAvatar);
            } catch (UnexpectedResponse | ConnectionException e) {
                // TODO show error message
                e.printStackTrace();
            }
        }
    }
}
