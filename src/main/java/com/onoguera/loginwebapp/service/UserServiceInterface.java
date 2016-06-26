package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.User;

/**
 * Created by olivernoguera on 26/06/2016.
 */
public interface UserServiceInterface {

    User validateUser(User user);
}
