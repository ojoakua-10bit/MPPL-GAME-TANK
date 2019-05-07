package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Item;
import com.fluxhydravault.restfrontendfx.service.ItemService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
        System.out.println("New item.");
    }

    @FXML
    private void editButtonClicked() {
        System.out.println("Edit item.");
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
            System.out.println("Yes, delete this item");
        }
    }
}
