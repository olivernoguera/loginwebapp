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
public class UserServiceTest {



    private static final UserService userService = UserService.getInstance();

    private static final UserConverter userConverter = UserConverter.getInstance();

    private static class MockUserDao extends GenericDao<User>
            implements Dao<User> {

    }

    private static class RoleServiceMock implements RoleServiceInterface {

        private static final Role role = new Role("role1");
        private static final Role role2 = new Role("role2");
        private static final Map<String,Role> roles = new HashMap();

        public RoleServiceMock(){
            roles.put(role.getId(),role);
            roles.put(role2.getId(),role2);
        }

        @Override
        public Role getRole(String roleId) {
            return roles.get(roleId);
        }

        @Override
        public Collection<ReadRole> getReadRoles() {
            return roles.values().stream().map(r-> new ReadRole(r.getId())).collect(Collectors.toList());
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
        userService.setUserDao(new MockUserDao());
        userService.setRoleService(new RoleServiceMock());
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


        User user = new User("test1","pass1");
        User user2 = new User("test2", "pass2");

        userService.upsertUser(user);
        Assert.assertThat("UserServiceTest createUsersTest addUser",
                userService.getUser(user.getId()), is(user));
        Assert.assertThat("UserServiceTest createUsersTest getUsers",
                convertReadCollectionToListOrdered(userService.getReadUsers())
                , is(convertReadCollectionToListOrdered(Arrays.asList(userConverter.entityToReadDTO(user)))));

        userService.upsertUser(user2);
        Assert.assertThat("UserServiceTest createUsersTest addUser2",
                userService.getUser(user2.getId()), is(user2));
        Assert.assertThat("UserServiceTest createUsersTest getUsers",
                orderedReadUserList(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(userConverter.entityToReadDTO(user),
                        userConverter.entityToReadDTO(user2)))));

        userService.removeUser(user.getId());
        Assert.assertThat("UserServiceTest createUsersTest removeUser1",
                userService.getUser(user2.getId()), is(user2));
        Assert.assertThat("UserServiceTest createUsersTest getUsers2",
                orderedReadUserList(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(userConverter.entityToReadDTO(user2)))));

        userService.setUsers(new ArrayList<>());
        Assert.assertThat("UserServiceTest createUsersTest createEmptyUsers",
                orderedReadUserList(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList())));

        userService.setUsers(Arrays.asList(userConverter.entityToWriteDTO(user),
                userConverter.entityToWriteDTO(user2)));
        Assert.assertThat("UserServiceTest createUsersTest createUsers",
                orderedReadUserList(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(userConverter.entityToReadDTO(user),
                        userConverter.entityToReadDTO(user2)))));
    }


    @Test
    public void readWriteUsersTest() {

        User user = new User("test1","passw1");


        userService.upsertUser(user);
        Assert.assertThat("UserServiceTest readWriteUsersTest addUser",
                userService.getUser(user.getId()), is(user));
        Assert.assertThat("UserServiceTest readWriteUsersTest getUsers",
                orderedReadUserList(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(userConverter.entityToReadDTO(user)))));
        Assert.assertThat("UserServiceTest readWriteUsersTest getReadUser",
                userService.getReadUser(user.getId()), is(UserConverter.getInstance().entityToReadDTO(user)));
        Assert.assertThat("UserServiceTest readWriteUsersTest getReadUsers",
                convertReadCollectionToListOrdered(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(UserConverter.getInstance().entityToReadDTO(user)))));
        userService.removeUsers();
        Assert.assertThat("UserServiceTest readWriteUsersTest removeAllUsers",
                convertReadCollectionToListOrdered(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList())));

    }

    @Test
    public void readBadUsersTest() {

        Assert.assertThat("UserServiceTest readBadUsers getReadUser that not exists",
                userService.getReadUser("15"), is(nullValue()));
        Assert.assertThat("UserServiceTest readBadUsers getReadUser that not exists",
                userService.getReadUser("15"), is(nullValue()));
        Assert.assertThat("UserServiceTest readBadUsers getReadUsers null",
                userService.getReadUsers(), is(new ArrayList<>()));

    }

    @Test
    public void createWriteUsersTest() {

        Role role = new Role("role1");
        Role role2 = new Role("role2");
        User user1 = new User("test1","passw2",  Arrays.asList(role));
        User user2 = new User("test1","passw3",  Arrays.asList(role,role2));

        WriteRole writeRole = new WriteRole("role1");
        WriteRole writeRole2 = new WriteRole("role2");
        WriteUser writeUser = new WriteUser("test1","passw2", Arrays.asList(writeRole));
        WriteUser writeUser2 = new WriteUser("test1","pass3", Arrays.asList(writeRole,writeRole2));


        userService.setUsers(Arrays.asList(writeUser));
        Assert.assertThat("UserServiceTest createWriteUsersTest addUser",
                userService.getUser(user1.getId()), is(user1));
        Assert.assertThat("UserServiceTest createWriteUsersTest getReadUser",
                userService.getReadUser(user1.getId()), is(UserConverter.getInstance().entityToReadDTO(user1)));
        Assert.assertThat("UserServiceTest createWriteUsersTest getReadUsers",
                convertReadCollectionToListOrdered(userService.getReadUsers()),
                is(convertReadCollectionToListOrdered(Arrays.asList(userConverter.entityToReadDTO(user1)))));

        userService.setUsers(Arrays.asList(writeUser2));
        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser getUser",
                userService.getUser(user2.getId()), is(user2));

        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser",
                userService.getReadUser(user2.getId()), is(UserConverter.getInstance().entityToReadDTO(user2)));
        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser getReadUsers",
                convertReadCollectionToListOrdered(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(UserConverter.getInstance().entityToReadDTO(user2)))));


    }

    @Test
    public void validateUserTest() {
        User user = new User("mockUser","mockPassword");
        userService.upsertUser(user);
        User userNotCreated = new User("mockUser1","mockPassword");
        User userWithNullPassword = new User("mockUser1",null);
        User userWithBadPassword = new User("mockUser","mockBadPassword");

        Assert.assertThat("UserServiceTest validateUserTest validateUser exist user",
                userService.validateUser(user), is(user));
        Assert.assertThat("UserServiceTest validateUserTest validateUser that not exists",
                userService.validateUser(userNotCreated), is(nullValue()));
        Assert.assertThat("UserServiceTest validateUserTest validateUser user with null password",
                userService.validateUser(userWithNullPassword), is(nullValue()));
        Assert.assertThat("UserServiceTest validateUserTest validateUser user with bad password",
                userService.validateUser(userWithBadPassword), is(nullValue()));
    }


}