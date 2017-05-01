package com.onoguera.loginwebapp.controller;

/**
 * Created by olivernoguera on 01/05/2017.
 */

import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.service.SessionServiceInterface;

class SessionServiceWithSession implements SessionServiceInterface {

    static final String COOKIE = "Cookie";



    private final Session  sesion;

    public SessionServiceWithSession(Session sesion){
        this.sesion = sesion;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Session getSession(String sessionId) {
        return sesion;
    }

    @Override
    public Session createSession(User user) {
        return sesion;

    }
}
