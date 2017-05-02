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
public class BrowserSessionServiceTest {

    private static BrowserSessionService browserSessionService = BrowserSessionService.getInstance();

    private static class MockSessionDao extends GenericDao<Session>
            implements Dao<Session> {


    }

    @Before
    public void beforeTest() throws Exception {
        browserSessionService.setSessionDao(new MockSessionDao());
        browserSessionService.setPeriodTimeToExpiredSession(1000);
    }

    @Test
    public void createSessionTest() throws InterruptedException {

        User user1 = new User("mockUserId", "mockPassword");
        Session session = browserSessionService.createSession(user1);

        Assert.assertThat("SessionServiceTest createSessionTest createSession",
                browserSessionService.getSession(session.getId()), is(session));

        browserSessionService.delete(session.getId());
        Assert.assertThat("SessionServiceTest createSessionTest deleteSession",
                browserSessionService.getSession(session.getId()), is(nullValue()));

        browserSessionService.setPeriodTimeToExpiredSession(1);
        session = browserSessionService.createSession(user1);
        Assert.assertThat("SessionServiceTest createSessionTest createSession with 10 miliseconds expired",
                browserSessionService.getSession(session.getId()), is(session));
        Thread.sleep(10);
        Assert.assertThat("SessionServiceTest createSessionTest createSession after 20 miliseconds session expierd",
                browserSessionService.getSession(session.getId()), is(nullValue()));

    }


}