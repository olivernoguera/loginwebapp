package com.onoguera.loginwebapp.controller;

/**
 * Created by olivernoguera on 12/06/2016.
 */
public class Authorization {

    private String username;
    private String password;

    public Authorization(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
