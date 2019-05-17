package com.fluxhydravault.restbackend.controller;

import com.fluxhydravault.restbackend.NoSuchPrivilegeException;
import com.fluxhydravault.restbackend.model.Admin;
import com.fluxhydravault.restbackend.services.AdminService;
import com.fluxhydravault.restbackend.services.TokenService;
import com.fluxhydravault.restbackend.utils.HeaderChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admins")
public class AdminController {
    private AdminService adminService;
    private TokenService tokenService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> newAdmin(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("admin_name") String adminName
    ) {
        HeaderChecker.checkAppToken(appToken);

        Admin result = adminService.newAdmin(username, password, adminName);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Admin registration success");
        map.put("data", result);

        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public Map<String, Object> editAdmin(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String adminID,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "admin_name", required = false) String adminName
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (username != null && !adminService.getAdmin(adminID).getUsername().equals(username)) {
            adminService.changeAdminUsername(adminID, username);
        }
        if (password != null) {
            adminService.changeAdminPassword(adminID, password);
        }
        if (adminName != null) {
            adminService.changeAdminName(adminID, adminName);
        }

        Admin result = adminService.getAdmin(adminID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Admin update success.");
        map.put("data", result);
        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteAdmin(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String adminID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);
        if (tokenService.isValidAdminToken(userToken)
                && !tokenService.getUserToken(userToken).getUser_id().equals(adminID)) {
            throw new NoSuchPrivilegeException();
        }

        adminService.deleteAdmin(adminID);
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> searchPlayerByUsername(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(name = "q", required = false) String username
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);
        Map<String, Object> map = new LinkedHashMap<>();
        Admin matchesResult;
        List<Admin> possibleResults;
        if (username == null){
            matchesResult = null;
            possibleResults = adminService.listAdmins();
        }
        else {
            matchesResult = adminService.getAdminByUsername(username);
            possibleResults = adminService.searchAdmin(username);
        }

        map.put("timestamp", new Date());
        map.put("matched_result", matchesResult);
        map.put("possible_results", possibleResults);
        return map;
    }
}
