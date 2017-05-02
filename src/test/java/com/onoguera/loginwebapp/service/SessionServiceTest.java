package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.Dao;
import com.onoguera.loginwebapp.dao.GenericDao;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 23/04/2017.
 */
public class SessionServiceTest {

    private static SessionService sessionService = SessionService.getInstance();

    private static class MockSessionDao extends GenericDao<Session>
            implements Dao<Session> {


    }

    @Before
    public void beforeTest() throws Exception {
        sessionService.setSessionDao(new MockSessionDao());
        sessionService.setPeriodTimeToExpiredSession(1000);
    }

    @Test
    public void createSessionTest() throws InterruptedException {

        User user1 = new User("mockUserId", "mockPassword");
        Session session = sessionService.createSession(user1);

        Assert.assertThat("SessionServiceTest createSessionTest createSession",
                sessionService.getSession(session.getId()), is(session));

        sessionService.delete(session.getId());
        Assert.assertThat("SessionServiceTest createSessionTest deleteSession",
                sessionService.getSession(session.getId()), is(nullValue()));

        sessionService.setPeriodTimeToExpiredSession(1);
        session = sessionService.createSession(user1);
        Assert.assertThat("SessionServiceTest createSessionTest createSession with 10 miliseconds expired",
                sessionService.getSession(session.getId()), is(session));
        Thread.sleep(10);
        Assert.assertThat("SessionServiceTest createSessionTest createSession after 20 miliseconds session expierd",
                sessionService.getSession(session.getId()), is(nullValue()));

    }


}