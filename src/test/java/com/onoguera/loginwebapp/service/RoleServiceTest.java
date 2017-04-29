package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.Dao;
import com.onoguera.loginwebapp.dao.GenericDao;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.WriteRole;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 23/04/2017.
 */
public class RoleServiceTest {

    private static RoleService roleService = RoleService.getInstance();

    private static class MockRoleDao extends GenericDao<Role>
            implements Dao<Role> {
    }

    @Before
    public void beforeTest() throws Exception {
        roleService.setRoleDao(new MockRoleDao());
    }

    private static List<ReadRole> convertReadCollectionToListOrdered(Collection<ReadRole> rolesList) {
        List<ReadRole> roles = rolesList.stream().collect(Collectors.toList());
        return orderedReadRoleList(roles);
    }

    private static List<ReadRole> orderedReadRoleList(List<ReadRole> roles) {
        return roles.stream().sorted((l1, l2) -> (l1.getRole().compareTo(l2.getRole()))).collect(Collectors.toList());
    }

    private static List<Role> convertCollectionToListOrdered(Collection<Role> rolesList) {
        List<Role> roles = rolesList.stream().collect(Collectors.toList());
        return orderedRoleList(roles);
    }

    private static List<Role> orderedRoleList(List<Role> roles) {
        return roles.stream().sorted((l1, l2) -> (l1.getId().compareTo(l2.getId()))).collect(Collectors.toList());
    }

    @Test
    public void createRolesTest() {


        Role role = new Role("test1");
        Role role2 = new Role("test2");


        roleService.addRole(role);
        Assert.assertThat("RoleServiceTest createRolesTest addRole",
                roleService.getRole(role.getId()), is(role));
        Assert.assertThat("RoleServiceTest createRolesTest getRoles",
                convertCollectionToListOrdered(roleService.getRoles()), is(orderedRoleList(Arrays.asList(role))));

        roleService.addRole(role2);
        Assert.assertThat("RoleServiceTest createRolesTest addRole2",
                roleService.getRole(role2.getId()), is(role2));
        Assert.assertThat("RoleServiceTest createRolesTest getRoles",
                convertCollectionToListOrdered(roleService.getRoles()), is(orderedRoleList(Arrays.asList(role, role2))));

        roleService.removeRole(role.getId());
        Assert.assertThat("RoleServiceTest createRolesTest removeRole1",
                roleService.getRole(role2.getId()), is(role2));
        Assert.assertThat("RoleServiceTest createRolesTest getRoles2",
                convertCollectionToListOrdered(roleService.getRoles()), is(Arrays.asList(role2)));

        roleService.createRoles(new ArrayList<>());
        Assert.assertThat("RoleServiceTest createRolesTest createEmptyRoles",
                convertCollectionToListOrdered(roleService.getRoles()), is(Arrays.asList(role2)));

        roleService.createRoles(Arrays.asList(role, role2));
        Assert.assertThat("RoleServiceTest createRolesTest createRoles",
                convertCollectionToListOrdered(roleService.getRoles()), is(orderedRoleList(Arrays.asList(role, role2))));
    }


    @Test
    public void readWriteRolesTest() {

        Role role = new Role("test1");
        WriteRole writeRole = new WriteRole("test2");

        roleService.addRole(role);
        Assert.assertThat("RoleServiceTest readWriteRolesTest addRole",
                roleService.getRole(role.getId()), is(role));
        Assert.assertThat("RoleServiceTest readWriteRolesTest getRoles",
                convertCollectionToListOrdered(roleService.getRoles()), is(orderedRoleList(Arrays.asList(role))));
        Assert.assertThat("RoleServiceTest readWriteRolesTest getReadRole",
               roleService.getReadRole(role.getId()), is(RoleConverter.getInstance().entityToReadDTO(role)));
        Assert.assertThat("RoleServiceTest readWriteRolesTest getReadRoles",
                convertReadCollectionToListOrdered(roleService.getReadRoles()),
                is(orderedReadRoleList(Arrays.asList(RoleConverter.getInstance().entityToReadDTO(role)))));

        roleService.addWriteRole(writeRole);
        Role role2 = RoleConverter.getInstance().writeDTOtoEntity(writeRole);
        Assert.assertThat("RoleServiceTest createRolesTest addRole2",
                roleService.getRole(writeRole.getRole()), is(role2));
        Assert.assertThat("RoleServiceTest createRolesTest getRoles",
                convertCollectionToListOrdered(roleService.getRoles()), is(orderedRoleList(Arrays.asList(role, role2))));
    }

    @Test
    public void readBadRoles() {

        Assert.assertThat("RoleServiceTest readBadRoles getReadRole that not exists",
                roleService.getReadRole("15"), is(nullValue()));
        Assert.assertThat("RoleServiceTest readBadRoles getReadRoles null",
                roleService.getReadRoles(), is(new ArrayList<>()));

    }
}