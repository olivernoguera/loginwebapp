package com.onoguera.loginwebapp.dao;


import com.onoguera.loginwebapp.entities.Role;

/**
 * Created by olivernoguera on 05/06/2016.
 *
 */
public class RoleDao extends GenericDao<Role> implements Dao<Role> {

    private static final RoleDao INSTANCE = new RoleDao();

    /**
     * Protect singleton
     */
    private RoleDao() {
        super();
    }

    /**
     * Get Singleton instance
     *
     * @return RoleDao instance
     */
    public static RoleDao getInstance() {
        return INSTANCE;
    }

}
