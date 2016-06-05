package com.onoguera.loginwebapp.controller;

import com.google.gson.Gson;
import com.onoguera.loginwebapp.model.Role;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.view.JsonResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseBadRequest;
import com.onoguera.loginwebapp.view.ResponseEmpty;
import com.onoguera.loginwebapp.view.ResponseNotFound;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;
import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class RoleControllerTest {

    private final static String BASE_PATH = "/roles";
    private final static String BASE_PATH_WITH_END_SLASH = "/roles/";
    private final static String BASE_PATH_WITH_PATH_PARAM_NUMERIC = "/roles/515151";
    private final static String BASE_PATH_WITH_PATH_PARAM_STRING = "/roles/test";

    private final static String BAD_PATH = "roles";
    private final static String BAD_PATH_2 = "badpath";

    private static final String CORRECT_PARAM = "roleId";
    private static final Gson GSON = new Gson();

    private RoleController roleController = new RoleController();

    @Test
    public void correctFiltersTest() {

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BASE_PATH,
                roleController.filter(BASE_PATH),
                is(Boolean.TRUE));

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BASE_PATH_WITH_END_SLASH,
                roleController.filter(BASE_PATH_WITH_END_SLASH),
                is(Boolean.TRUE));

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BASE_PATH_WITH_PATH_PARAM_NUMERIC,
                roleController.filter(BASE_PATH_WITH_PATH_PARAM_NUMERIC),
                is(Boolean.TRUE));

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BASE_PATH_WITH_PATH_PARAM_STRING,
                roleController.filter(BASE_PATH_WITH_PATH_PARAM_STRING),
                is(Boolean.TRUE));
    }

    @Test
    public void badFiltersTest() {

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BAD_PATH,
                roleController.filter(BAD_PATH),
                is(Boolean.FALSE));

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BAD_PATH_2,
                roleController.filter(BAD_PATH_2),
                is(Boolean.FALSE));
    }


    @Test
    public void doGetBadPathParamResource() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("badparam", "badparam");

        Request request = new Request(null, pathParams, null);
        Response response = roleController.doGet(request);
        Assert.assertThat(" ResponseroleController must be bad request", response, instanceOf(ResponseBadRequest.class));

    }

    @Test
    public void doGetEmptyResource() {

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        Request request = new Request(null, pathParams, null);
        Response response = roleController.doGet(request);
        Assert.assertThat(" Response must be not found", response, instanceOf(ResponseNotFound.class));
    }

    @Test
    public void doGeWithResource() {

        RoleService roleService = RoleService.getInstance();
        Role role = new Role("test");
        roleService.addRole(role);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, role.getId().toString());

        Request request = new Request(null, pathParams, null);
        Response response = roleController.doGet(request);

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty", response.getOutput(), is(GSON.toJson(role)));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        roleService.removeRole(role.getId());

    }


    @Test
    public void doGetEmptyCollection() {

        Request request = new Request(null, null, null);
        Response response = roleController.doGet(request);
        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty", response.getOutput(), is("[]"));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));
    }

    @Test
    public void doGeWithCollectionResource() {

        RoleService roleService = RoleService.getInstance();

        Role role = new Role("test");
        roleService.addRole(role);
        Role role2 = new Role("test2");
        roleService.addRole(role2);

        List<Role> collectionRole = new ArrayList<>();

        collectionRole.add(role2);
        collectionRole.add(role);

        Request request = new Request(null, null, null);
        Response response = roleController.doGet(request);

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty", response.getOutput(), is(GSON.toJson(collectionRole)));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        roleService.removeRole(role.getId());
        roleService.removeRole(role2.getId());
    }

    @Test
    public void doPost() {

        Request request = new Request(null, null, null);
        Response response = roleController.doPost(request);
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));

    }

    @Test
    public void doPut() {

        Role role = new Role("test");
        String output = GSON.toJson(role);
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("roleId", role.getId());

        Request request = new Request(null, pathParams, output);
        Response response = roleController.doPut(request);
        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be:", response.getOutput(), is(output));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));
        RoleService.getInstance().removeRole(role.getId());
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));
    }


    @Test
    public void doPutNullParam() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("roleId", null);
        Request request = new Request(null, null, null);
        Response response = roleController.doPut(request);
        Assert.assertThat(" Response must be ResponseBadRequest", response, instanceOf(ResponseBadRequest.class));
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPutEmptyParam() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("roleId", "");
        Request request = new Request(null, null, null);
        Response response = roleController.doPut(request);
        Assert.assertThat(" Response must be ResponseBadRequest", response, instanceOf(ResponseBadRequest.class));
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPutEmptyParams() {
        Request request = new Request(null, null, null);
        Response response = roleController.doPut(request);
        Assert.assertThat(" Response must be ResponseBadRequest", response, instanceOf(ResponseBadRequest.class));
    }


    @Test
    public void doPutNullBody() {

        Role role = new Role("test");
        String output = GSON.toJson(role);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("roleId", role.getId());

        Request request = new Request(null, pathParams, null);
        Response response = roleController.doPut(request);

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be:", response.getOutput(), is(output));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));
        Assert.assertThat("Roles collections must be 1 element", RoleService.getInstance().getRoles().size(), is(1));
        RoleService.getInstance().removeRole(role.getId());
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));
    }


    @Test
    public void doDelete() {
        Role role = new Role("test");
        RoleService.getInstance().addRole(role);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("roleId", role.getId());
        Request request = new Request(null, pathParams, null);
        Response response = roleController.doDelete(request);
        Assert.assertThat(" Response must be Empty", response, instanceOf(ResponseEmpty.class));
    }
}
