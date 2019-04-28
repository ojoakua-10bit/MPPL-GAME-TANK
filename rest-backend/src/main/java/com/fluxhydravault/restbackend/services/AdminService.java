package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.model.Admin;

import java.util.List;

public interface AdminService {
    Admin newAdmin(String username, String password, String adminName);

    Admin getAdmin(String adminID);

    Admin getAdminByUsername(String username);

    List<Admin> searchAdmin(String username);

    List<Admin> listAdmins();

    void deleteAdmin(String adminID);

    void changeAdminUsername(String adminID, String username);

    void changeAdminPassword(String adminID, String password);

    void changeAdminName(String adminID, String adminName);

    void changePlayerAvatar(String adminID, String location);

    Admin authenticateUser(String username, String password);

    void deleteAdminAvatar(String adminID);
}
