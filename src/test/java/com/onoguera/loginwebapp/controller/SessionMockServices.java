package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.service.SessionServiceInterface;
import com.onoguera.loginwebapp.service.UserServiceInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivernoguera on 26/06/2016.
 *
 */
public final class SessionMockServices {

    /**
     * Protect instances
     *
     */
    private SessionMockServices(){}



    public static class SessionServiceMock implements SessionServiceInterface{

        @Override
        public void delete(String id) {

        }

        @Override
        public Session findOne(String sessionId) {
            return new Session(new User("test", "test"),sessionId);
        }

        @Override
        public Session createSession(User user) {
            return new Session(new User("test", "test"),"1");
        }
    }


}
