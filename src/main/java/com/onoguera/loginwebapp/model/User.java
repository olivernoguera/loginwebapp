package com.onoguera.loginwebapp.model;

/**
 * Created by oliver on 1/06/16.
 */

public class User extends Entity<User>{

    private String password;

    public User(String username, String password) {
        super(username);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
