package com.onoguera.loginwebapp.entities;

/**
 * Created by olivernoguera on 25/06/2016.
 * 
 */
public class Session extends Entity {

    private final User user;
    private Long timeToExpire;

    public Session(User user, String id, Long timeToExpire) {
        super(id);
        this.user = user;
        this.timeToExpire = timeToExpire;
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

    @Override
    public String toString() {
        return "Session{id=" + getId()+
                ",user=" + user +
                '}';
    }

    public Long getTimeToExpire() {
        return timeToExpire;
    }

    public void setTimeToExpire(Long timeToExpire) {
        this.timeToExpire = timeToExpire;
    }
}
