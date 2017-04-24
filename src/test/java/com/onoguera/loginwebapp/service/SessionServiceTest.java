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
    }

    @Test
    public void createSessionTest() {

        User user1 = new User("mockUserId", "mockPassword");
        Session session = sessionService.createSession(user1);
        Session session1 = new Session(session.getUser(),session.getId());

        Assert.assertThat("SessionServiceTest createSessionTest createSession",
                sessionService.getSession(session.getId()), is(session));

        Assert.assertThat("SessionServiceTest createSessionTest getSession",
                sessionService.getSession(session.getId()), is(session1));
        sessionService.delete(session.getId());
        Assert.assertThat("SessionServiceTest createSessionTest deleteSession",
                sessionService.getSession(session.getId()), is(nullValue()));
    }


}