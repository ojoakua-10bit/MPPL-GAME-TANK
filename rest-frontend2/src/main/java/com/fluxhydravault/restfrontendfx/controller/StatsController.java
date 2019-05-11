package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Stat;
import com.fluxhydravault.restfrontendfx.service.StatService;
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

public class StatsController {
    private Stat selectedStat;
    private StatService service;
    private Stage primaryStage;
    @FXML
    private TableView<Stat> statTable;
    @FXML
    private TableColumn<Stat, String> idColumn;
    @FXML
    private TableColumn<Stat, String> nameColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label valueLabel;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void updateTable(List<Stat> list) {
        if (list != null)
            statTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    @FXML
    private void initialize() {
        service = StatService.getInstance();
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getStatIdProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getStatNameProperty());

        setSelectedStat(null);

        statTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, old, current) -> setSelectedStat(current));

        updateTable(service.getStatLists());
    }

    private void setSelectedStat(Stat stat) {
        selectedStat = stat;
        if (stat == null) {
            idLabel.setText("");
            nameLabel.setText("");
            typeLabel.setText("");
            valueLabel.setText("");
        } else {
            idLabel.setText(selectedStat.getStat_id().toString());
            nameLabel.setText(selectedStat.getName());
            typeLabel.setText(selectedStat.getType().toString());
            valueLabel.setText(selectedStat.getValue().toString());
        }
    }

    @FXML
    private void newButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditStat.fxml"));
            Parent root = loader.load();
            EditStatController controller = loader.getController();
            controller.setStat(null);

            Stage stage = new Stage();
            stage.setTitle("New Stat");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setResizable(false);
            controller.setStage(stage);

            Scene scene = new Scene(root);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);
            stage.setScene(scene);
            System.out.println("New Stat.");

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditItem.fxml"));
            Parent root = loader.load();
            EditStatController controller = loader.getController();
            controller.setStat(selectedStat);

            Stage stage = new Stage();
            stage.setTitle("Edit Stat");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setResizable(false);

            if (selectedStat == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(primaryStage);
                alert.setTitle("Error");
                alert.setHeaderText("No selection");
                alert.setContentText("Please select one stat first.");

                alert.showAndWait();
                return;
            }
            controller.setStage(stage);

            Scene scene = new Scene(root);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);
            stage.setScene(scene);
            System.out.println("Edit Stat.");

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Stat");
        alert.setContentText("Are you sure to delete this stat? \nThis action cannot be undone.");

        alert.showAndWait();

        System.out.println("Delete stat.");

        ButtonType result = alert.getResult();
        if (result == ButtonType.OK) {
            // TODO: deletion code here
            System.out.println("Yes, delete this stat");
        }
    }
}
