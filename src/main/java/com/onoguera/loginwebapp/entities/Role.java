package com.onoguera.loginwebapp.entities;

/**
 * Created by oliver on 1/06/16.
 *
 */
public class Role extends Entity {


    public Role(String id) {
        super(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Role{"+getId()+"}";
    }
}
