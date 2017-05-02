package com.onoguera.loginwebapp.startup;


import com.onoguera.loginwebapp.dao.UserDao;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.service.PageApiRoleService;
import com.onoguera.loginwebapp.service.AuthorizationService;

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

        AuthorizationService authorizationService = AuthorizationService.getInstance();
        UserDao userDao = UserDao.getInstance();
        authorizationService.setUserDao(userDao);

        PageApiRoleService pageApiRoleService = PageApiRoleService.getInstance();
        LoaderEntities.createUsers(  userDao, 3);

        List<Role> roles = LoaderEntities.createRoles(3);
        pageApiRoleService.createRoles(roles);

        User adminUser = new User("ADMIN", "ADMIN");
        adminUser.addRoles(roles);
        adminUser.addRole(PageApiRoleService.API_ROLE);
        adminUser.addRole(PageApiRoleService.WRITER_API_ROLE);

        pageApiRoleService.createRoles(Arrays.asList(PageApiRoleService.API_ROLE, PageApiRoleService.WRITER_API_ROLE));

        userDao.insert(adminUser);
    }

    private static void createUsers(UserDao userDao, Integer numUsers) {

        for (int i = 1; i <= numUsers; i++) {
            User user = new User(USER_PREFIX + i, PASSWORD_PREFIX + i);
            Role role = new Role(ROLE_PREFIX + i);
            user.addRole(role);
            user.addRole(PageApiRoleService.API_ROLE);
            userDao.insert(user);

        }
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
