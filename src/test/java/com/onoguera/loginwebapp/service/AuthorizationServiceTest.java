package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.Dao;
import com.onoguera.loginwebapp.dao.GenericDao;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 23/04/2017.
 */
public class AuthorizationServiceTest {


    private static final AuthorizationService AUTHORIZATION_SERVICE = AuthorizationService.getInstance();

    private static final UserConverter userConverter = UserConverter.getInstance();

    private static class MockUserDao extends GenericDao<User>
            implements Dao<User> {

    }

    private static class RoleServiceMock implements RoleService {

        private static final Role role = new Role("role1");
        private static final Role role2 = new Role("role2");
        private static final Map<String, Role> roles = new HashMap();

        public RoleServiceMock() {
            roles.put(role.getId(), role);
            roles.put(role2.getId(), role2);
        }

        @Override
        public Role getRole(String roleId) {
            return roles.get(roleId);
        }

        @Override
        public Collection<ReadRole> getReadRoles() {
            return roles.values().stream().map(r -> new ReadRole(r.getId())).collect(Collectors.toList());
        }

        @Override
        public ReadRole getReadRole(String roleId) {
            return new ReadRole(this.getRole(roleId).getId());
        }

        @Override
        public void addWriteRole(WriteRole role) {

        }

        @Override
        public void removeRole(String roleId) {

        }

        @Override
        public boolean existsRoles(List<Role> roles) {
            return roles.containsAll(roles);
        }
    }

    @Before
    public void beforeTest() throws Exception {
        AUTHORIZATION_SERVICE.setUserDao(new MockUserDao());
        AUTHORIZATION_SERVICE.setRoleService(new RoleServiceMock());
    }

    private static List<ReadUser> convertReadCollectionToListOrdered(Collection<ReadUser> UsersList) {
        List<ReadUser> Users = UsersList.stream().collect(Collectors.toList());
        return orderedReadUserList(Users);
    }

    private static List<ReadUser> orderedReadUserList(List<ReadUser> Users) {
        return Users.stream().sorted((l1, l2) ->
                (l1.getUsername().compareTo(l2.getUsername()))).collect(Collectors.toList());
    }

    private static List<User> convertCollectionToListOrdered(Collection<User> UsersList) {
        List<User> Users = UsersList.stream().collect(Collectors.toList());
        return orderedUserList(Users);
    }

    private static List<User> orderedUserList(List<User> Users) {
        return Users.stream().sorted((l1, l2) -> (l1.getId().compareTo(l2.getId()))).collect(Collectors.toList());
    }

    @Test
    public void createUsersTest() {


        User user = new User("test1", "pass1");
        User user2 = new User("test2", "pass2");

        AUTHORIZATION_SERVICE.upsertUser(user);
        Assert.assertThat("UserServiceTest createUsersTest addUser",
                AUTHORIZATION_SERVICE.getUser(user.getId()), is(user));
        Assert.assertThat("UserServiceTest createUsersTest getUsers",
                convertReadCollectionToListOrdered(AUTHORIZATION_SERVICE.getReadUsers())
                , is(convertReadCollectionToListOrdered(Arrays.asList(userConverter.entityToReadDTO(user)))));

        AUTHORIZATION_SERVICE.upsertUser(user2);
        Assert.assertThat("UserServiceTest createUsersTest addUser2",
                AUTHORIZATION_SERVICE.getUser(user2.getId()), is(user2));
        Assert.assertThat("UserServiceTest createUsersTest getUsers",
                orderedReadUserList(AUTHORIZATION_SERVICE.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(userConverter.entityToReadDTO(user),
                        userConverter.entityToReadDTO(user2)))));

