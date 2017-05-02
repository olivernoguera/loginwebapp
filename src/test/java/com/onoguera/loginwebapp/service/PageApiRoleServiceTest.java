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
public class PageApiRoleServiceTest {

    private static PageApiRoleService pageApiRoleService = PageApiRoleService.getInstance();

    private static class MockRoleDao extends GenericDao<Role>
            implements Dao<Role> {
    }

    @Before
    public void beforeTest() throws Exception {
        pageApiRoleService.setRoleDao(new MockRoleDao());
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


        pageApiRoleService.addRole(role);
        Assert.assertThat("RoleServiceTest createRolesTest addRole",
                pageApiRoleService.getRole(role.getId()), is(role));
        Assert.assertThat("RoleServiceTest createRolesTest getRoles",
                convertCollectionToListOrdered(pageApiRoleService.getRoles()), is(orderedRoleList(Arrays.asList(role))));

        pageApiRoleService.addRole(role2);
        Assert.assertThat("RoleServiceTest createRolesTest addRole2",
                pageApiRoleService.getRole(role2.getId()), is(role2));
        Assert.assertThat("RoleServiceTest createRolesTest getRoles",
                convertCollectionToListOrdered(pageApiRoleService.getRoles()), is(orderedRoleList(Arrays.asList(role, role2))));

        pageApiRoleService.removeRole(role.getId());
        Assert.assertThat("RoleServiceTest createRolesTest removeRole1",
                pageApiRoleService.getRole(role2.getId()), is(role2));
        Assert.assertThat("RoleServiceTest createRolesTest getRoles2",
                convertCollectionToListOrdered(pageApiRoleService.getRoles()), is(Arrays.asList(role2)));

        pageApiRoleService.createRoles(new ArrayList<>());
        Assert.assertThat("RoleServiceTest createRolesTest createEmptyRoles",
                convertCollectionToListOrdered(pageApiRoleService.getRoles()), is(Arrays.asList(role2)));

        pageApiRoleService.createRoles(Arrays.asList(role, role2));
        Assert.assertThat("RoleServiceTest createRolesTest createRoles",
                convertCollectionToListOrdered(pageApiRoleService.getRoles()), is(orderedRoleList(Arrays.asList(role, role2))));
    }


    @Test
    public void readWriteRolesTest() {

        Role role = new Role("test1");
        WriteRole writeRole = new WriteRole("test2");

        pageApiRoleService.addRole(role);
        Assert.assertThat("RoleServiceTest readWriteRolesTest addRole",
                pageApiRoleService.getRole(role.getId()), is(role));
        Assert.assertThat("RoleServiceTest readWriteRolesTest getRoles",
                convertCollectionToListOrdered(pageApiRoleService.getRoles()), is(orderedRoleList(Arrays.asList(role))));
        Assert.assertThat("RoleServiceTest readWriteRolesTest getReadRole",
               pageApiRoleService.getReadRole(role.getId()), is(RoleConverter.getInstance().entityToReadDTO(role)));
        Assert.assertThat("RoleServiceTest readWriteRolesTest getReadRoles",
                convertReadCollectionToListOrdered(pageApiRoleService.getReadRoles()),
                is(orderedReadRoleList(Arrays.asList(RoleConverter.getInstance().entityToReadDTO(role)))));

        pageApiRoleService.addWriteRole(writeRole);
        Role role2 = RoleConverter.getInstance().writeDTOtoEntity(writeRole);
        Assert.assertThat("RoleServiceTest createRolesTest addRole2",
                pageApiRoleService.getRole(writeRole.getRole()), is(role2));
        Assert.assertThat("RoleServiceTest createRolesTest getRoles",
                convertCollectionToListOrdered(pageApiRoleService.getRoles()), is(orderedRoleList(Arrays.asList(role, role2))));
    }

    @Test
    public void readBadRolesTest() {

        Assert.assertThat("RoleServiceTest readBadRolesTest getReadRole that not exists",
                pageApiRoleService.getReadRole("15"), is(nullValue()));
        Assert.assertThat("RoleServiceTest readBadRolesTest getReadRoles null",
                pageApiRoleService.getReadRoles(), is(new ArrayList<>()));

    }

    @Test
    public void existsRolesTest() {

        Role mockRole = new Role("role1");
        Role mockRole2 = new Role("role2");

        Assert.assertThat("RoleServiceTest existsRolesTest empty list",
                pageApiRoleService.existsRoles(new ArrayList<>()), is(Boolean.FALSE));
        Assert.assertThat("RoleServiceTest existsRolesTest role not exists",
                pageApiRoleService.existsRoles(Arrays.asList(mockRole)), is(Boolean.FALSE));

        pageApiRoleService.addRole(mockRole);
        Assert.assertThat("RoleServiceTest existsRolesTest role exists",
                pageApiRoleService.existsRoles(Arrays.asList(mockRole)), is(Boolean.TRUE));

        Assert.assertThat("RoleServiceTest existsRolesTest two roles one not exists",
                pageApiRoleService.existsRoles(Arrays.asList(mockRole,mockRole2)), is(Boolean.FALSE));

        pageApiRoleService.addRole(mockRole2);
        Assert.assertThat("RoleServiceTest existsRolesTest two roles exist",
                pageApiRoleService.existsRoles(Arrays.asList(mockRole,mockRole2)), is(Boolean.TRUE));
    }
}