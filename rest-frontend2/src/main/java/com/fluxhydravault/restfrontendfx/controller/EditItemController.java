package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditItemController {
    private Item item;
    private Stage stage;
    @FXML
    private ComboBox<String> categoryBox;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descField;
    @FXML
    private Button uploadButton;

    public void setItem(Item item) {
        this.item = item;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        ObservableList<String> optionList = FXCollections.observableArrayList();
        optionList.add("TANK");
        optionList.add("SKIN");
        optionList.add("GAMEPLAY_DROPPED_ITEM");
        optionList.add("INVENTORY_CAPACITY");
        categoryBox.setItems(optionList);
    }

    @FXML
    private void doCancel() {
        stage.close();
    }

    @FXML
    private void doSave() {}

    @FXML
    private void doUpload() {}
}
