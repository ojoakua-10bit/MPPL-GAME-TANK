package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Item;
import com.fluxhydravault.restfrontendfx.service.ItemService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

import java.io.IOException;
import java.util.List;

public class ItemController {
    private Item selectedItem;
    private ItemService service;
    private Stage primaryStage;
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, String> idColumn;
    @FXML
    private TableColumn<Item, String> nameColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label modelLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void updateTable(List<Item> list) {
        if (list != null)
            itemTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    @FXML
    private void initialize() {
        service = ItemService.getInstance();
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getItemIdProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getItemNameProperty());

        setSelectedItem(null);

        itemTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, old, current) -> setSelectedItem(current));

        updateTable(service.getItemLists());
    }

    private void setSelectedItem(Item item) {
        selectedItem = item;
        editButton.disableProperty().setValue(item == null);
        deleteButton.disableProperty().setValue(item == null);

        if (item == null) {
            idLabel.setText("");
            categoryLabel.setText("");
            nameLabel.setText("");
            descriptionLabel.setText("");
            modelLabel.setText("");
        } else {
            idLabel.setText(item.getItem_id());
            categoryLabel.setText(item.getItem_category().toString());
            nameLabel.setText(item.getItem_name());
            descriptionLabel.setText(item.getDescription());
            modelLabel.setText(item.getModel_location());
        }
    }

    @FXML
    private void newButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditItem.fxml"));
            Parent root = loader.load();
            EditItemController controller = loader.getController();
            controller.setItem(null);

            Stage stage = new Stage();
            stage.setTitle("New Item");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setResizable(false);
            controller.setStage(stage);

            Scene scene = new Scene(root);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);
            stage.setScene(scene);
            System.out.println("New Item.");

            stage.showAndWait();

            updateTable(service.getItemLists());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditItem.fxml"));
            Parent root = loader.load();
            EditItemController controller = loader.getController();
            controller.setItem(selectedItem);

            Stage stage = new Stage();
            stage.setTitle("Edit Item");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setResizable(false);

            controller.setStage(stage);

            Scene scene = new Scene(root);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);
            stage.setScene(scene);
            System.out.println("Edit Item.");

            stage.showAndWait();

            updateTable(service.getItemLists());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Item");
        alert.setContentText("Are you sure to delete this item? \nThis action cannot be undone.");

        alert.showAndWait();

        System.out.println("Delete player.");

        ButtonType result = alert.getResult();
        if (result == ButtonType.OK) {
            // TODO: deletion code here
            service.deleteItem(selectedItem.getItem_id());
            updateTable(service.getItemLists());
            selectedItem = null;
            System.out.println("Yes, delete this item");
        }
    }
}
