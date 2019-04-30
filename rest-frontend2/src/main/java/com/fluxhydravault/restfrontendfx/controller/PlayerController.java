package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Player;
import com.fluxhydravault.restfrontendfx.service.PlayerService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class PlayerController {
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

    private void updateTable(List<Player> list) {
        if (list != null)
            playerTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayerIdProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().getUsernameProperty());

        showPlayerDetails(null);

        playerTable.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, player, t1) -> showPlayerDetails(t1));

        updateTable(PlayerService.getInstance().getPlayerLists());
    }

    private void showPlayerDetails(Player player) {
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
            idLabel.setText(player.getPlayer_id());
            usernameLabel.setText(player.getUsername());
            nicknameLabel.setText(player.getPlayer_name());
            xpLabel.setText(Integer.toString(player.getXp()));
            rankLabel.setText(Integer.toString(player.getRank()));
            diamondLabel.setText(Integer.toString(player.getDiamond_count()));
            goldLabel.setText(Integer.toString(player.getGold_count()));
            creditLabel.setText(Integer.toString(player.getCredit_balance()));
            inventoryLabel.setText(Integer.toString(player.getInventory()));
            avatarLabel.setText(player.getAvatar());
            banLabel.setText(Boolean.toString(player.getBan_status()));
        }
    }

    @FXML
    private void newButtonClicked() {
        System.out.println("New player.");
    }

    @FXML
    private void editButtonClicked() {
        System.out.println("Edit player.");
    }

    @FXML
    private void deleteButtonClicked() {
        System.out.println("Delete player.");
    }
}
