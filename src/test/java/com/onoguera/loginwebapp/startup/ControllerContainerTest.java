package com.onoguera.loginwebapp.startup;

import com.onoguera.loginwebapp.controller.Controller;
import com.onoguera.loginwebapp.controller.LoginController;
import com.onoguera.loginwebapp.controller.LogoutController;
import com.onoguera.loginwebapp.controller.PageController;
import com.onoguera.loginwebapp.restcontroller.RoleControllerRest;
import com.onoguera.loginwebapp.restcontroller.UserControllerRest;
import com.onoguera.loginwebapp.restcontroller.UserControllerRestTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class ControllerContainerTest {

    @Test
    public void getControllerByNullPath() {

        ControllerContainer controllerContainer = ControllerContainer.getInstance();
        Optional<Controller> controller = controllerContainer.findController(null);

        Assert.assertThat("ControllerContainerTest getControllerByNullPath  Not exist controller with pattern path null",
                controller.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void getControllerByEmptyPath() {

        ControllerContainer controllerContainer = ControllerContainer.getInstance();
        Optional<Controller> controller = controllerContainer.findController("");

        Assert.assertThat("ControllerContainerTest getControllerByNullPath Not exist controller with pattern path \"\"",
                controller.isPresent(), is(Boolean.FALSE));

    }

    @Test
    public void getControllerByTestPath() {

        ControllerContainer controllerContainer = ControllerContainer.getInstance();
        Optional<Controller> controller = controllerContainer.findController("");

        Assert.assertThat("ControllerContainerTest getControllerByNullPath Not exist controller with pattern path Test",
                controller.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void getControllerByUserCorrectPaths() {

        ControllerContainer controllerContainer = ControllerContainer.getInstance();

        Optional<Controller> controller = controllerContainer.findController("/users");
        Assert.assertThat("ControllerContainerTest getControllerByNullPath Not exist controller with pattern path /users",
                controller.isPresent(), is(Boolean.TRUE));

        controller = controllerContainer.findController("/users/");
        Assert.assertThat("ControllerContainerTest getControllerByNullPath Not exist controller with pattern path /users/",
                controller.isPresent(), is(Boolean.TRUE));

        controller = controllerContainer.findController("/users/51515");
        Assert.assertThat("ControllerContainerTest getControllerByNullPath Not exist controller with pattern path /users/",
                controller.isPresent(), is(Boolean.TRUE));

        controller = controllerContainer.findController("/users/r22r2");
        Assert.assertThat("ControllerContainerTest getControllerByNullPath Not exist controller with pattern path /users/",
                controller.isPresent(), is(Boolean.TRUE));


    }

    @Test
    public void loginControllerServicesTest(){

        ControllerContainer controllerContainer = ControllerContainer.getInstance();

        Optional<Controller> controller = controllerContainer.findController("/login");
        Assert.assertThat("ControllerContainerTest loginControllerServicesTest exists Controller with path /login",
                controller.isPresent(), is(Boolean.TRUE));


        Assert.assertThat("ControllerContainerTest loginControllerServicesTest Controller with path /login is ",
                controller.get(), is(instanceOf(LoginController.class)));
        LoginController loginController = (LoginController) controller.get();
        Assert.assertThat("ControllerContainerTest loginControllerServicesTest LoginController  has UserService ",
                loginController.getUserService(), is(not(nullValue())));
        Assert.assertThat("ControllerContainerTest loginControllerServicesTest LoginController  has SessionService ",
                loginController.getSessionService(), is(not(nullValue())));
    }

    @Test
    public void logoutControllerServicesTest(){

        ControllerContainer controllerContainer = ControllerContainer.getInstance();

        Optional<Controller> controller = controllerContainer.findController("/logout");
        Assert.assertThat("ControllerContainerTest logoutControllerServicesTest exists Controller with path /logout",
                controller.isPresent(), is(Boolean.TRUE));


        Assert.assertThat("ControllerContainerTest logoutControllerServicesTest Controller with path /logout is ",
                controller.get(), is(instanceOf(LogoutController.class)));
        LogoutController logoutController = (LogoutController) controller.get();
        Assert.assertThat("ControllerContainerTest logoutControllerServicesTest LogoutController  has SessionService ",
                logoutController.getSessionService(), is(not(nullValue())));
    }

    @Test
    public void pageControllerServicesTest(){

        ControllerContainer controllerContainer = ControllerContainer.getInstance();

        Optional<Controller> controller = controllerContainer.findController("/page_1");
        Assert.assertThat("ControllerContainerTest loginControllerServicesTest exists Controller with path /page_1",
                controller.isPresent(), is(Boolean.TRUE));


        Assert.assertThat("ControllerContainerTest loginControllerServicesTest Controller with path /page_1 is ",
                controller.get(), is(instanceOf(PageController.class)));
        PageController pageController = (PageController) controller.get();

        Assert.assertThat("ControllerContainerTest loginControllerServicesTest PageController  has SessionService ",
                pageController.getSessionService(), is(not(nullValue())));
    }

    @Test
    public void userControllerServicesTest(){

        ControllerContainer controllerContainer = ControllerContainer.getInstance();

        Optional<Controller> controller = controllerContainer.findController("/users");
        Assert.assertThat("ControllerContainerTest userControllerServicesTest exists Controller with path /users",
                controller.isPresent(), is(Boolean.TRUE));


        Assert.assertThat("ControllerContainerTest userControllerServicesTest Controller with path /users is ",
                controller.get(), is(instanceOf(UserControllerRest.class)));
        UserControllerRest userControllerRest = (UserControllerRest) controller.get();
        Assert.assertThat("ControllerContainerTest userControllerServicesTest UserControllerRest  has UserService ",
                userControllerRest.getUserService(), is(not(nullValue())));


    }

    @Test
    public void roleControllerServicesTest(){

        ControllerContainer controllerContainer = ControllerContainer.getInstance();

        Optional<Controller> controller = controllerContainer.findController("/roles");
        Assert.assertThat("ControllerContainerTest roleControllerServicesTest exists Controller with path /roles",
                controller.isPresent(), is(Boolean.TRUE));


        Assert.assertThat("ControllerContainerTest roleControllerServicesTest Controller with path /roles is ",
                controller.get(), is(instanceOf(RoleControllerRest.class)));
        RoleControllerRest roleControllerRest = (RoleControllerRest) controller.get();
        Assert.assertThat("ControllerContainerTest roleControllerServicesTest UserControllerRest  has UserService ",
                roleControllerRest.getUserService(), is(not(nullValue())));

        Assert.assertThat("ControllerContainerTest roleControllerServicesTest UserControllerRest  has RoleService ",
                roleControllerRest.getRoleService(), is(not(nullValue())));
    }
}
