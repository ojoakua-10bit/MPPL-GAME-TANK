package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Stat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditStatController {
    private Stat stat;
    private Stage stage;
    @FXML
    private ComboBox<String> typeBox;
    @FXML
    private TextField nameField;
    @FXML
    private TextField valueField;

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        ObservableList<String> optionList = FXCollections.observableArrayList();
        optionList.add("HITPOINT");
        optionList.add("ATTACK");
        optionList.add("DEFENSE");
        optionList.add("SPEED");
        optionList.add("RELOAD_SPEED");
        optionList.add("HITPOINT_BOOST");
        optionList.add("ATTACK_BOOST");
        optionList.add("DEFENSE_BOOST");
        optionList.add("SPEED_BOOST");
        optionList.add("RELOAD_SPEED_BOOST");
        optionList.add("INV_CAPACITY_BONUS");
        typeBox.setItems(optionList);
    }

    @FXML
    private void doCancel() {
        stage.close();
    }

    @FXML
    private void doSave() {}
}
