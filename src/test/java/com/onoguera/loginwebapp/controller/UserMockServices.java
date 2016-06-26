package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.service.UserServiceInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivernoguera on 26/06/2016.
 */
public final class UserMockServices {

    /**
     * Protect instances
     *
     */
    private UserMockServices(){}

    private final static User USER_TEST = new User("test","test");
    private final static Role ROLE_TEST1 = new Role("PAGE_1");
    private final static Role ROLE_TEST2 = new Role("PAGE_2");
    private final static Role ROLE_TEST3 = new Role("PAGE_3");

    public static class UserServiceNotValidUserMock implements UserServiceInterface{

        @Override
        public User validateUser(User user) {
            return null;
        }
    }

    public static class UserServiceWithoutRolesMock implements UserServiceInterface{

        @Override
        public User validateUser(User user) {
            user = USER_TEST;
            return user;
        }
    }

    public static class UserServicePage1Mock implements UserServiceInterface{

        @Override
        public User validateUser(User user) {
            user = USER_TEST;
            List<Role> roles = new ArrayList<>();
            roles.add(ROLE_TEST1);
            user.setRoles(roles);
            return user;
        }
    }

    public static class UserServicePage2Mock implements UserServiceInterface{

        @Override
        public User validateUser(User user) {
            user = USER_TEST;
            List<Role> roles = new ArrayList<>();
            roles.add(ROLE_TEST2);
            user.setRoles(roles);
            return user;
        }
    }

    public static class UserServicePage3Mock implements UserServiceInterface{

        @Override
        public User validateUser(User user) {
            user = USER_TEST;
            List<Role> roles = new ArrayList<>();
            roles.add(ROLE_TEST3);
            user.setRoles(roles);
            return user;
        }
    }

    public static class UserServicePagesMock implements UserServiceInterface{

        @Override
        public User validateUser(User user) {
            user = USER_TEST;
            List<Role> roles = new ArrayList<>();
            roles.add(ROLE_TEST3);
            roles.add(ROLE_TEST2);
            roles.add(ROLE_TEST1);
            user.setRoles(roles);
            return user;
        }
    }
}
