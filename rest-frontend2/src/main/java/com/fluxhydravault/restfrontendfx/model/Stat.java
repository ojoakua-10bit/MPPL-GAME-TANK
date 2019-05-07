package com.fluxhydravault.restfrontendfx.model;

public class Stat {
    private long stat_id;
    private StatType type;
    private String name;
    private double value;

    public long getStat_id() {
        return stat_id;
    }

    public void setStat_id(long stat_id) {
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
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

