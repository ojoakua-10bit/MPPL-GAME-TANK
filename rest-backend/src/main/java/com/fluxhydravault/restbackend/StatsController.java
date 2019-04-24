package com.fluxhydravault.restbackend;

import com.fluxhydravault.restbackend.dao.StatDAO;
import com.fluxhydravault.restbackend.dao.TokenDAO;
import com.fluxhydravault.restbackend.model.Stat;
import com.fluxhydravault.restbackend.model.StatType;
import com.fluxhydravault.restbackend.utils.HeaderChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatsController {
    private TokenDAO tokenDAO;
    private StatDAO statDAO;

    @Autowired
    public void setTokenDAO(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Autowired
    public void setStatDAO(StatDAO statDAO) {
        this.statDAO = statDAO;
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        StatType type;
        try {
            type = StatType.valueOf(statType);
        } catch (IllegalArgumentException e) {
            throw new InputFormatException();
        }

        Stat result = statDAO.newStat(type, statName, statValue);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("data", result);
        return map;
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        if (statType != null) {
            StatType type;
            try {
                type = StatType.valueOf(statType);
            } catch (IllegalArgumentException e) {
                throw new InputFormatException();
            }
            statDAO.changeStatType(statID, type);
        }
        if (statName != null) {
            statDAO.changeStatName(statID, statName);
        }
        if (statValue != null) {
            statDAO.changeStatValue(statID, statValue);
        }

        Stat result = statDAO.getStatById(statID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        if (statType != null) {
            StatType type;
            try {
                type = StatType.valueOf(statType);
            } catch (IllegalArgumentException e) {
                throw new InputFormatException();
            }
            statDAO.changeStatType(statName, type);
        }
        if (newStatName != null) {
            statDAO.changeStatName(statName, statName);
        }
        if (statValue != null) {
            statDAO.changeStatValue(statName, statValue);
        }

        Stat result = statDAO.getStatByName(statName);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        if (statID != null && statName != null) {
            throw new InputFormatException();
        }
        else if (statID != null) {
            statDAO.deleteStat(statID);
        }
        else if (statName != null) {
            statDAO.deleteStat(statName);
        }
        else {
            throw new InputFormatException();
        }
    }
}
