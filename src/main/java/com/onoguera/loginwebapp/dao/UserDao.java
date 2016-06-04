package com.onoguera.loginwebapp.dao;

import com.onoguera.loginwebapp.model.User;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class UserDao  extends AbstractDao<User>{

    private final static UserDao INSTANCE = new UserDao();

    /**
     * Protect singleton
     */
    private UserDao(){
       super();
    }

    /**
     * Get Singleton instance
     * @return
     */
    public static UserDao getInstance(){
        return INSTANCE;
    }

}
