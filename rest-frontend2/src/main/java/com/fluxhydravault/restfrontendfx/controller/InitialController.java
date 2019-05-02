package com.fluxhydravault.restfrontendfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

import java.io.IOException;

public class InitialController {
    private Scene initialScene;
    private Stage primaryStage;
    @FXML
    private BorderPane rootPane;
    @FXML
    private GridPane initialPane;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setInitialScene(Scene initialScene) {
        this.initialScene = initialScene;
    }

    public void doLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            AnchorPane panel = loader.load();

            LoginController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setInitialRoot(rootPane);
            controller.setInitialPane(initialPane);

            rootPane.setBottom(panel);
            System.out.println("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Register.fxml"));
            AnchorPane panel = loader.load();

            RegisterController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setInitialScene(initialScene);

            Scene scene = new Scene(panel, 600, 400);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);
            primaryStage.setScene(scene);
            System.out.println("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
