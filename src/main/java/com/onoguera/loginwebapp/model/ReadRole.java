package com.onoguera.loginwebapp.model;

/**
 * Created by olivernoguera on 05/06/2016.
 *
 */
public class ReadRole implements ReadDTO {

    private String role;

    public ReadRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
