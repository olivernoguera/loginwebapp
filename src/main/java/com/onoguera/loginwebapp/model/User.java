package com.onoguera.loginwebapp.model;

import com.onoguera.loginwebapp.dao.AbstractDao;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by oliver on 1/06/16.
 */

public class User extends Entity<User>{

    private String password;

    private final AbstractDao<Role> roles;

    public User(String username, String password) {
        super(username);
        this.password = password;
        this.roles = new AbstractDao<>();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles(){
        return roles.elements();
    }

    public void addRole(final Role role){
        roles.insert(role);
    }

    public void deleteRoles() {
        roles.deleteAll();
    }
}
