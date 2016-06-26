package com.onoguera.loginwebapp.dao;

import com.onoguera.loginwebapp.entities.Session;

/**
 * Created by olivernoguera on 25/06/2016.
 *
 */
public class SessionDao extends GenericDao<Session>  {

    private final static SessionDao INSTANCE = new SessionDao();

    /**
     * Protect singleton
     */
    private SessionDao() {
        super();
    }

    /**
     * Get Singleton instance
     *
     * @return
     */
    public static SessionDao getInstance() {
        return INSTANCE;
    }

}
