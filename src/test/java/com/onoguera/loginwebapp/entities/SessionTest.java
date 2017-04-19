package com.onoguera.loginwebapp.entities;

import org.junit.Test;

/**
 * Created by olivernoguera on 19/04/2017.
 */
public class SessionTest {

    @Test
    public void createSession() {
        User user = new User("mockusername", "mockpassword");
        Session sessionToTest = new Session(user, "sessionmock1");

    }
}
