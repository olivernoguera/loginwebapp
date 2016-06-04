package com.onoguera.loginwebapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by oliver on 4/06/16.
 */
public class ControllerContainer {

    private List<Controller> controllers = new ArrayList<>();

    private final static ControllerContainer INSTANCE = new ControllerContainer();


    private ControllerContainer(){

        UserController userController = new UserController();
        controllers.add(userController);
    }

    public static ControllerContainer getInstance(){
        return INSTANCE;
    }

    public Optional<Controller> findController( String path )
    {
        Optional<Controller> controller =
        controllers.stream().
                filter(c -> c.filter(path)).
                findFirst();
        return controller;

    }

}
