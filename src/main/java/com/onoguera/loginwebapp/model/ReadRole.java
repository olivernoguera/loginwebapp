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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReadRole readRole = (ReadRole) o;

        return role != null ? role.equals(readRole.role) : readRole.role == null;

    }

    @Override
    public int hashCode() {
        return role != null ? role.hashCode() : 0;
    }
}
