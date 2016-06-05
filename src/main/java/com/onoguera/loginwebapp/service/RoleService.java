package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.RoleDao;
import com.onoguera.loginwebapp.model.Role;

import java.util.Collection;

/**
 * Created by oliver on 1/06/16.
 */
public class RoleService implements  Service
{
    private final static RoleService INSTANCE = new RoleService();
    private final RoleDao roleDao  = RoleDao.getInstance();

    private RoleService(){
        super();
    }

    public static RoleService getInstance() {
        return INSTANCE;
    }

    public void addRole(Role role){
        this.roleDao.insert(role);
    }

    public void bulkCreateRoles(Collection<Role> roles){
        roles.forEach(role -> this.roleDao.insert(role));
    }

    public Collection<Role> getRoles(){
        return this.roleDao.elements();
    }


    public void removeRole(final String id) {
        this.roleDao.delete(id);
    }

    public Role getRole(final String roleId) {
        return this.roleDao.findOne(roleId);
    }
}
