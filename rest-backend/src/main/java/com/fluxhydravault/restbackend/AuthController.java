package com.fluxhydravault.restbackend;

import com.fluxhydravault.restbackend.dao.PlayerDAO;
import com.fluxhydravault.restbackend.dao.TokenDAO;
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
    private PlayerDAO playerDAO;
    private TokenDAO tokenDAO;

    @Autowired
    public void setPlayerDAO(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Autowired
    public void setTokenDAO(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> login(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        HeaderChecker.checkAppToken(appToken);

        Player player = playerDAO.authenticateUser(username, password);
        if (player == null) {
            throw new NotAuthenticatedException("Wrong username or password!");
        }
        String token = tokenDAO.generateToken(player.getPlayer_id());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("time", new Date());
        map.put("user_token", token);
        map.put("user_data", player);
        return map;
    }
}
