package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.RoleDao;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.WriteRole;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by oliver on 1/06/16.
 */
public class RoleService implements Service {

    private final static RoleService INSTANCE = new RoleService();
    private final RoleDao roleDao = RoleDao.getInstance();
    private final RoleConverter roleConverter = new RoleConverter();

    private RoleService() {
        super();
    }

    public static RoleService getInstance() {
        return INSTANCE;
    }

    public void addRole(Role role) {
        this.roleDao.insert(role);
    }

    public void createRoles(List<Role> roles) {
        roles.forEach(role -> this.roleDao.insert(role));
    }

    public Collection<Role> getRoles() {
        return this.roleDao.elements();
    }

    public void removeRole(final String id) {
        this.roleDao.delete(id);
    }

    public Role getRole(final String roleId) {
        return this.roleDao.findOne(roleId);
    }


    public List<ReadRole> getReadRoles() {
        Collection<Role> roles = this.getRoles();
        if(roles == null){
            return null;
        }
        return roles.stream().map(r -> roleConverter.entityToReadDTO(r)).collect(Collectors.toList());
    }

    public ReadRole getReadRole(String roleId) {
        Role roleEntity = this.getRole(roleId);
        if( roleEntity == null){
            return null;
        }
        return  roleConverter.entityToReadDTO(roleEntity);
    }

    public void addWriteRole(WriteRole role) {
        Role roleEntity = roleConverter.writeDTOtoEntity(role);
        this.addRole(roleEntity);
    }
}