        AUTHORIZATION_SERVICE.removeUser(user.getId());
        Assert.assertThat("UserServiceTest createUsersTest removeUser1",
                AUTHORIZATION_SERVICE.getUser(user2.getId()), is(user2));
        Assert.assertThat("UserServiceTest createUsersTest getUsers2",
                orderedReadUserList(AUTHORIZATION_SERVICE.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(userConverter.entityToReadDTO(user2)))));

        AUTHORIZATION_SERVICE.setUsers(new ArrayList<>());
        Assert.assertThat("UserServiceTest createUsersTest createEmptyUsers",
                orderedReadUserList(AUTHORIZATION_SERVICE.getReadUsers()),
                is(orderedReadUserList(Arrays.asList())));

        AUTHORIZATION_SERVICE.setUsers(Arrays.asList(userConverter.entityToWriteDTO(user),
                userConverter.entityToWriteDTO(user2)));
        Assert.assertThat("UserServiceTest createUsersTest createUsers",
                orderedReadUserList(AUTHORIZATION_SERVICE.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(userConverter.entityToReadDTO(user),
                        userConverter.entityToReadDTO(user2)))));
    }


    @Test
    public void readWriteUsersTest() {

        User user = new User("test1", "passw1");


        AUTHORIZATION_SERVICE.upsertUser(user);
        Assert.assertThat("UserServiceTest readWriteUsersTest addUser",
                AUTHORIZATION_SERVICE.getUser(user.getId()), is(user));
        Assert.assertThat("UserServiceTest readWriteUsersTest getUsers",
                orderedReadUserList(AUTHORIZATION_SERVICE.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(userConverter.entityToReadDTO(user)))));
        Assert.assertThat("UserServiceTest readWriteUsersTest getReadUser",
                AUTHORIZATION_SERVICE.getReadUser(user.getId()), is(UserConverter.getInstance().entityToReadDTO(user)));
        Assert.assertThat("UserServiceTest readWriteUsersTest getReadUsers",
                convertReadCollectionToListOrdered(AUTHORIZATION_SERVICE.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(UserConverter.getInstance().entityToReadDTO(user)))));
        AUTHORIZATION_SERVICE.removeUsers();
        Assert.assertThat("UserServiceTest readWriteUsersTest removeAllUsers",
                convertReadCollectionToListOrdered(AUTHORIZATION_SERVICE.getReadUsers()),
                is(orderedReadUserList(Arrays.asList())));

    }

    @Test
    public void readBadUsersTest() {

        Assert.assertThat("UserServiceTest readBadUsers getReadUser that not exists",
                AUTHORIZATION_SERVICE.getReadUser("15"), is(nullValue()));
        Assert.assertThat("UserServiceTest readBadUsers getReadUser that not exists",
                AUTHORIZATION_SERVICE.getReadUser("15"), is(nullValue()));
        Assert.assertThat("UserServiceTest readBadUsers getReadUsers null",
                AUTHORIZATION_SERVICE.getReadUsers(), is(new ArrayList<>()));

    }

    @Test
    public void createWriteUsersTest() {

        Role role = new Role("role1");
        Role role2 = new Role("role2");
        User user1 = new User("test1", "passw2", Arrays.asList(role));
        User user2 = new User("test1", "passw3", Arrays.asList(role, role2));

        WriteRole writeRole = new WriteRole("role1");
        WriteRole writeRole2 = new WriteRole("role2");
        WriteUser writeUser = new WriteUser("test1", "passw2", Arrays.asList(writeRole));
        WriteUser writeUser2 = new WriteUser("test1", "pass3", Arrays.asList(writeRole, writeRole2));


        AUTHORIZATION_SERVICE.setUsers(Arrays.asList(writeUser));
        Assert.assertThat("UserServiceTest createWriteUsersTest addUser",
                AUTHORIZATION_SERVICE.getUser(user1.getId()), is(user1));
        Assert.assertThat("UserServiceTest createWriteUsersTest getReadUser",
                AUTHORIZATION_SERVICE.getReadUser(user1.getId()), is(UserConverter.getInstance().entityToReadDTO(user1)));
        Assert.assertThat("UserServiceTest createWriteUsersTest getReadUsers",
                convertReadCollectionToListOrdered(AUTHORIZATION_SERVICE.getReadUsers()),
                is(convertReadCollectionToListOrdered(Arrays.asList(userConverter.entityToReadDTO(user1)))));

        AUTHORIZATION_SERVICE.setUsers(Arrays.asList(writeUser2));
        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser getUser",
                AUTHORIZATION_SERVICE.getUser(user2.getId()), is(user2));

        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser",
                AUTHORIZATION_SERVICE.getReadUser(user2.getId()), is(UserConverter.getInstance().entityToReadDTO(user2)));
        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser getReadUsers",
                convertReadCollectionToListOrdered(AUTHORIZATION_SERVICE.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(UserConverter.getInstance().entityToReadDTO(user2)))));


    }

    @Test
    public void validateUserTest() {
        User user = new User("mockUser", "mockPassword");
        AUTHORIZATION_SERVICE.upsertUser(user);
        User userNotCreated = new User("mockUser1", "mockPassword");
        User userWithNullPassword = new User("mockUser1", null);
        User userWithBadPassword = new User("mockUser", "mockBadPassword");

        Assert.assertThat("UserServiceTest validateUserTest validateUser exist user",
                AUTHORIZATION_SERVICE.validateUser(user), is(user));
        Assert.assertThat("UserServiceTest validateUserTest validateUser that not exists",
                AUTHORIZATION_SERVICE.validateUser(userNotCreated), is(nullValue()));
        Assert.assertThat("UserServiceTest validateUserTest validateUser user with null password",
                AUTHORIZATION_SERVICE.validateUser(userWithNullPassword), is(nullValue()));
        Assert.assertThat("UserServiceTest validateUserTest validateUser user with bad password",
                AUTHORIZATION_SERVICE.validateUser(userWithBadPassword), is(nullValue()));
    }

    @Test
    public void upsertRolesOfUserTest() {

        User user = new User("mockUser", null);
        AUTHORIZATION_SERVICE.upsertUser(user);
        boolean result = AUTHORIZATION_SERVICE.upsertRolesOfUser(user.getId(),new ArrayList<>());

        Assert.assertThat("UserServiceTest upsertRolesOfUserTest upsertRolesOfUser password is null",
                result, is(Boolean.FALSE));

        result = AUTHORIZATION_SERVICE.upsertRolesOfUser("badusers",null);
        Assert.assertThat("UserServiceTest upsertRolesOfUserTest upsertRolesOfUser user not exists",
                result, is(Boolean.FALSE));

        result = AUTHORIZATION_SERVICE.upsertRolesOfUser(user.getId(),Arrays.asList(new WriteRole("BADROLE")));
        Assert.assertThat("UserServiceTest upsertRolesOfUserTest upsertRolesOfUser role not exists",
                result, is(Boolean.FALSE));

        user = new User("mockUser", "password");
        AUTHORIZATION_SERVICE.upsertUser(user);
        result = AUTHORIZATION_SERVICE.upsertRolesOfUser(user.getId(),Arrays.asList(new WriteRole("role1")));
        Assert.assertThat("UserServiceTest upsertRolesOfUserTest upsertRolesOfUser role not exists",
                result, is(Boolean.TRUE));
        user.setRoles(Arrays.asList(new Role("role1")));
        Assert.assertThat("UserServiceTest upsertRolesOfUserTest upsertRolesOfUser roles well updated",
                AUTHORIZATION_SERVICE.getUser(user.getId()), is(user));
    }


    @Test
    public void upsertWriterUserTest() {

        WriteUser user = new WriteUser("mockUser2", "test", new ArrayList<>());
        boolean result = AUTHORIZATION_SERVICE.upsertUser(user);

        Assert.assertThat("UserServiceTest upsertRolesOfUserTest upsertWriterUserTest good result",
                result, is(Boolean.TRUE));

        Assert.assertThat("UserServiceTest upsertRolesOfUserTest upsertWriterUserTest well saved user",
                AUTHORIZATION_SERVICE.getUser(user.getUsername()), is(userConverter.writeDTOtoEntity(user)));
    }
}