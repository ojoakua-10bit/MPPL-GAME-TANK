package com.fluxhydravault.restfrontendfx.controller;

import com.fluxhydravault.restfrontendfx.model.Stat;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.util.List;

public class EditItemStatsController {
    private List<Stat> stats;
    private List<Stat> itemStats;
    private Stage stage;

    public void setStats(List<Stat> stats) {
        this.stats = stats;
    }

    @FXML
    private void initialize() {}

    @FXML
    private void doCancel() {
        stage.close();
    }

    @FXML
    private void doSave() {}
}
