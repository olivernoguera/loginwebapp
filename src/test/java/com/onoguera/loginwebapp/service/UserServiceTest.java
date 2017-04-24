package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.Dao;
import com.onoguera.loginwebapp.dao.GenericDao;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;
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
public class UserServiceTest {

    private static UserService userService = UserService.getInstance();

    private static class MockUserDao extends GenericDao<User>
            implements Dao<User> {

    }

    @Before
    public void beforeTest() throws Exception {
        userService.setUserDao(new MockUserDao());
    }

    private static List<ReadUser> convertReadCollectionToListOrdered(Collection<ReadUser> UsersList) {
        List<ReadUser> Users = UsersList.stream().collect(Collectors.toList());
        return orderedReadUserList(Users);
    }

    private static List<ReadUser> orderedReadUserList(List<ReadUser> Users) {
        return Users.stream().sorted((l1, l2) -> (l1.getUsername().compareTo(l2.getUsername()))).collect(Collectors.toList());
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


        userService.addUser(user);
        Assert.assertThat("UserServiceTest createUsersTest addUser",
                userService.getUser(user.getId()), is(user));
        Assert.assertThat("UserServiceTest createUsersTest getUsers",
                convertCollectionToListOrdered(userService.getUsers()), is(orderedUserList(Arrays.asList(user))));

        userService.addUser(user2);
        Assert.assertThat("UserServiceTest createUsersTest addUser2",
                userService.getUser(user2.getId()), is(user2));
        Assert.assertThat("UserServiceTest createUsersTest getUsers",
                convertCollectionToListOrdered(userService.getUsers()), is(orderedUserList(Arrays.asList(user, user2))));

        userService.removeUser(user.getId());
        Assert.assertThat("UserServiceTest createUsersTest removeUser1",
                userService.getUser(user2.getId()), is(user2));
        Assert.assertThat("UserServiceTest createUsersTest getUsers2",
                convertCollectionToListOrdered(userService.getUsers()), is(Arrays.asList(user2)));

        userService.createUsers(new ArrayList<>());
        Assert.assertThat("UserServiceTest createUsersTest createEmptyUsers",
                convertCollectionToListOrdered(userService.getUsers()), is(Arrays.asList(user2)));

        userService.createUsers(Arrays.asList(user, user2));
        Assert.assertThat("UserServiceTest createUsersTest createUsers",
                convertCollectionToListOrdered(userService.getUsers()), is(orderedUserList(Arrays.asList(user, user2))));
    }


    @Test
    public void readWriteUsersTest() {

        User user = new User("test1","passw1");


        userService.addUser(user);
        Assert.assertThat("UserServiceTest readWriteUsersTest addUser",
                userService.getUser(user.getId()), is(user));
        Assert.assertThat("UserServiceTest readWriteUsersTest getUsers",
                convertCollectionToListOrdered(userService.getUsers()), is(orderedUserList(Arrays.asList(user))));
        Assert.assertThat("UserServiceTest readWriteUsersTest getReadUser",
                userService.getReadUser(user.getId()), is(UserConverter.getInstance().entityToReadDTO(user)));
        Assert.assertThat("UserServiceTest readWriteUsersTest getReadUsers",
                convertReadCollectionToListOrdered(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(UserConverter.getInstance().entityToReadDTO(user)))));
        userService.removeAllUsers();
        Assert.assertThat("UserServiceTest readWriteUsersTest removeAllUsers",
                convertReadCollectionToListOrdered(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList())));

    }

    @Test
    public void readBadUsersTest() {

        Assert.assertThat("UserServiceTest readBadUsers getReadUser that not exists",
                userService.getReadUser("15"), is(nullValue()));
        Assert.assertThat("UserServiceTest readBadUsers getReadUser that not exists",
                userService.getWriteUser("15"), is(nullValue()));
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


        userService.createWriteUsers(Arrays.asList(writeUser));
        Assert.assertThat("UserServiceTest createWriteUsersTest addUser",
                userService.getUser(user1.getId()), is(user1));
        Assert.assertThat("UserServiceTest createWriteUsersTest getUsers",
                convertCollectionToListOrdered(userService.getUsers()), is(orderedUserList(Arrays.asList(user1))));
        Assert.assertThat("UserServiceTest createWriteUsersTest getReadUser",
                userService.getReadUser(user1.getId()), is(UserConverter.getInstance().entityToReadDTO(user1)));
        Assert.assertThat("UserServiceTest createWriteUsersTest getReadUsers",
                convertReadCollectionToListOrdered(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(UserConverter.getInstance().entityToReadDTO(user1)))));

        userService.updateWriteUser(writeUser2);
        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser getUser",
                userService.getUser(user2.getId()), is(user2));
        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser getUsers",
                convertCollectionToListOrdered(userService.getUsers()), is(orderedUserList(Arrays.asList(user2))));
        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser",
                userService.getReadUser(user2.getId()), is(UserConverter.getInstance().entityToReadDTO(user2)));
        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser getReadUsers",
                convertReadCollectionToListOrdered(userService.getReadUsers()),
                is(orderedReadUserList(Arrays.asList(UserConverter.getInstance().entityToReadDTO(user2)))));
        Assert.assertThat("UserServiceTest createWriteUsersTest updateWriteUser getWriteUser",
                userService.getWriteUser(user2.getId()), is(writeUser2));

    }

    @Test
    public void validateUserTest() {
        User user = new User("mockUser","mockPassword");
        userService.createUsers(Arrays.asList(user));
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