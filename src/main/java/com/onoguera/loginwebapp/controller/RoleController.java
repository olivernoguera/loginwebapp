package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.model.Role;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.view.ResponseEmpty;
import com.onoguera.loginwebapp.view.JsonResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseBadRequest;
import com.onoguera.loginwebapp.view.ResponseNotFound;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by oliver on 1/06/16.
 */
public class RoleController extends BaseController implements AuthController {

    //private static final Logger LOGGER = LoggerFactory.getLogger(RolesController.class);

    private static final String PATH = "/roles";

    private static final String ROLE_ID = "roleId";

    private static final RoleService roleService = RoleService.getInstance();

    private static final Pattern p = Pattern.compile(PATH + "/*(?<" + ROLE_ID + ">\\S*)");

    @Override
    public Pattern getURLPattern() {
        return p;
    }

    @Override
    public List<String> getPathParams() {
        return Arrays.asList(ROLE_ID);
    }

    @Override
    public Response doGet(Request request) {

        Map<String, String> pathParams = request.getPathParams();
        Response response;

        if (pathParams == null || pathParams.isEmpty()) {

            Collection<Role> roles = roleService.getRoles();
            response = new JsonResponse(HttpURLConnection.HTTP_OK, roles);

        } else {

            String roleId = pathParams.get(ROLE_ID);
            if (roleId == null) {
                return new ResponseBadRequest();
            }
            Role role = roleService.getRole(roleId);
            if (role == null) {
                return new ResponseNotFound();
            }
            response = new JsonResponse(HttpURLConnection.HTTP_OK, role);
        }
        return response;
    }

    @Override
    public Response doPost(Request request) {
        return new ResponseNotImplemented();
    }

    @Override
    public Response doPut(Request request) {

        Map<String, String> pathParams = request.getPathParams();
        if (pathParams == null) {
            return new ResponseBadRequest();
        }
        String roleId = pathParams.get(ROLE_ID);
        if (roleId == null || roleId.isEmpty()) {
            return new ResponseBadRequest();
        }
        Role role = new Role(roleId);
        roleService.addRole(role);
        Response response = new JsonResponse(HttpURLConnection.HTTP_CREATED, role);
        return response;
    }

    @Override
    public Response doDelete(Request request) {

        Map<String, String> pathParams = request.getPathParams();
        String roleId = pathParams.get(ROLE_ID);
        roleService.removeRole(roleId);
        Response response = new ResponseEmpty();
        return response;
    }
}
