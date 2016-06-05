package com.onoguera.loginwebapp.model;

/**
 * Created by oliver on 1/06/16.
 */
public class Role extends Entity<Role> {

    private boolean writeAccess;

    public Role(String id) {
        super(id);
    }

    public Role(String id, boolean writeAccess) {
        super(id);
        this.writeAccess = writeAccess;
    }

    public boolean isWriteAccess() {
        return writeAccess;
    }
}
