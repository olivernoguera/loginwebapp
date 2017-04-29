package com.onoguera.loginwebapp.startup;

import com.onoguera.loginwebapp.controller.Controller;
import com.onoguera.loginwebapp.controller.LoginController;
import com.onoguera.loginwebapp.controller.LogoutController;
import com.onoguera.loginwebapp.controller.PageController;
import com.onoguera.loginwebapp.restcontroller.RoleControllerRest;
import com.onoguera.loginwebapp.restcontroller.UserControllerRest;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.service.SessionService;
import com.onoguera.loginwebapp.service.SessionServiceInterface;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.service.UserServiceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by oliver on 4/06/16.
 *
 */
public class ControllerContainer {

    private List<Controller> controllers = new ArrayList<>();

    private static final ControllerContainer INSTANCE = new ControllerContainer();


    private ControllerContainer() {

        UserServiceInterface userService = UserService.getInstance();
        SessionServiceInterface sessionService = SessionService.getInstance();
        RoleService roleService = RoleService.getInstance();

        UserControllerRest userController = new UserControllerRest(userService,roleService);
        RoleControllerRest roleController = new RoleControllerRest(userService,roleService);

        LoginController loginController = new LoginController(sessionService,userService);
        PageController pageController = new PageController(sessionService);
        LogoutController logoutController = new LogoutController(sessionService);

        controllers.add(userController);
        controllers.add(roleController);
        controllers.add(loginController);
        controllers.add(pageController);
        controllers.add(logoutController);

    }

    public static ControllerContainer getInstance() {
        return INSTANCE;
    }

    public Optional<Controller> findController(String path) {
        Optional<Controller> controller =
                controllers.stream().
                        filter(c -> c.filter(path)).
                        findFirst();
        return controller;

    }

}
