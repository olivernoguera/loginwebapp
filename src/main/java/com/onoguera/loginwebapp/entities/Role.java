package com.onoguera.loginwebapp.entities;

/**
 * Created by oliver on 1/06/16.
 *
 */
public class Role extends Entity {

    private boolean writeAccess;

    public Role(String id) {
        super(id);
        this.writeAccess = false;
    }

    public Role(String id, boolean writeAccess) {
        super(id);
        this.writeAccess = writeAccess;
    }

    public boolean isWriteAccess() {
        return writeAccess;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Role role = (Role) o;

        return writeAccess == role.writeAccess;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (writeAccess ? 1 : 0);
        return result;
    }
}
