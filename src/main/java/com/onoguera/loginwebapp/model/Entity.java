package com.onoguera.loginwebapp.model;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public abstract class Entity<T> implements Cloneable {

    private String id;

    public Entity(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Entity) {
            return this.getClass().isAssignableFrom(obj.getClass())
                    && ((Entity<?>) obj).getId().equals(this.getId());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " { id:" + getId() + " }";
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
