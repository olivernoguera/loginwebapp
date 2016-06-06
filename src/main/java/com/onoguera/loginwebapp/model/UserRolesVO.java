package com.onoguera.loginwebapp.model;

import java.util.List;

/**
 * Created by olivernoguera on 05/06/2016.
 */
public class UserRolesVO {

    private String username;
    private String password;

    private List<RoleVO> roles;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<RoleVO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleVO> roles) {
        this.roles = roles;
    }
}
