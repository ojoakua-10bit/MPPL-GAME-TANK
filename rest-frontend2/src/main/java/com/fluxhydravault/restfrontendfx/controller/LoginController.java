package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.UnauthorizedException;
import com.fluxhydravault.restfrontendfx.service.LoginService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

import java.io.IOException;

public class LoginController {
    private Stage primaryStage;
    private BorderPane initialRoot;
    private GridPane initialPane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setInitialRoot(BorderPane initialRoot) {
        this.initialRoot = initialRoot;
    }

    public void setInitialPane(GridPane initialPane) {
        this.initialPane = initialPane;
    }

    @FXML
    private void doCancel() {
        initialRoot.setBottom(initialPane);
    }

    @FXML
    private void doLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty()) {
            showAlert("Input error", "Username field is empty.");
            return;
        }
        if (password.isEmpty()) {
            showAlert("Input error", "Password field is empty.");
            return;
        }

        try {
            LoginService.getInstance().login(username, password);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            BorderPane panel = loader.load();

            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            Scene scene = new Scene(panel, 960, 600);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);
            primaryStage.setScene(scene);
        } catch (UnauthorizedException e) {
            showAlert("Login error", e.getMessage());
            passwordField.setText("");
        } catch (RuntimeException e) {
            showAlert("Error", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Login");
    }

    private void showAlert(String headerMessage, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle("Error");
        alert.setHeaderText(headerMessage);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
