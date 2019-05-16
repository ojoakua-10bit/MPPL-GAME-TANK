package com.fluxhydravault.restfrontendfx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Stat {
    private transient StringProperty statIdProperty;
    private transient StringProperty statNameProperty;
    private transient StringProperty statTypeProperty;
    private transient StringProperty statValueProperty;

    private Long statId;
    private StatType type;
    private String name;
    private Double value;

    public Stat() {
        statIdProperty = new SimpleStringProperty(null);
        statNameProperty = new SimpleStringProperty(null);
        statTypeProperty = new SimpleStringProperty(null);
        statValueProperty = new SimpleStringProperty(null);
    }

    public Long getStatId() {
        return statId;
    }

    public void setStatId(Long statId) {
        statIdProperty.setValue(statId.toString());
        this.statId = statId;
    }

    public StatType getType() {
        return type;
    }

    public void setType(StatType type) {
        statTypeProperty.setValue(type.name());
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

    public void setValue(Double value) {
        statValueProperty.setValue(value.toString());
        this.value = value;
    }

    public StringProperty getStatIdProperty() {
        statIdProperty.setValue(statId.toString());
        return statIdProperty;
    }

    public StringProperty getStatNameProperty() {
        statNameProperty.setValue(name);
        return statNameProperty;
    }

    public StringProperty getStatTypeProperty() {
        statNameProperty.setValue(type.name());
        return statNameProperty;
    }

    public StringProperty getStatValueProperty() {
        statNameProperty.setValue(value.toString());
        return statNameProperty;
    }
}

