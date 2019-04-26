package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.model.Stat;
import com.fluxhydravault.restbackend.model.StatType;

import java.util.List;

public interface StatService {
    Stat newStat(StatType type, String name, double value);

    Stat getStatById(long id);

    Stat getStatByName(String name);

    List<Stat> getAllStats();

    List<Stat> searchStatByName(String name);

    List<Stat> getStatsByType(StatType type);

    void changeStatName(long statID, String name);

    void changeStatName(String oldName, String newName);

    void changeStatType(long statID, StatType type);

    void changeStatType(String name, StatType type);

    void changeStatValue(long statID, double value);

    void changeStatValue(String name, double value);

    void deleteStat(long statID);

    void deleteStat(String name);

    List<Stat> getItemStats(String itemID);

    void addStatToItem(String itemID, long statID);

    void addStatToItem(String itemID, String statName);

    void deleteStatFromItem(String itemID, long statID);

    void deleteStatFromItem(String itemID, String statName);
}
