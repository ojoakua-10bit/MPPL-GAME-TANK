package com.fluxhydravault.restbackend.controller;

import com.fluxhydravault.restbackend.InputFormatException;
import com.fluxhydravault.restbackend.services.StatService;
import com.fluxhydravault.restbackend.services.TokenService;
import com.fluxhydravault.restbackend.model.Stat;
import com.fluxhydravault.restbackend.model.StatType;
import com.fluxhydravault.restbackend.utils.HeaderChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/stats")
public class StatsController {
    private TokenService tokenService;
    private StatService statService;

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setStatService(StatService statService) {
        this.statService = statService;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> newStat(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam("stat_type") String statType,
            @RequestParam("stat_name") String statName,
            @RequestParam("stat_value") double statValue
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        StatType type;
        try {
            type = StatType.valueOf(statType);
        } catch (IllegalArgumentException e) {
            throw new InputFormatException();
        }

        Stat result = statService.newStat(type, statName, statValue);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "New stat successfully added.");
        map.put("data", result);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Stat> getStats(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(name = "q", required = false) String statName,
            @RequestParam(name = "type", required = false) String statType
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (statName != null) {
            return statService.searchStatByName(statName);
        }
        else if (statType != null) {
            return statService.getStatsByType(StatType.valueOf(statType));
        }
        else {
            return statService.getAllStats();
        }
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/{id}", method = { RequestMethod.PATCH, RequestMethod.PUT })
    public Map<String, Object> updateStat(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") long statID,
            @RequestParam(name = "stat_type", required = false) String statType,
            @RequestParam(name = "stat_name", required = false) String statName,
            @RequestParam(name = "stat_value", required = false) Double statValue
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (statType != null) {
            StatType type;
            try {
                type = StatType.valueOf(statType);
            } catch (IllegalArgumentException e) {
                throw new InputFormatException();
            }
            statService.changeStatType(statID, type);
        }
        if (statName != null) {
            statService.changeStatName(statID, statName);
        }
        if (statValue != null) {
            statService.changeStatValue(statID, statValue);
        }

        Stat result = statService.getStatById(statID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Stat successfully changed.");
        map.put("data", result);
        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "", method = { RequestMethod.PATCH, RequestMethod.PUT })
    public Map<String, Object> updateStatByName(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(name = "stat_name") String statName,
            @RequestParam(name = "stat_new_name", required = false) String newStatName,
            @RequestParam(name = "stat_type", required = false) String statType,
            @RequestParam(name = "stat_value", required = false) Double statValue
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (statType != null) {
            StatType type;
            try {
                type = StatType.valueOf(statType);
            } catch (IllegalArgumentException e) {
                throw new InputFormatException();
            }
            statService.changeStatType(statName, type);
        }
        if (newStatName != null) {
            statService.changeStatName(statName, statName);
        }
        if (statValue != null) {
            statService.changeStatValue(statName, statValue);
        }

        Stat result = statService.getStatByName(statName);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Stat successfully changed.");
        map.put("data", result);
        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void deleteStat(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(name = "stat_id", required = false) Long statID,
            @RequestParam(name = "stat_name", required = false) String statName
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (statID != null && statName != null) {
            throw new InputFormatException();
        }
        else if (statID != null) {
            statService.deleteStat(statID);
        }
        else if (statName != null) {
            statService.deleteStat(statName);
        }
        else {
            throw new InputFormatException();
        }
    }
}
