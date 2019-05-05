package com.fluxhydravault.restfrontendfx;

import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.controller.InitialController;
import com.fluxhydravault.restfrontendfx.controller.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Config config = Config.getConfig();
        config.loadConfig();
        Scene scene;

        if (config.getUserToken() == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Initial.fxml"));
            Parent root = loader.load();

            scene = new Scene(root, 600, 400);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);

            InitialController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setInitialScene(scene);
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();

            scene = new Scene(root, 960, 600);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);

            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setInitialScene(null);
        }

        primaryStage.setTitle("War Tanks: Admin Portal");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
        primaryStage.show();
    }
}
