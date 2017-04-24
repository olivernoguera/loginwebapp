package com.onoguera.loginwebapp.model;

import java.util.List;

/**
 * Created by olivernoguera on 04/06/2016.
 *
 */
public class WriteUser implements WriteDTO {

    private  String username;

    private  String password;

    private  List<WriteRole> roles;

    public WriteUser(final String username, final String password,final List<WriteRole> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public WriteUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<WriteRole> getRoles() {
        return roles;
    }

    public void setRoles(final List<WriteRole> roles) {
        this.roles = roles;
    }

    public void addRoles(final List<WriteRole> roles) {
        this.roles.addAll(roles);
    }

    public void addRole(final WriteRole role) {
        this.roles.add(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WriteUser writeUser = (WriteUser) o;

        if (username != null ? !username.equals(writeUser.username) : writeUser.username != null) return false;
        if (password != null ? !password.equals(writeUser.password) : writeUser.password != null) return false;
        return roles != null ? roles.equals(writeUser.roles) : writeUser.roles == null;
    }

}
