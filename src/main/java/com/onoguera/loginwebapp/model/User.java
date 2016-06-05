package com.onoguera.loginwebapp.model;

import com.onoguera.loginwebapp.dao.AbstractDao;

import java.util.Collection;
import java.util.List;

/**
 * Created by oliver on 1/06/16.
 */

public class User extends Entity<User> {

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

    public Collection<Role> getRoles() {
        return roles.elements();
    }

    public void addRole(final Role role) {
        roles.insert(role);
    }

    public void addRoles(final List<Role> roles) {
        roles.stream().forEach(r -> this.roles.insert(r));
    }

    public void deleteRoles() {
        roles.deleteAll();
    }

    public void removeRole(final String roleId) {
        roles.delete(roleId);
    }
}
