package com.fluxhydravault.restfrontendfx.model;

public class Admin {
    private String admin_id;
    private String username;
    private String admin_name;
    private String avatar;

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "admin_id='" + admin_id + '\'' +
                ", username='" + username + '\'' +
                ", admin_name='" + admin_name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
