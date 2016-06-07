package com.onoguera.loginwebapp.model;

import java.util.List;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class ReadUser implements ReadDTO {

    private  String username;

    private  List<ReadRole> roles;

    public ReadUser(final String username, final List<ReadRole> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public List<ReadRole> getRoles() {
        return roles;
    }

    public void setRoles(final List<ReadRole> roles) {
        this.roles = roles;
    }

    public void addRoles(final List<ReadRole> roles) {
        this.roles.addAll(roles);
    }

    public void addRole(final ReadRole role) {
        this.roles.add(role);
    }


}
