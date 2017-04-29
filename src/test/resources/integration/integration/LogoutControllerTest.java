package integration.integration;

import com.onoguera.loginwebapp.controller.LogoutController;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseNotImplemented;
import com.onoguera.loginwebapp.view.LoginResponse;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 26/06/2016.
 */
public class LogoutControllerTest {

    private LogoutController logoutController = new LogoutController();


    @Test
    public void doPostWithoutSessionTest(){

        Request request = new Request(null,null,null,null);
        Response response = logoutController.doPost(request);
        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 302 status ", response.getHttpStatus(), is(302));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be login", loginResponse.getLocation(), is( "login"));

    }

    @Test
    public void doPostWithSessionTest(){

        Request request = new Request(null,null,null,new Session(null ,"1"));
        logoutController.setSessionService(new SessionMockServices.SessionServiceMock());
        Response response = logoutController.doPost(request);
        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 302 status ", response.getHttpStatus(), is(302));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be login", loginResponse.getLocation(), is( "login"));
    }

    @Test
    public void doGetTest(){
        Request request = new Request(null,null,null,null);
        Response response = logoutController.doGet(request);
        Assert.assertThat(" Response must be ResponseNotImplemented ", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doDeleteTest(){
        Request request = new Request(null,null,null,null);
        Response response = logoutController.doDelete(request);
        Assert.assertThat(" Response must be ResponseNotImplemented ", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doPutTest(){
        Request request = new Request(null,null,null,null);
        Response response = logoutController.doPut(request);
        Assert.assertThat(" Response must be ResponseNotImplemented ", response, instanceOf(ResponseNotImplemented.class));
    }


    @Test
    public void checkAuthAndRestAPITest(){
        Assert.assertThat(" getBadHeaders must be return null ", logoutController.getBadHeaders(null, null, null, null), nullValue());
    }

}
