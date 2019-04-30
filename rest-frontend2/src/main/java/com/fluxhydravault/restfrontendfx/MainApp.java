package com.fluxhydravault.restfrontendfx;

import com.fluxhydravault.restfrontendfx.service.LoginService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

import java.io.File;

public class MainApp extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoginService.getInstance().login("root", "akmalzonk");

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Scene scene = new Scene(root, 960, 600);
        new JMetro(JMetro.Style.DARK).applyTheme(scene);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("War Tanks: Admin Portal");
        this.primaryStage.setScene(scene);
        this.primaryStage.setResizable(false);
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
        this.primaryStage.show();
    }
}
