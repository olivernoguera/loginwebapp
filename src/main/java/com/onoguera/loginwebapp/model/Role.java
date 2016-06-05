package com.onoguera.loginwebapp.model;

/**
 * Created by oliver on 1/06/16.
 */
public class Role extends Entity<Role>{

    public Role(String id) {
        super(id);
    }

    public Role(String id, boolean fullAccess){
        super(id);
    }

}
