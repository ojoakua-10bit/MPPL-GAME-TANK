package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Stat;
import com.fluxhydravault.restfrontendfx.model.StatType;
import com.fluxhydravault.restfrontendfx.service.StatService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        if (stat != null) {
            typeBox.getSelectionModel().select(stat.getType().toString());
            nameField.setText(stat.getName());
            valueField.setText(stat.getValue().toString());
        }
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

        if (stat != null) {
            typeBox.getSelectionModel().select(stat.getType().toString());
            nameField.setText(stat.getName());
            valueField.setText(stat.getValue().toString());
        }
    }

    @FXML
    private void doCancel() {
        stage.close();
    }

    @FXML
    private void doSave() {
        StatService service = StatService.getInstance();
        boolean newStat = false;

        if (stat == null) {
            stat = new Stat();
            newStat = true;
        }
        stat.setName(nameField.getText());
        stat.setType(StatType.valueOf(typeBox.getSelectionModel().getSelectedItem()));
        try {
            stat.setValue(Double.parseDouble(valueField.getText()));
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid value");
            alert.setContentText("Value should be valid real number.");

            alert.showAndWait();
            return;
        }

        if (newStat) {
            service.newStat(stat);
        } else  {
            service.editStat(stat);
        }

        stage.close();
    }
}
