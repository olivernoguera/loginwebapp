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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return id != null ? id.equals(entity.id) : entity.id == null;

    }



    public String getId() {
        return id;
    }


}
