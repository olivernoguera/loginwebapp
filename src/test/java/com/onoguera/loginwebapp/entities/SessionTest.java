package com.onoguera.loginwebapp.entities;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 19/04/2017.
 */
public class SessionTest {

    @Test
    public void createSession() {
        User user = new User("mockusername", "mockpassword");
        String sessionId = "sessionmock1";
        Session sessionToTest = new Session(user, sessionId,0L);

        Assert.assertThat("SessionTest createSession getUser", sessionToTest.getUser(), is(user));
        Assert.assertThat("SessionTest createSession getId", sessionToTest.getId(), is(sessionId));
    }
}
