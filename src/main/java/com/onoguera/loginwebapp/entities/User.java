package com.onoguera.loginwebapp.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliver on 1/06/16.
 *
 */

public class User extends Entity {

    private String password;

    private List<Role> roles;


    public User(String username, String password) {
        super(username);
        this.password = password;
        this.roles = new ArrayList<>();

    }

    //TO BE refactor
    public User(String username, String password, List<Role> roles) {
        this(username, password);
        this.addRoles(roles);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }

    public void addRoles(final List<Role> roles) {
        if( roles != null){
            this.roles.addAll(roles);
        }
    }

    public void addRole(final Role role) {
        this.roles.add(role);
    }

    public void removeRole(String roleId) {
        Role role = new Role(roleId);
        this.roles.remove(role);
    }

    public void removeRoles() {
        this.roles = new ArrayList<>();
    }
}
