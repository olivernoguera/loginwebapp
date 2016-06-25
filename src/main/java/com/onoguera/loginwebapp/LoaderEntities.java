package com.onoguera.loginwebapp;


import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivernoguera on 04/06/2016.
 *
 */
public class LoaderEntities {


    private static final String USER_PREFIX = "USER_";
    private static final String PASSWORD_PREFIX = "PASS_";
    private static final String ROLE_PREFIX = "PAGE_";

    public LoaderEntities() {
    }

    public void loadEntities() {
        UserService userService = UserService.getInstance();
        RoleService roleService = RoleService.getInstance();

        List<User> users = LoaderEntities.createUsers(3);
        userService.createUsers(users);

        List<Role> roles = LoaderEntities.createRoles(3);
        roleService.createRoles(roles);

        Role adminRole = new Role("ADMIN", true);
        roleService.addRole(adminRole);

        User adminUser = new User("ADMIN", "ADMIN");
        adminUser.addRole(adminRole);
        userService.addUser(adminUser);


    }

    private static List<User> createUsers(Integer numUsers) {
        List<User> users = new ArrayList<>();

        for (int i = 1; i <= numUsers; i++) {
            User user = new User(USER_PREFIX + i, PASSWORD_PREFIX + i);
            Role role = new Role(ROLE_PREFIX + i);
            user.addRole(role);
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
