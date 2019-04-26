package com.fluxhydravault.restbackend;

import com.fluxhydravault.restbackend.dao.MatchDAO;
import com.fluxhydravault.restbackend.dao.TokenDAO;
import com.fluxhydravault.restbackend.model.Match;
import com.fluxhydravault.restbackend.model.Player;
import com.fluxhydravault.restbackend.dao.PlayerDAO;
import com.fluxhydravault.restbackend.model.PlayerInventory;
import com.fluxhydravault.restbackend.utils.HeaderChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private PlayerDAO playerDAO;
    private TokenDAO tokenDAO;
    private MatchDAO matchDAO;

    @Autowired
    public void setPlayerDAO(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Autowired
    public void setTokenDAO(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Autowired
    public void setMatchDAO(MatchDAO matchDAO) {
        this.matchDAO = matchDAO;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> newPlayer(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("player_name") String playerName
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);
        Player result = playerDAO.newPlayer(username, password, playerName);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Registration Success");
        map.put("data", result);

        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/{id}", method = { RequestMethod.PATCH, RequestMethod.PUT })
    public Map<String, Object> updatePlayer(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam Map<String, String> params,
            @PathVariable("id") String playerID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenDAO);

        if (userToken != null && !tokenDAO.getToken(userToken).getPlayer_id().equals(playerID)) {
            throw new NoSuchPrivilegeException();
        }

        if (params.containsKey("username")) {
            playerDAO.changePlayerUsername(playerID, params.get("username"));
        }
        if (params.containsKey("password")) {
            playerDAO.changePlayerPassword(playerID, params.get("password"));
        }
        if (params.containsKey("player_name")) {
            playerDAO.changePlayerName(playerID, params.get("player_name"));
        }

        try {
            if (params.containsKey("rank")) {
                playerDAO.changePlayerRank(playerID, Integer.parseInt(params.get("rank")));
            }
            if (params.containsKey("xp")) {
                playerDAO.changePlayerXP(playerID, Integer.parseInt(params.get("xp")));
            }
            if (params.containsKey("diamond_count")) {
                playerDAO.changePlayerDiamond(playerID, Integer.parseInt(params.get("diamond_count")));
            }
            if (params.containsKey("gold_count")) {
                playerDAO.changePlayerGold(playerID, Integer.parseInt(params.get("gold_count")));
            }
            if (params.containsKey("credit_balance")) {
                playerDAO.changePlayerCredit(playerID, Integer.parseInt(params.get("credit_balance")));
            }
            if (params.containsKey("inventory")) {
                playerDAO.changePlayerInventory(playerID, Integer.parseInt(params.get("inventory")));
            }
            if (params.containsKey("online_status")) {
                playerDAO.setPlayerOnlineStatus(playerID, Boolean.getBoolean(params.get("online_status")));
            }
        } catch (NumberFormatException ex) {
            throw new InputFormatException();
        }

        Player result = playerDAO.getPlayer(playerID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("message", "Update success.");
        map.put("data", result);
        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePlayer(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        playerDAO.deletePlayer(playerID);
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void deletePlayer(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenDAO);

        throw new NotAllowedException("Cannot delete all players, too dangerous");
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> searchPlayerByUsername(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(name = "q", required = false) String username
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenDAO);
        Map<String, Object> map = new LinkedHashMap<>();
        Player matchesResult;
        List<Player> possibleResults;
        if (username == null){
            matchesResult = null;
            possibleResults = playerDAO.listPlayers();
        }
        else {
            matchesResult = playerDAO.getPlayerByUsername(username);
            possibleResults = playerDAO.searchPlayer(username);
        }

        map.put("timestamp", new Date());
        map.put("matched_result", matchesResult);
        map.put("possible_results", possibleResults);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Map<String, Object> searchPlayerByID(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenDAO);
        Map<String, Object> map = new LinkedHashMap<>();
        Player matchesResult = playerDAO.getPlayer(playerID);

        map.put("timestamp", new Date());
        map.put("matched_result", matchesResult);
        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/matches", method = RequestMethod.POST)
    public Map<String, Object> newMatchHistory(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID,
            @RequestParam("match_status") boolean matchStatus,
            @RequestParam("score") int score,
            @RequestParam("total_damage") int totalDamage,
            @RequestParam("gold_gained") int goldGained,
            @RequestParam(value = "item_gained", required = false) String itemGained
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        if (!tokenDAO.getToken(userToken).getPlayer_id().equals(playerID)) {
            throw new NotAuthenticatedException();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        Match result = matchDAO.newMatch(playerID, matchStatus, score, totalDamage, goldGained, itemGained);
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("result", result);

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/matches", method = RequestMethod.GET)
    public List<Match> listMatches(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID,
            @RequestParam(name = "from", required = false) Integer start,
            @RequestParam(name = "n", required = false) Integer limit,
            HttpServletResponse response
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        List<Match> result;
        response.addHeader("X-Total-Count", matchDAO.getUserMatchesCount(playerID).toString());
        if (start == null && limit == null) {
            result = matchDAO.getUserMatches(playerID);
        }
        else if (start == null) {
            result = matchDAO.getUserMatches(playerID, 0, limit);
        }
        else if (limit == null) {
            result = matchDAO.getUserMatches(playerID, start, 10);
        }
        else {
            result = matchDAO.getUserMatches(playerID, start, limit);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/{player_id}/matches/{match_id}", method = RequestMethod.GET)
    public Match getMatch(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("player_id") String playerID,
            @PathVariable("match_id") String matchID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        return matchDAO.getMatch(matchID);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/friends", method = RequestMethod.GET)
    public List<Player> getFriends(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID,
            @RequestParam(name = "from", required = false) Integer start,
            @RequestParam(name = "n", required = false) Integer limit,
            HttpServletResponse response
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        List<Player> result;
        response.addHeader("X-Total-Count", playerDAO.getNumberOfFriends(playerID).toString());
        if (start == null && limit == null) {
            result = playerDAO.getFriends(playerID, 0, 10);
        }
        else if (start == null) {
            result = playerDAO.getFriends(playerID, 0, limit);
        }
        else if (limit == null) {
            result = playerDAO.getFriends(playerID, start, 10);
        }
        else {
            result = playerDAO.getFriends(playerID, start, limit);
        }

        return result;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/friends", method = RequestMethod.POST)
    public Map<String, Object> addFriend(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID,
            @RequestParam("friend_id") String friendID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        playerDAO.addFriend(playerID, friendID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Player '" + friendID + "' has been successfully added as your friend");

        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{player_id}/friends/{friend_id}", method = RequestMethod.DELETE)
    public void unFriend(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("player_id") String playerID,
            @PathVariable("friend_id") String friendID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        playerDAO.removeFriend(playerID, friendID);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/friends", method = RequestMethod.DELETE)
    public void unFriend(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        throw new NotAllowedException();
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/ban", method = { RequestMethod.PUT, RequestMethod.PATCH })
    public void setBan(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID,
            @RequestParam("value") boolean value
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        if (value) playerDAO.banPlayer(playerID);
        else playerDAO.unBanPlayer(playerID);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public List<PlayerInventory> getPlayerItems(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        return playerDAO.getPlayerItems(playerID);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/items", method = RequestMethod.POST)
    public List<PlayerInventory> addInventory(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID,
            @RequestParam("item_id") String itemID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        if (!tokenDAO.getToken(userToken).getPlayer_id().equals(playerID)) {
            throw new NoSuchPrivilegeException();
        }

        return playerDAO.addItemToInventory(playerID, itemID);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}/items", method = RequestMethod.DELETE)
    public void deletePlayerItem(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID,
            @RequestParam("q") long inventoryID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        if (!tokenDAO.getToken(userToken).getPlayer_id().equals(playerID)) {
            throw new NoSuchPrivilegeException();
        }

        playerDAO.deleteItemFromInventory(inventoryID);
    }
}
