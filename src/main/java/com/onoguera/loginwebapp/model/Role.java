package com.onoguera.loginwebapp.model;

/**
 * Created by oliver on 1/06/16.
 */
public class Role extends Entity<Role>{

    private boolean fullAccess;

    public Role(String id) {
        super(id);
        this.fullAccess = false;
    }

    public Role(String id, boolean fullAccess){
        super(id);
        this.fullAccess = fullAccess;
    }

}
