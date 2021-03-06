package com.fluxhydravault.restbackend.controller;

import com.fluxhydravault.restbackend.NotAuthenticatedException;
import com.fluxhydravault.restbackend.model.Admin;
import com.fluxhydravault.restbackend.services.AdminService;
import com.fluxhydravault.restbackend.services.PlayerService;
import com.fluxhydravault.restbackend.services.TokenService;
import com.fluxhydravault.restbackend.model.Player;
import com.fluxhydravault.restbackend.utils.HeaderChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private PlayerService playerService;
    private AdminService adminService;
    private TokenService tokenService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> login(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        HeaderChecker.checkAppToken(appToken);

        Player player = playerService.authenticateUser(username, password);
        if (player == null) {
            throw new NotAuthenticatedException("Wrong username or password!");
        }
        String token = tokenService.generateUserToken(player.getPlayer_id());
        playerService.setPlayerOnlineStatus(player.getPlayer_id(), true);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("token", token);
        map.put("data", player);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public Map<String, Object> loginAdmin(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        HeaderChecker.checkAppToken(appToken);

        Admin admin = adminService.authenticateUser(username, password);
        if (admin == null) {
            throw new NotAuthenticatedException("Wrong username or password!");
        }
        String token = tokenService.generateAdminToken(admin.getAdmin_id());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("token", token);
        map.put("data", admin);
        return map;
    }
}
