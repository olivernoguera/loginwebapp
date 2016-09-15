package com.onoguera.loginwebapp.entities;

/**
 * Created by olivernoguera on 04/06/2016.
 *
 *
 */
public abstract class Entity {

    private final String id;

    public Entity(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Entity
                    && ((Entity) obj).getId().equals(this.getId());
    }


    public String getId() {
        return id;
    }


}
