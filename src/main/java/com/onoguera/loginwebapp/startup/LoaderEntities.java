package com.onoguera.loginwebapp.startup;


import com.onoguera.loginwebapp.dao.UserDao;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by olivernoguera on 04/06/2016.
 *
 */
public final class LoaderEntities {


    private static final String USER_PREFIX = "USER_";
    private static final String PASSWORD_PREFIX = "PASS_";
    private static final String ROLE_PREFIX = "PAGE_";


    public static void loadEntities() {
        UserService userService = UserService.getInstance();
        UserDao userDao = UserDao.getInstance();
        userService.setUserDao(userDao);

        RoleService roleService = RoleService.getInstance();

        List<User> users = LoaderEntities.createUsers(3);
        userService.createUsers(users);

        List<Role> roles = LoaderEntities.createRoles(3);
        roleService.createRoles(roles);

        User adminUser = new User("ADMIN", "ADMIN");
        adminUser.addRoles(roles);
        adminUser.addRole(RoleService.API_ROLE);
        adminUser.addRole(RoleService.WRITER_API_ROLE);

        roleService.createRoles(Arrays.asList(RoleService.API_ROLE, RoleService.WRITER_API_ROLE));

        userService.addUser(adminUser);


    }

    private static List<User> createUsers(Integer numUsers) {
        List<User> users = new ArrayList<>();

        for (int i = 1; i <= numUsers; i++) {
            User user = new User(USER_PREFIX + i, PASSWORD_PREFIX + i);
            Role role = new Role(ROLE_PREFIX + i);
            user.addRole(role);
            user.addRole(RoleService.API_ROLE);
            users.add(user);
        }
        return users;
    }

    private static List<Role> createRoles(Integer numRoles) {
        List<Role> roles = new ArrayList<>();

        for (int i = 1; i <= numRoles; i++) {
            Role role = new Role(ROLE_PREFIX + i);
            roles.add(role);
        }
        return roles;
    }


}
