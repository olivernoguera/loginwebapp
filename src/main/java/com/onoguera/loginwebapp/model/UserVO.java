package com.onoguera.loginwebapp.model;

import java.util.Collection;
import java.util.List;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class UserVO {

    private final String username;

    private final Collection<Role> roles;

    public UserVO(final String username, final Collection<Role> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public Collection<Role> getRoles() {
        return roles;
    }


}
