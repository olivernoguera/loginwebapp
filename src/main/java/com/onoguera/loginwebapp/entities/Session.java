package com.onoguera.loginwebapp.entities;

/**
 * Created by olivernoguera on 25/06/2016.
 * 
 */
public class Session extends Entity {

    private final User user;

    public Session(User user, String id) {
        super(id);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
