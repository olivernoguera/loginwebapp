package com.onoguera.loginwebapp.model;

/**
 * Created by olivernoguera on 07/06/2016.
 */
public class WriteRole implements WriteDTO {

    private String role;
    private boolean writeAccess;

    public WriteRole(String role) {
        this.role = role;
    }
    public WriteRole(String role, boolean writeAccess) {
        this.role = role;
        this.writeAccess = writeAccess;
    }

    public WriteRole() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isWriteAccess() {
        return writeAccess;
    }

    public void setWriteAccess(boolean writeAccess) {
        this.writeAccess = writeAccess;
    }
}
