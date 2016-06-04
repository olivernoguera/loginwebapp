package com.onoguera.loginwebapp.model;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class UserVO {

    private String username;

    public UserVO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
