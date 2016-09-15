package com.onoguera.loginwebapp.integration;

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

    private final static ControllerContainer INSTANCE = new ControllerContainer();


    private ControllerContainer() {
        UserServiceInterface userService = UserService.getInstance();
        SessionServiceInterface sessionServiceInterface = SessionService.getInstance();

        UserControllerRest userController = new UserControllerRest();
        RoleControllerRest roleController = new RoleControllerRest();

        LoginController loginController = new LoginController();
        loginController.setUserService(userService);
        loginController.setSessionService(sessionServiceInterface);

        PageController pageController = new PageController();
        pageController.setSessionService(sessionServiceInterface);

        LogoutController logoutController = new LogoutController();
        logoutController.setSessionService(sessionServiceInterface);

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
