package com.fluxhydravault.restbackend;

import com.fluxhydravault.restbackend.dao.PlayerDAO;
import com.fluxhydravault.restbackend.dao.TokenDAO;
import com.fluxhydravault.restbackend.utils.FileUploader;
import com.fluxhydravault.restbackend.utils.HeaderChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/uploads")
public class UploadsController {
    private PlayerDAO playerDAO;
    private TokenDAO tokenDAO;
    private FileUploader fileUploader;
    private final String FILE_SERVER_ROOT = "/static/";

    @Autowired
    public void setPlayerDAO(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Autowired
    public void setTokenDAO(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Autowired
    public void setFileUploader(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/images/{id}", method = RequestMethod.POST)
    public Map<String, Object> uploadAvatar(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID,
            @RequestParam("image-data") MultipartFile file
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        if (userToken != null && !tokenDAO.getToken(userToken).getPlayer_id().equals(playerID)) {
            throw new NoSuchPrivilegeException();
        }

        fileUploader.uploadImage(file);
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String path = FILE_SERVER_ROOT + "images/" + filename;
        playerDAO.changePlayerAvatar(playerID, path);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("message", "Update success.");
        map.put("path", path);
        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
    public void uploadAvatar(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "PLAYER", tokenDAO);

        playerDAO.deletePlayerAvatar(playerID);
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/images/{id}", method = RequestMethod.POST)
    public Map<String, Object> uploadAssets(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String itemID,
            @RequestParam("assets-data") MultipartFile file
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        fileUploader.uploadAsset(file);
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String path = FILE_SERVER_ROOT + "assets/" + filename;
        // add item assets

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("message", "Update success.");
        map.put("path", path);
        return map;
    }
}
