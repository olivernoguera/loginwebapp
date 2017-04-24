package integration.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onoguera.loginwebapp.controller.Request;
import com.onoguera.loginwebapp.restcontroller.RoleControllerRest;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.response.JsonResponse;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseBadRequest;
import com.onoguera.loginwebapp.response.ResponseEmpty;
import com.onoguera.loginwebapp.response.ResponseNotFound;
import com.onoguera.loginwebapp.response.ResponseNotImplemented;
import com.onoguera.loginwebapp.service.RoleConverter;
import com.onoguera.loginwebapp.service.RoleService;
import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RoleControllerRest roleController = new RoleControllerRest();
    private RoleConverter roleConverter = new RoleConverter();

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

        Request request = new Request(null, pathParams, null,null);
        Response response = roleController.doGet(request);
        Assert.assertThat(" ResponseroleController must be bad request", response, instanceOf(ResponseBadRequest.class));
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));

    }

    @Test
    public void doGetEmptyResource() {

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        Request request = new Request(null, pathParams, null,null);
        Response response = roleController.doGet(request);
        Assert.assertThat(" Response must be not found", response, instanceOf(ResponseNotFound.class));
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doGetWithResource() throws JsonProcessingException {

        RoleService roleService = RoleService.getInstance();
        Role role = new Role("test");
        roleService.addRole(role);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, role.getId().toString());

        Request request = new Request(null, pathParams, null,null);
        Response response = roleController.doGet(request);

        ReadRole expectedRole = roleConverter.entityToReadDTO(role);

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty", response.getOutput(), is(MAPPER.writeValueAsString(expectedRole)));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        roleService.removeRole(role.getId());
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));

    }


    @Test
    public void doGetEmptyCollection() {

        Request request = new Request(null, null, null,null);
        Response response = roleController.doGet(request);
        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty", response.getOutput(), is("[]"));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doGeWithCollectionResource() throws JsonProcessingException {

        RoleService roleService = RoleService.getInstance();

        Role role = new Role("test");
        roleService.addRole(role);
        Role role2 = new Role("test2");
        roleService.addRole(role2);

        List<Role> collectionRole = new ArrayList<>();

        collectionRole.add(role2);
        collectionRole.add(role);

        Request request = new Request(null, null, null,null);
        Response response = roleController.doGet(request);



        List<ReadRole> expectedRoles =
                collectionRole.stream().map(r-> roleConverter.entityToReadDTO(r)).collect(Collectors.toList());

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be", response.getOutput(), is(MAPPER.writeValueAsString(expectedRoles)));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        roleService.removeRole(role.getId());
        roleService.removeRole(role2.getId());
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPost() {

        Request request = new Request(null, null, null,null);
        Response response = roleController.doPost(request);
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));

    }

    @Test
    public void doPut() throws JsonProcessingException {

        Role role = new Role("test");
        String output = MAPPER.writeValueAsString(roleConverter.entityToWriteDTO(role));

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("roleId", role.getId());

        Request request = new Request(null, pathParams, output,null);
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
        Request request = new Request(null, null, null,null);
        Response response = roleController.doPut(request);
        Assert.assertThat(" Response must be ResponseBadRequest", response, instanceOf(ResponseBadRequest.class));
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPutEmptyParam() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("roleId", "");
        Request request = new Request(null, null, null,null);
        Response response = roleController.doPut(request);
        Assert.assertThat(" Response must be ResponseBadRequest", response, instanceOf(ResponseBadRequest.class));
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPutEmptyParams() {
        Request request = new Request(null, null, null,null);
        Response response = roleController.doPut(request);
        Assert.assertThat(" Response must be ResponseBadRequest", response, instanceOf(ResponseBadRequest.class));
        Assert.assertThat("Roles collections must be empty", RoleService.getInstance().getRoles().size(), is(0));
    }


    @Test
    public void doPutNullBody() throws JsonProcessingException {

        Role role = new Role("test");
        String output = MAPPER.writeValueAsString(roleConverter.entityToWriteDTO(role));

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("roleId", role.getId());

        Request request = new Request(null, pathParams, null,null);
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
        Request request = new Request(null, pathParams, null,null);
        Response response = roleController.doDelete(request);
        Assert.assertThat(" Response must be Empty", response, instanceOf(ResponseEmpty.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }
}
