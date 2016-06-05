package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.model.Role;
import com.onoguera.loginwebapp.model.User;
import com.onoguera.loginwebapp.model.UserVO;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.JsonResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseBadRequest;
import com.onoguera.loginwebapp.view.ResponseEmpty;
import com.onoguera.loginwebapp.view.ResponseNotFound;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by oliver on 1/06/16.
 */
public class UserController extends BaseController implements AuthController {

    //private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String PATH = "/users";

    private static final String USER_ID = "userId";

    private static final String ROLE_ID = "roleId";

    private static final String PATH_ROLES = "roles";

    private static final UserService userService = UserService.getInstance();

    private static final Pattern p =
            Pattern.compile(PATH + "/*(?<" + USER_ID + ">[^:\\/\\s]+)?\\/?(?<" + PATH_ROLES + ">roles)?\\/?(?<" + ROLE_ID + ">[^:\\/\\s]+)?");

    @Override
    public Pattern getURLPattern() {
        return p;
    }

    @Override
    public List<String> getPathParams() {
        return Arrays.asList(USER_ID, PATH_ROLES, ROLE_ID);
    }

    @Override
    public Response doGet(Request request) {

        Map<String, String> pathParams = request.getPathParams();
        Response response;

        if (pathParams == null || pathParams.isEmpty()) {

            Collection<UserVO> users = userService.getUsersVO();
            response = new JsonResponse(HttpURLConnection.HTTP_OK, users);

        } else {
            String userId = pathParams.get(USER_ID);
            if (userId == null) {
                return new ResponseBadRequest();
            }

            UserVO user = userService.getUserVO(userId);
            if (user == null) {
                return new ResponseNotFound();
            }

            if (pathParams.get(PATH_ROLES) != null) {
                return this.getRoles(request, user);
            }

            response = new JsonResponse(HttpURLConnection.HTTP_OK, user);
        }
        return response;
    }

    private Response getRoles(Request request, UserVO user) {
        Response response;

        Map<String, String> pathParams = request.getPathParams();
        String roleId = pathParams.get(ROLE_ID);
        Collection<Role> roles = user.getRoles();
        if (roleId == null) {
            response = new JsonResponse(HttpURLConnection.HTTP_OK, roles);
        } else {
            Optional<Role> role = roles.stream().filter(r -> r.getId().equals(roleId)).findFirst();
            if (role.isPresent()) {
                response = new JsonResponse(HttpURLConnection.HTTP_OK, role.get());
            } else {
                response = new ResponseNotFound();
            }
        }
        return response;
    }

    @Override
    public Response doPost(Request request) {
        return new ResponseNotImplemented();
    }

    @Override
    public Response doPut(Request request) {

        /*
        JsonRequest jsonRequest;

        if( request instanceof  JsonRequest){
            jsonRequest = (JsonRequest)request;
        }else{
            return new ResponseUnsupportedMediaType();
        }*/
        return new ResponseNotImplemented();
    }

    @Override
    public Response doDelete(Request request) {
        Map<String, String> pathParams = request.getPathParams();
        if(pathParams == null ||  pathParams.get(USER_ID) == null) {
        //Delete all users, but not specify by security badrequest
            return new  ResponseBadRequest();
        }
        String userId = pathParams.get(USER_ID);
        User user = userService.getUser(userId);
        if(user == null){
            return new ResponseNotFound();
        }
        String roles = pathParams.get(PATH_ROLES);
        if( roles != null && !roles.isEmpty()){
            String roleId = pathParams.get(ROLE_ID);
            if( roleId == null || roleId.isEmpty()){
                //delete all roles of user
                user.deleteRoles();
            }else{
                user.removeRole(roleId);
            }
            userService.updateUser(user);
        }else{
            userService.removeUser(userId);
        }

        Response response = new ResponseEmpty();
        return response;
    }
}
