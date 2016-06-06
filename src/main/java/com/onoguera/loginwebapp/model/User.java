package com.onoguera.loginwebapp.model;

import com.onoguera.loginwebapp.dao.AbstractDao;

import java.util.Collection;
import java.util.List;

/**
 * Created by oliver on 1/06/16.
 */

public class User extends Entity<User> {

    private String password;

    //TO BE refactor persistence delegate DAo
    private final AbstractDao<Role> roles;


    public User(String username, String password) {
        super(username);
        this.password = password;
        this.roles = new AbstractDao<>();
    }

    //TO BE refactor
    public User(String username, String password,List<Role> roles){
        this(username,password);
        this.addRoles(roles);
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

    public void addVORoles(List<RoleVO> roles) {
        roles.stream().forEach(r -> this.roles.insert(new Role(r.getRole())));
    }

    public void deleteRoles() {
        roles.deleteAll();
    }

    public void removeRole(final String roleId) {
        roles.delete(roleId);
    }


}
