package integration.integration;

import com.onoguera.loginwebapp.controller.Controller;
import com.onoguera.loginwebapp.controller.ControllerContainer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class ControllerContainerTest {

    @Test
    public void getControllerByNullPath() {

        ControllerContainer controllerContainer = ControllerContainer.getInstance();
        Optional<Controller> controller = controllerContainer.findController(null);

        Assert.assertThat("getControllerByNullPath:: Not exist controller with pattern path null",
                controller.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void getControllerByEmptyPath() {

        ControllerContainer controllerContainer = ControllerContainer.getInstance();
        Optional<Controller> controller = controllerContainer.findController("");

        Assert.assertThat("getControllerByNullPath:: Not exist controller with pattern path \"\"",
                controller.isPresent(), is(Boolean.FALSE));

    }

    @Test
    public void getControllerByTestPath() {

        ControllerContainer controllerContainer = ControllerContainer.getInstance();
        Optional<Controller> controller = controllerContainer.findController("");

        Assert.assertThat("getControllerByNullPath:: Not exist controller with pattern path Test",
                controller.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void getControllerByUserCorrectPaths() {

        ControllerContainer controllerContainer = ControllerContainer.getInstance();

        Optional<Controller> controller = controllerContainer.findController("/users");
        Assert.assertThat("getControllerByNullPath:: Not exist controller with pattern path /users",
                controller.isPresent(), is(Boolean.TRUE));

        controller = controllerContainer.findController("/users/");
        Assert.assertThat("getControllerByNullPath:: Not exist controller with pattern path /users/",
                controller.isPresent(), is(Boolean.TRUE));

        controller = controllerContainer.findController("/users/51515");
        Assert.assertThat("getControllerByNullPath:: Not exist controller with pattern path /users/",
                controller.isPresent(), is(Boolean.TRUE));

        controller = controllerContainer.findController("/users/r22r2");
        Assert.assertThat("getControllerByNullPath:: Not exist controller with pattern path /users/",
                controller.isPresent(), is(Boolean.TRUE));


    }


}
