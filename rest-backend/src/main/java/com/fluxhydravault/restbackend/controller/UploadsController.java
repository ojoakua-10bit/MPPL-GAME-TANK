package com.fluxhydravault.restbackend.controller;

import com.fluxhydravault.restbackend.NoSuchPrivilegeException;
import com.fluxhydravault.restbackend.services.*;
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
    private PlayerService playerService;
    private TokenService tokenService;
    private ItemService itemService;
    private AdminService adminService;
    private FileUploadService fileUploadService;
    private final String FILE_SERVER_ROOT = "/static/";

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setFileUploadService(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/images/{id}", method = RequestMethod.POST)
    public Map<String, Object> uploadPlayerAvatar(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID,
            @RequestParam("image_data") MultipartFile file
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenService);

        if (tokenService.isValidPlayerToken(userToken) &&
                !tokenService.getUserToken(userToken).getUser_id().equals(playerID)) {
            throw new NoSuchPrivilegeException();
        }

        fileUploadService.uploadImage(playerID, false, file);
        String filename = playerID + StringUtils.cleanPath(file.getOriginalFilename());
        String path = FILE_SERVER_ROOT + "images/" + filename;
        playerService.changePlayerAvatar(playerID, path);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Player avatar upload success.");
        map.put("data", path);
        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/images/admin/{id}", method = RequestMethod.POST)
    public Map<String, Object> uploadAdminAvatar(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String adminID,
            @RequestParam("image_data") MultipartFile file
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (tokenService.isValidAdminToken(userToken)
                && !tokenService.getUserToken(userToken).getUser_id().equals(adminID)) {
            throw new NoSuchPrivilegeException();
        }

        fileUploadService.uploadImage(adminID, true, file);
        String filename = adminID + StringUtils.cleanPath(file.getOriginalFilename());
        String path = FILE_SERVER_ROOT + "images/admin/" + filename;
        adminService.changePlayerAvatar(adminID, path);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Avatar upload success.");
        map.put("data", path);
        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
    public void deletePlayerAvatar(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String playerID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenService);
        if (tokenService.isValidPlayerToken(userToken) &&
                !tokenService.getUserToken(userToken).getUser_id().equals(playerID)) {
            throw new NoSuchPrivilegeException();
        }

        playerService.deletePlayerAvatar(playerID);
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/images/admin/{id}", method = RequestMethod.DELETE)
    public void deleteAdminAvatar(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String adminID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);
        if (tokenService.isValidAdminToken(userToken)
                && !tokenService.getUserToken(userToken).getUser_id().equals(adminID)) {
            throw new NoSuchPrivilegeException();
        }

        adminService.deleteAdminAvatar(adminID);
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/assets/{id}", method = RequestMethod.POST)
    public Map<String, Object> uploadAssets(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String itemID,
            @RequestParam("assets_data") MultipartFile file
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        fileUploadService.uploadAsset(itemID, file);
        String filename = itemID + StringUtils.cleanPath(file.getOriginalFilename());
        String path = FILE_SERVER_ROOT + "assets/" + filename;
        itemService.changeItemModelLocation(itemID, FILE_SERVER_ROOT + "assets/" + filename);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Asset upload success.");
        map.put("data", path);
        return map;
    }
}
