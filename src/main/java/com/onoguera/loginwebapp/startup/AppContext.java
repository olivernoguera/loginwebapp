package com.onoguera.loginwebapp.startup;

import com.onoguera.loginwebapp.dao.RoleDao;
import com.onoguera.loginwebapp.dao.UserDao;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.service.UserService;

/**
 * Created by olivernoguera on 23/04/2017.
 */
public final class AppContext {


    /**
     * Protect singletton
     */
    private AppContext(){}


    public static void startContext(){
        RoleService roleService = RoleService.getInstance();
        roleService.setRoleDao(RoleDao.getInstance());

        UserService userService = UserService.getInstance();
        userService.setUserDao(UserDao.getInstance());
    }

}
