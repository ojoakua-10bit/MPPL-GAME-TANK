package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Item;
import com.fluxhydravault.restfrontendfx.model.Stat;
import com.fluxhydravault.restfrontendfx.service.ItemService;
import com.fluxhydravault.restfrontendfx.service.StatService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public class EditItemStatsController {
    private ItemService itemService;
    private StatService statService;
    private Stat selectedStat;
    private Stat selectedItemStat;
    private Item currentItem;
    private Stage stage;
    @FXML
    private TableView<Stat> itemStatTable;
    @FXML
    private TableColumn<Stat, String> itemStatName;
    @FXML
    private TableColumn<Stat, String> itemStatType;
    @FXML
    private TableColumn<Stat, String> itemStatValue;
    @FXML
    private TableView<Stat> statTable;
    @FXML
    private TableColumn<Stat, String> statName;
    @FXML
    private TableColumn<Stat, String> statType;
    @FXML
    private TableColumn<Stat, String> statValue;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
        updateItemStatTable(itemService.getItemStats(currentItem.getItemId()));
    }

    private void updateStatTable(List<Stat> list) {
        if (list != null)
            statTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    private void updateItemStatTable(List<Stat> list) {
        if (list != null)
            itemStatTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    @FXML
    private void initialize() {
        itemService = ItemService.getInstance();
        statService = StatService.getInstance();

        setSelectedItemStat(null);
        setSelectedStat(null);

        statName.setCellValueFactory(cellData -> cellData.getValue().getStatNameProperty());
        statType.setCellValueFactory(cellData -> cellData.getValue().getStatTypeProperty());
        statValue.setCellValueFactory(cellData -> cellData.getValue().getStatValueProperty());

        statTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, old, current) -> setSelectedStat(current));

        updateStatTable(statService.getStatLists());

        itemStatName.setCellValueFactory(cellData -> cellData.getValue().getStatNameProperty());
        itemStatType.setCellValueFactory(cellData -> cellData.getValue().getStatTypeProperty());
        itemStatValue.setCellValueFactory(cellData -> cellData.getValue().getStatValueProperty());

        itemStatTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, old, current) -> setSelectedItemStat(current));
    }

    private void setSelectedStat(Stat selectedStat) {
        this.selectedStat = selectedStat;
        addButton.disableProperty().setValue(selectedStat == null);
    }

    private void setSelectedItemStat(Stat selectedItemStat) {
        this.selectedItemStat = selectedItemStat;
        deleteButton.disableProperty().setValue(selectedItemStat == null);
    }

    @FXML
    private void doDelete() {
        List<Stat> result = itemService.deleteItemStats(currentItem.getItemId(), selectedItemStat.getStatId());
        updateItemStatTable(result);
    }

    @FXML
    private void doAdd() {
        List<Stat> result = itemService.addItemStat(currentItem.getItemId(), selectedStat.getStatId());
        updateItemStatTable(result);
    }

    @FXML
    private void doCancel() {
        stage.close();
    }
}
