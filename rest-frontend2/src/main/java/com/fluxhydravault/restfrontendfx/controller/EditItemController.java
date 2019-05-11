package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Item;
import com.fluxhydravault.restfrontendfx.model.ItemCategory;
import com.fluxhydravault.restfrontendfx.service.FileUploadService;
import com.fluxhydravault.restfrontendfx.service.ItemService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EditItemController {
    private Item item;
    private Stage stage;
    private File file;
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
        file = null;

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
    private void doSave() {
        ItemService service = ItemService.getInstance();
        FileUploadService uploadService = FileUploadService.getInstance();
        boolean newItem = false;

        if (item == null) {
            item = new Item();
            item.setModel_location("null");
            newItem = true;
        }
        item.setItem_category(ItemCategory.valueOf(categoryBox.getSelectionModel().getSelectedItem()));
        item.setItem_name(nameField.getText());
        item.setDescription(descField.getText());

        if (newItem) {
            service.newItem(item);
        } else {
            service.editItem(item);
        }

        if (file != null) {
            String location = uploadService.uploadAsset(file, item.getItem_id());
            item.setModel_location(location);
        }
    }

    @FXML
    private void doUpload() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Supported archive files (*.zip)", "*.zip"));

        System.out.println("Choosing archive.");

        file = chooser.showOpenDialog(stage);
    }


    @FXML
    private void editStats() {}
}
