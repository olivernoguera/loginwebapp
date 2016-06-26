package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;

/**
 * Created by olivernoguera on 26/06/2016.
 */
public interface SessionServiceInterface {

    void delete(String id);

    Session findOne(String sessionId);

    Session createSession(final User user);
}
