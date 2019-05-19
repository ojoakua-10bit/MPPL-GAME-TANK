package com.fluxhydravault.restbackend.controller;

import com.fluxhydravault.restbackend.*;
import com.fluxhydravault.restbackend.services.MatchService;
import com.fluxhydravault.restbackend.services.TokenService;
import com.fluxhydravault.restbackend.model.Match;
import com.fluxhydravault.restbackend.model.Player;
import com.fluxhydravault.restbackend.services.PlayerService;
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
    private PlayerService playerService;
    private TokenService tokenService;
    private MatchService matchService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setMatchService(MatchService matchService) {
        this.matchService = matchService;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, Object> newPlayer(
            @RequestHeader(name = "App-Token", required = false) String appToken,
//            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("player_name") String playerName
    ) {
//        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);
        HeaderChecker.checkAppToken(appToken);
        Player result = playerService.newPlayer(username, password, playerName);

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
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenService);

        if (tokenService.isValidPlayerToken(userToken)
                && !tokenService.getUserToken(userToken).getUser_id().equals(playerID)) {
            throw new NoSuchPrivilegeException();
        }

        if (params.containsKey("username")) {
            playerService.changePlayerUsername(playerID, params.get("username"));
        }
        if (params.containsKey("password")) {
            playerService.changePlayerPassword(playerID, params.get("password"));
        }
        if (params.containsKey("player_name")) {
            playerService.changePlayerName(playerID, params.get("player_name"));
        }

        try {
            if (params.containsKey("rank")) {
                playerService.changePlayerRank(playerID, Integer.parseInt(params.get("rank")));
            }
            if (params.containsKey("xp")) {
                playerService.changePlayerXP(playerID, Integer.parseInt(params.get("xp")));
            }
            if (params.containsKey("diamond_count")) {
                playerService.changePlayerDiamond(playerID, Integer.parseInt(params.get("diamond_count")));
            }
            if (params.containsKey("gold_count")) {
                playerService.changePlayerGold(playerID, Integer.parseInt(params.get("gold_count")));
            }
            if (params.containsKey("credit_balance")) {
                playerService.changePlayerCredit(playerID, Integer.parseInt(params.get("credit_balance")));
            }
            if (params.containsKey("inventory")) {
                playerService.changePlayerInventory(playerID, Integer.parseInt(params.get("inventory")));
            }
            if (params.containsKey("online_status")) {
                playerService.setPlayerOnlineStatus(playerID, Boolean.getBoolean(params.get("online_status")));
            }
        } catch (NumberFormatException ex) {
            throw new InputFormatException();
        }

        Player result = playerService.getPlayer(playerID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        playerService.deletePlayer(playerID);
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void deletePlayer(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenService);

        throw new NotAllowedException("Cannot delete all players, too dangerous");
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> searchPlayerByUsername(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(name = "q", required = false) String username
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenService);
        Map<String, Object> map = new LinkedHashMap<>();
        Player matchesResult;
        List<Player> possibleResults;
        if (username == null){
            matchesResult = null;
            possibleResults = playerService.listPlayers();
        }
        else {
            matchesResult = playerService.getPlayerByUsername(username);
            possibleResults = playerService.searchPlayer(username);
        }

        map.put("timestamp", new Date());
        map.put("matched_result", matchesResult);
        map.put("possible_results", possibleResults);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Player getPlayerByID(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenService);

        Player player = playerService.getPlayer(playerID);
        if (player == null) {
            throw new NotFoundException("Player@" + playerID);
        }

        return player;
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
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);

        if (!tokenService.getUserToken(userToken).getUser_id().equals(playerID)) {
            throw new NotAuthenticatedException();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        Match result = matchService.newMatch(playerID, matchStatus, score, totalDamage, goldGained, itemGained);
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Match history created");
        map.put("data", result);

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
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);

        List<Match> result;
        response.addHeader("X-Total-Count", matchService.getUserMatchesCount(playerID).toString());
        if (start == null && limit == null) {
            result = matchService.getUserMatches(playerID);
        }
        else if (start == null) {
            result = matchService.getUserMatches(playerID, 0, limit);
        }
        else if (limit == null) {
            result = matchService.getUserMatches(playerID, start, 10);
        }
        else {
            result = matchService.getUserMatches(playerID, start, limit);
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
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);

        return matchService.getMatch(matchID);
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
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);

        List<Player> result;
        response.addHeader("X-Total-Count", playerService.getNumberOfFriends(playerID).toString());
        if (start == null && limit == null) {
            result = playerService.getFriends(playerID, 0, 10);
        }
        else if (start == null) {
            result = playerService.getFriends(playerID, 0, limit);
        }
        else if (limit == null) {
            result = playerService.getFriends(playerID, start, 10);
        }
        else {
            result = playerService.getFriends(playerID, start, limit);
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
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);

        playerService.addFriend(playerID, friendID);
        Player data = playerService.getPlayer(friendID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Player '" + friendID + "' has been successfully added as your friend");
        map.put("data", data);

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
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);

        if (!tokenService.getUserToken(userToken).getUser_id().equals(playerID)) {
            throw new NoSuchPrivilegeException();
        }

        playerService.removeFriend(playerID, friendID);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/friends", method = RequestMethod.DELETE)
    public void unFriend(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);

        throw new NotAllowedException();
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}/ban", method = { RequestMethod.PUT, RequestMethod.PATCH })
    public void setBan(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID,
            @RequestParam("value") boolean value
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (value) playerService.banPlayer(playerID);
        else playerService.unBanPlayer(playerID);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public List<PlayerInventory> getPlayerItems(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);

        return playerService.getPlayerItems(playerID);
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
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);

        if (!tokenService.getUserToken(userToken).getUser_id().equals(playerID)) {
            throw new NoSuchPrivilegeException();
        }

        return playerService.addItemToInventory(playerID, itemID);
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
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenService);

        if (!tokenService.getUserToken(userToken).getUser_id().equals(playerID)) {
            throw new NoSuchPrivilegeException();
        }

        playerService.deleteItemFromInventory(inventoryID);
    }
}
