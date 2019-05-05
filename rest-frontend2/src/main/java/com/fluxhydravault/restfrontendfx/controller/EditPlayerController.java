package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.controlsfx.control.ToggleSwitch;

public class EditPlayerController {
    private Player player;
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

    @FXML
    private void initialize() {
    }
}
