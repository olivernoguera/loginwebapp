package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.service.SessionServiceInterface;

/**
 * Created by olivernoguera on 01/05/2017.
 */
class SessionServiceWithoutSession implements SessionServiceInterface {

    @Override
    public void delete(String id) {

    }

    @Override
    public Session getSession(String sessionId) {
        return null;
    }

    @Override
    public Session createSession(User user) {
        return null;
    }
}
