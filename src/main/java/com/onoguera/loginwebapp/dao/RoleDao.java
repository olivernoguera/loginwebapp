package com.onoguera.loginwebapp.dao;


import com.onoguera.loginwebapp.entities.Role;

/**
 * Created by olivernoguera on 05/06/2016.
 *
 */
public class RoleDao extends GenericDao<Role> {

    private final static RoleDao INSTANCE = new RoleDao();

    /**
     * Protect singleton
     */
    private RoleDao() {
        super();
    }

    /**
     * Get Singleton instance
     *
     * @return
     */
    public static RoleDao getInstance() {
        return INSTANCE;
    }

}
