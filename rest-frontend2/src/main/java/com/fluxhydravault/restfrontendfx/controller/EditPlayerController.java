package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Player;
import com.fluxhydravault.restfrontendfx.service.PlayerService;
import javafx.fxml.FXML;
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
        creditField.setText(Integer.toString(player.getCredit_balance()));
        banToggle.setSelected(player.getBan_status());
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

        // TODO: show confirmation first

        if (password.isEmpty() || rePassword.isEmpty()) {
            service.editPlayer(null, credit, banStatus);
            return;
        }
        if (!password.equals(rePassword)) {
            throw new RuntimeException("Password is mismatch");
        }
        else {
            service.editPlayer(password, credit, banStatus);
        }

        player.setCredit_balance(credit);
        player.setBan_status(banStatus);
    }
}
