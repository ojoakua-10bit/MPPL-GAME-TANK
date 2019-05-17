package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Player;
import com.fluxhydravault.restfrontendfx.service.PlayerService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;

public class EditPlayerController {
    private Player player;
    private Stage stage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField rePasswordField;
    @FXML
    private TextField creditField;
    @FXML
    private ToggleSwitch banToggle;

    public void setPlayer(Player player) {
        this.player = player;
        creditField.setText(Integer.toString(player.getCreditBalance()));
        banToggle.setSelected(player.getBanStatus());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void doCancel() {
        stage.close();
    }

    @FXML
    private void doSave() {
        PlayerService service = PlayerService.getInstance();
        String password = passwordField.getText();
        String rePassword = rePasswordField.getText();
        int credit = Integer.parseInt(creditField.getText());
        boolean banStatus = banToggle.isSelected();

        if (password.isEmpty() || rePassword.isEmpty()) {
            service.editPlayer(player.getPlayerId(), null, credit, banStatus);
            stage.close();
            return;
        }
        if (!password.equals(rePassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Error");
            alert.setHeaderText("Input Error");
            alert.setContentText("Password is mismatch");

            alert.showAndWait();
            return;
        }
        else {
            service.editPlayer(player.getPlayerId(), password, credit, banStatus);
        }

        player.setCreditBalance(credit);
        player.setBanStatus(banStatus);
        stage.close();
    }
}
