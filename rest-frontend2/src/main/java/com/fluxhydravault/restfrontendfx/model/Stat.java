package com.fluxhydravault.restfrontendfx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Stat {
    private transient StringProperty statIdProperty;
    private transient StringProperty statNameProperty;

    private Long stat_id;
    private StatType type;
    private String name;
    private Double value;

    public Stat() {
        statIdProperty = new SimpleStringProperty(null);
        statNameProperty = new SimpleStringProperty(null);
    }

    public Long getStat_id() {
        return stat_id;
    }

    public void setStat_id(Long stat_id) {
        statIdProperty.setValue(stat_id.toString());
        this.stat_id = stat_id;
    }

    public StatType getType() {
        return type;
    }

    public void setType(StatType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        statNameProperty.setValue(name);
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public StringProperty getStatIdProperty() {
        statIdProperty.setValue(stat_id.toString());
        return statIdProperty;
    }

    public StringProperty getStatNameProperty() {
        statNameProperty.setValue(name);
        return statNameProperty;
    }
}

