package com.onoguera.loginwebapp.dao;

import com.onoguera.loginwebapp.entities.Session;

/**
 * Created by olivernoguera on 25/06/2016.
 *
 */
public class SessionDao extends GenericDao<Session>  implements Dao<Session>  {

    private static final SessionDao INSTANCE = new SessionDao();

    /**
     * Protect singleton
     */
    private SessionDao() {
        super();
    }

    /**
     * Get Singleton instance
     *
     * @return SessionDao Instance
     */
    public static SessionDao getInstance() {
        return INSTANCE;
    }

}
