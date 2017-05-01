package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.WriteRole;

import java.util.Collection;
import java.util.List;

/**
 * Created by olivernoguera on 30/04/2017.
 */
public interface RoleServiceInterface {

    Role getRole(String roleId);

    Collection<ReadRole> getReadRoles();

    ReadRole getReadRole(String roleId);

    void addWriteRole(WriteRole role);

    void removeRole(String roleId);

    boolean existsRoles(List<Role> roles);
}
