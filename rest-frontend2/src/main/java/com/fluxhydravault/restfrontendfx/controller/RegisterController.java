package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Admin;
import com.fluxhydravault.restfrontendfx.service.AdminService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
    private Stage primaryStage;
    private Scene initialScene;
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

    public void setInitialScene(Scene initialScene) {
        this.initialScene = initialScene;
    }

    @FXML
    private void doRegister() {
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String password = passwordField.getText();
        String rePassword = rePasswordField.getText();

        if (username.isEmpty()) {
            showErrorAlert("Input error", "Username field is empty.");
        }
        else if (nickname.isEmpty()) {
            showErrorAlert("Input error", "Nickname field is empty.");
        }
        else if (password.isEmpty()) {
            showErrorAlert("Input error", "Password field is empty.");
        }
        else if (rePassword.isEmpty()) {
            showErrorAlert("Input error", "Confirm Password field is empty.");
        }
        else if (!password.equals(rePassword)) {
            showErrorAlert("Input error", "Password is mismatch field is empty.");
        }

        AdminService service = AdminService.getInstance();
        Admin result = service.registerAdmin(username, password, nickname);
        if (result == null) {
            showErrorAlert("Register Error", "An error has occurred while registering your account");
        }
        else {
            showSuccessAlert("Registration success", "Now you can login using your new account");
            primaryStage.setScene(initialScene);
        }
    }

    @FXML
    private void doCancel() {
        primaryStage.setScene(initialScene);
    }

    private void showErrorAlert(String headerMessage, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle("Error");
        alert.setHeaderText(headerMessage);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showSuccessAlert(String headerMessage, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Success");
        alert.setHeaderText(headerMessage);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
