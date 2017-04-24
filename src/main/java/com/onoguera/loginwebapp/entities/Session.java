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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Session session = (Session) o;

        return user != null ? user.equals(session.user) : session.user == null;

    }
}
