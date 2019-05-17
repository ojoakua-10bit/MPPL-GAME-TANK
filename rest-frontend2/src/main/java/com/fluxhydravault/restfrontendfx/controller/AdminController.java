package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.ConnectionException;
import com.fluxhydravault.restfrontendfx.UnauthorizedException;
import com.fluxhydravault.restfrontendfx.UnexpectedResponse;
import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
import com.fluxhydravault.restfrontendfx.model.Admin;
import com.fluxhydravault.restfrontendfx.service.AdminService;
import com.fluxhydravault.restfrontendfx.service.FileUploadService;
import com.fluxhydravault.restfrontendfx.service.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminController {
    private Admin admin;
    private AdminService adminService;
    private FileUploadService uploadService;
    private Config config;
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
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField rePasswordField;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        config = Config.getConfig();
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
        oldPasswordField.setText("");
        passwordField.setText("");
        rePasswordField.setText("");
    }

    @FXML
    private void doUpdate() {
        String password = passwordField.getText();
        String rePassword = rePasswordField.getText();
        String username = usernameField.getText();
        String nickname = nicknameField.getText();

        LoginService loginService = LoginService.getInstance();

        if (password.isEmpty() || rePassword.isEmpty()) {
            Admin result = adminService.editAdmin(username, null, nickname);
            postUpdateProfile(result);
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
            try {
                String oldPassword = oldPasswordField.getText();
                loginService.login(admin.getUsername(), oldPassword);
                Admin result = adminService.editAdmin(username, password, nickname);
                postUpdateProfile(result);
            } catch (UnauthorizedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(primaryStage);
                alert.setTitle("Error");
                alert.setHeaderText("Input Error");
                alert.setContentText("Current Password is incorrect");

                alert.showAndWait();
                return;
            }
        }

        admin.setUsername(username);
        admin.setAdminName(nickname);
    }

    private void postUpdateProfile(Admin result) {
        if (result != null) {
            config.setCurrentAdmin(result);
            config.saveConfig();
            mainMenuController.setAdmin(result);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(primaryStage);
            alert.setTitle("Success");
            alert.setHeaderText("Profile Update Success.");
            alert.setContentText("Your profile updated successfully.");

            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(primaryStage);
            alert.setTitle("Error");
            alert.setHeaderText("Update Error");
            alert.setContentText("An error has occurred while updating your profile");

            alert.showAndWait();
        }
    }

    @FXML
    private void doUpload() {
        if (adminAvatar != null) {
            try {
                String destImageFile = config.getConfigLocation() + Defaults.getImageLocation();
                if (!adminAvatar.getPath().equals(destImageFile)) {
                    String result = uploadService.uploadAvatar(adminAvatar, admin.getAdminId(), true);
                    config.getCurrentAdmin().setAvatar(result);
                    config.saveConfig();
                    // TODO show success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(primaryStage);
                    alert.setTitle("Success");
                    alert.setHeaderText("Upload image success.");
                    alert.setContentText("Avatar uploaded successfully.");

                    alert.showAndWait();

                    mainMenuController.setAdminAvatarImage(adminAvatar);
                    FileUtils.copyFile(adminAvatar, new File(destImageFile));
                }
            } catch (RuntimeException e) {
                // TODO show error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(primaryStage);
                alert.setTitle("Error");
                alert.setHeaderText("Upload Error");
                alert.setContentText(e.getMessage());

                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
