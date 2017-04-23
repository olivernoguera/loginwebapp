package com.onoguera.loginwebapp.model;

import java.util.List;

/**
 * Created by olivernoguera on 04/06/2016.
 *
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

    public void addRole(final ReadRole role) {
        this.roles.add(role);
    }


    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReadUser readUser = (ReadUser) o;

        if (username != null ? !username.equals(readUser.username) : readUser.username != null) return false;
        return roles != null ? roles.equals(readUser.roles) : readUser.roles == null;

    }


}
