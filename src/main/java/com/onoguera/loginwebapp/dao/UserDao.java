package com.onoguera.loginwebapp.dao;


import com.onoguera.loginwebapp.entities.User;

/**
 * Created by olivernoguera on 04/06/2016.
 *
 */
public class UserDao extends GenericDao<User> implements Dao<User>   {

    private final static UserDao INSTANCE = new UserDao();


    /**
     * Protect singleton
     */
    private UserDao() {
        super();
    }

    /**
     * Get Singleton instance
     *
     * @return User Dao instance
     */
    public static UserDao getInstance() {
        return INSTANCE;
    }

}
