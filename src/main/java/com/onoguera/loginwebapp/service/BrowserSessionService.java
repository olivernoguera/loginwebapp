package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.Dao;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;

import java.util.UUID;

/**
 * Created by olivernoguera on 25/06/2016.
 */
public class BrowserSessionService implements SessionService {

    private final static BrowserSessionService INSTANCE = new BrowserSessionService();
    private Dao sessionDao;
    protected Integer periodTimeToExpiredSession;

    private BrowserSessionService(){}

    public Session createSession(final User user){
        Session session = new Session(user, UUID.randomUUID().toString(), calcTimeToExpire());
        sessionDao.insert(session);
        return session;
    }

    public static BrowserSessionService getInstance() {
        return INSTANCE;
    }

    public Session getSession(String sessionId) {
       Session session = (Session) sessionDao.findOne(sessionId);
       if( session != null){
           Long timeToExpireCurrentSession = session.getTimeToExpire();
           Long now = System.currentTimeMillis();

           if( timeToExpireCurrentSession + periodTimeToExpiredSession < now){
                sessionDao.delete(sessionId);
                return null;
           }else{
               session.setTimeToExpire(calcTimeToExpire());
               this.sessionDao.update(session);
               return session;
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
        Long timeToExpire = now + periodTimeToExpiredSession;
        return timeToExpire;
    }

    public void setPeriodTimeToExpiredSession(Integer periodTimeToExpiredSession) {
        this.periodTimeToExpiredSession = periodTimeToExpiredSession;
    }
}
