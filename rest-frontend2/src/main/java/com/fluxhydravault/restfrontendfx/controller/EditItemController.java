package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Item;
import com.fluxhydravault.restfrontendfx.model.ItemCategory;
import com.fluxhydravault.restfrontendfx.service.FileUploadService;
import com.fluxhydravault.restfrontendfx.service.ItemService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

import java.io.File;
import java.io.IOException;

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
        if (item != null) {
            nameField.setText(item.getItem_name());
            categoryBox.getSelectionModel().select(item.getItem_category().toString());
            descField.setText(item.getDescription());
        }
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

        if (item != null) {
            nameField.setText(item.getItem_name());
            categoryBox.getSelectionModel().select(item.getItem_category().toString());
            descField.setText(item.getDescription());
        }
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

        if (nameField.getText().isEmpty()) {
            showInputErrorMessage("Item name is empty.");
            return;
        }
        if (descField.getText().isEmpty()) {
            showInputErrorMessage("Item description is empty.");
            return;
        }

        if (item == null) {
            item = new Item();
            item.setModel_location("null");
            newItem = true;
        }
        item.setItem_category(ItemCategory.valueOf(categoryBox.getSelectionModel().getSelectedItem()));
        item.setItem_name(nameField.getText());
        item.setDescription(descField.getText());

        Item result;
        if (newItem) {
            result = service.newItem(item);
        } else {
            result = service.editItem(item);
        }

        if (file != null) {
            String location = uploadService.uploadAsset(file, result.getItem_id());
            item.setModel_location(location);
        }

        stage.close();
    }

    @FXML
    private void doUpload() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Supported archive files (*.zip)", "*.zip"));

        System.out.println("Choosing archive.");

        file = chooser.showOpenDialog(stage);

        if (file != null)
            uploadButton.setText(file.getName());
    }


    @FXML
    private void editStats() {
        if (item == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditItemStats.fxml"));
            Parent root = loader.load();
            EditItemStatsController controller = loader.getController();
            controller.setCurrentItem(item);

            Stage stage = new Stage();
            stage.setTitle("Edit Item Stats");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setResizable(false);
            controller.setStage(stage);

            Scene scene = new Scene(root);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);
            stage.setScene(scene);
            System.out.println("Edit item stats.");

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInputErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(stage);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid input");
        alert.setContentText(message);

        alert.showAndWait();
    }
}
