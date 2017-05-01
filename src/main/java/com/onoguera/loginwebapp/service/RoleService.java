package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.Dao;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.WriteRole;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by oliver on 1/06/16.
 *
 */
public class RoleService implements RoleServiceInterface {

    private final static RoleService INSTANCE = new RoleService();
    private Dao roleDao;

    public final static Role API_ROLE = new Role("API");
    public final static Role WRITER_API_ROLE = new Role("WRITER_API");

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

    @Override
    public boolean existsRoles(List<Role> roles) {
        if( roles == null || roles.isEmpty()){
            return false;
        }
        for( Role role: roles){
            if( role == null || roleDao.findOne(role.getId()) == null){
                return false;
            }
        }
        return true;
    }

    public Role getRole(final String roleId) {
        return (Role)this.roleDao.findOne(roleId);
    }


    public List<ReadRole> getReadRoles() {
        Collection<Role> roles = this.getRoles();
        return roles.stream().map(r -> RoleConverter.getInstance().entityToReadDTO(r)).collect(Collectors.toList());
    }

    public ReadRole getReadRole(String roleId) {
        Role roleEntity = this.getRole(roleId);
        if( roleEntity == null){
            return null;
        }
        return  RoleConverter.getInstance().entityToReadDTO(roleEntity);
    }

    public void addWriteRole(WriteRole role) {
        Role roleEntity = RoleConverter.getInstance().writeDTOtoEntity(role);
        this.addRole(roleEntity);
    }


    public void setRoleDao(Dao roleDao) {
        this.roleDao = roleDao;
    }
}
