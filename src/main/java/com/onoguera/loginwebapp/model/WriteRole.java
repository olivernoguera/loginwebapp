package com.onoguera.loginwebapp.model;

/**
 * Created by olivernoguera on 07/06/2016.
 *
 */
public class WriteRole implements WriteDTO {

    private String role;


    public WriteRole(String role) {
        this.role = role;
    }

    public WriteRole() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WriteRole writeRole = (WriteRole) o;
        return role != null ? role.equals(writeRole.role) : writeRole.role == null;

    }


}
