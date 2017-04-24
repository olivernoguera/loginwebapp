package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.Dao;
import com.onoguera.loginwebapp.dao.SessionDao;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;

import java.util.UUID;

/**
 * Created by olivernoguera on 25/06/2016.
 */
public class SessionService implements  SessionServiceInterface {

    private final static SessionService INSTANCE = new SessionService();
    private Dao sessionDao;

    private SessionService(){}

    public Session createSession(final User user){
        Session session = new Session(user, UUID.randomUUID().toString());
        sessionDao.insert(session);
        return session;
    }

    public static SessionService getInstance() {
        return INSTANCE;
    }

    public Session getSession(String sessionId) {
        return (Session) sessionDao.findOne(sessionId);
    }

    public void delete(String id) {
        this.sessionDao.delete(id);
    }

    public void setSessionDao(Dao sessionDao) {
         this.sessionDao = sessionDao;
    }


}
