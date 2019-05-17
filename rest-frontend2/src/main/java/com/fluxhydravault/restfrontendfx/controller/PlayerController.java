package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Player;
import com.fluxhydravault.restfrontendfx.service.PlayerService;
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

public class PlayerController {
    private Player selectedPlayer;
    private PlayerService service;
    private Stage primaryStage;
    @FXML
    private TableView<Player> playerTable;
    @FXML
    private TableColumn<Player, String> idColumn;
    @FXML
    private TableColumn<Player, String> usernameColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label xpLabel;
    @FXML
    private Label rankLabel;
    @FXML
    private Label diamondLabel;
    @FXML
    private Label goldLabel;
    @FXML
    private Label creditLabel;
    @FXML
    private Label inventoryLabel;
    @FXML
    private Label avatarLabel;
    @FXML
    private Label banLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void updateTable(List<Player> list) {
        if (list != null)
            playerTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    @FXML
    private void initialize() {
        service = PlayerService.getInstance();
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayerIdProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().getUsernameProperty());

        setSelectedPlayer(null);

        playerTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, old, current) -> setSelectedPlayer(current));

        updateTable(service.getPlayerLists());
    }

    private void setSelectedPlayer(Player player) {
        selectedPlayer = player;
        editButton.disableProperty().setValue(player == null);
        deleteButton.disableProperty().setValue(player == null);

        if (player == null) {
            idLabel.setText("");
            usernameLabel.setText("");
            nicknameLabel.setText("");
            xpLabel.setText("");
            rankLabel.setText("");
            diamondLabel.setText("");
            goldLabel.setText("");
            creditLabel.setText("");
            inventoryLabel.setText("");
            avatarLabel.setText("");
            banLabel.setText("");
        }
        else {
            idLabel.setText(player.getPlayerId());
            usernameLabel.setText(player.getUsername());
            nicknameLabel.setText(player.getPlayerName());
            xpLabel.setText(Integer.toString(player.getXp()));
            rankLabel.setText(Integer.toString(player.getRank()));
            diamondLabel.setText(Integer.toString(player.getDiamondCount()));
            goldLabel.setText(Integer.toString(player.getGoldCount()));
            creditLabel.setText(Integer.toString(player.getCreditBalance()));
            inventoryLabel.setText(Integer.toString(player.getInventory()));
            avatarLabel.setText(player.getAvatar());
            banLabel.setText(Boolean.toString(player.getBanStatus()));
        }
    }

    @FXML
    private void editButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditPlayer.fxml"));
            Parent root = loader.load();
            EditPlayerController controller = loader.getController();

            controller.setPlayer(selectedPlayer);

            Stage stage = new Stage();
            stage.setTitle("Edit Player");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setResizable(false);
            controller.setStage(stage);

            Scene scene = new Scene(root);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);
            stage.setScene(scene);
            System.out.println("Edit player.");

            stage.showAndWait();
            updateTable(service.getPlayerLists());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Player");
        alert.setContentText("Are you sure to delete this player? \nThis action cannot be undone.");

        alert.showAndWait();

        System.out.println("Delete player.");

        ButtonType result = alert.getResult();
        if (result == ButtonType.OK) {
            // TODO: deletion code here
            service.deletePlayer(selectedPlayer.getPlayerId());
            updateTable(service.getPlayerLists());
            selectedPlayer = null;
            System.out.println("Yes, delete this player");
        }
    }
}
