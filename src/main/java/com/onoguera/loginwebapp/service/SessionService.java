package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.Dao;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;

import java.util.UUID;

/**
 * Created by olivernoguera on 25/06/2016.
 */
public class SessionService implements  SessionServiceInterface {

    private final static SessionService INSTANCE = new SessionService();
    private Dao sessionDao;
    private Integer TIME_PERIOD_TO_EXPIRED = 1 * 60 * 1000;

    private SessionService(){}

    public Session createSession(final User user){
        Session session = new Session(user, calcTimeToExpire().toString());
        sessionDao.insert(session);
        return session;
    }

    public static SessionService getInstance() {
        return INSTANCE;
    }

    public Session getSession(String sessionId) {
       Session session = (Session) sessionDao.findOne(sessionId);
       if( session != null){
           Long timeToExpireCurrentSession = Long.parseLong(session.getId());
           Long now = System.currentTimeMillis();
           sessionDao.delete(sessionId);
           if( timeToExpireCurrentSession + TIME_PERIOD_TO_EXPIRED < now){
                return null;
           }else{
                return this.createSession(session.getUser());
           }
       }
       return null;

    }

    public void delete(String id) {
        this.sessionDao.delete(id);
    }

    public void setSessionDao(Dao sessionDao) {
         this.sessionDao = sessionDao;
    }

    private Long calcTimeToExpire(){
        Long now = System.currentTimeMillis();
        Long timeToExpire = now + TIME_PERIOD_TO_EXPIRED;
        return timeToExpire;
    }

}
