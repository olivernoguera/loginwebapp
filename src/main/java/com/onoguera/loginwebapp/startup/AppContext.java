package com.onoguera.loginwebapp.startup;

import com.onoguera.loginwebapp.dao.RoleDao;
import com.onoguera.loginwebapp.dao.SessionDao;
import com.onoguera.loginwebapp.dao.UserDao;
import com.onoguera.loginwebapp.service.PageApiRoleService;
import com.onoguera.loginwebapp.service.BrowserSessionService;
import com.onoguera.loginwebapp.service.AuthorizationService;

/**
 * Created by olivernoguera on 23/04/2017.
 */
public final class AppContext {


    /**
     * Protect singletton
     */
    private AppContext(){}


    public static void startContext(int timeSessionMiliseconds){

        PageApiRoleService pageApiRoleService = PageApiRoleService.getInstance();
        pageApiRoleService.setRoleDao(RoleDao.getInstance());

        AuthorizationService authorizationService = AuthorizationService.getInstance();
        authorizationService.setUserDao(UserDao.getInstance());
        authorizationService.setRoleService(pageApiRoleService);

        BrowserSessionService browserSessionService = BrowserSessionService.getInstance();
        browserSessionService.setSessionDao(SessionDao.getInstance().getInstance());
        browserSessionService.setPeriodTimeToExpiredSession(timeSessionMiliseconds);
    }

}
