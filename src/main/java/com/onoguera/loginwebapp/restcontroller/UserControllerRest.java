package com.onoguera.loginwebapp.restcontroller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onoguera.loginwebapp.controller.Request;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;
import com.onoguera.loginwebapp.response.JsonResponse;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseBadRequest;
import com.onoguera.loginwebapp.response.ResponseEmpty;
import com.onoguera.loginwebapp.response.ResponseNotFound;
import com.onoguera.loginwebapp.response.ResponseNotImplemented;
import com.onoguera.loginwebapp.response.ResponseUnsupportedMediaType;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.service.UserService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by oliver on 1/06/16.
 *
 */
public class UserControllerRest extends RestAuthController {

    //private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String PATH = "/users";

    private static final String USER_ID = "userId";

    private static final String ROLE_ID = "roleId";

    private static final String PATH_ROLES = "roles";

    private static final UserService userService = UserService.getInstance();

    private static final RoleService roleService = RoleService.getInstance();

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

            Collection<ReadUser> users = userService.getReadUsers();
            response = new JsonResponse(HttpURLConnection.HTTP_OK, users);

        } else {
            String userId = pathParams.get(USER_ID);
            if (userId == null) {
                return new ResponseBadRequest();
            }

            ReadUser user = userService.getReadUser(userId);
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

    private Response getRoles(Request request, ReadUser user) {

        Response response;
        Map<String, String> pathParams = request.getPathParams();

        String roleId = pathParams.get(ROLE_ID);

        if (roleId == null) {
            response = new JsonResponse(HttpURLConnection.HTTP_OK, user.getRoles());
        } else {

            Optional<ReadRole> readRole =  user.getRoles().stream().filter(r->r.getRole().equals(roleId)).findFirst();
            if (readRole.isPresent()) {
                response = new JsonResponse(HttpURLConnection.HTTP_OK, readRole.get());
            } else {
                response = new ResponseNotFound();
            }
        }
        return response;
    }

    @Override
    public Response doPost(Request request) {
        Map<String, String> pathParams = request.getPathParams();
        if (pathParams == null) {
            return new ResponseBadRequest();
        }
        String userId = pathParams.get(USER_ID);
        String roles = pathParams.get(PATH_ROLES);
        String roleId = pathParams.get(ROLE_ID);
        if (userId != null && roles == null) {
            //Post must not be path variable of users
            //Not generate id's on this api
            //To create only one user use put
            return new ResponseNotImplemented();
        }
        if (userId != null && roleId != null) {
            //Post must not be path variable of users
            //Not generate id's on this api
            //To create only one role of user use put
            return new ResponseNotImplemented();
        }

        JsonRequest jsonRequest;
        Object object;
        if (request instanceof JsonRequest) {
            jsonRequest = (JsonRequest) request;
            if (userId != null) {
                WriteUser writeUser = userService.getWriteUser(userId);
                if( writeUser == null){
                    return new ResponseNotFound();
                }
                if (roles != null) {
                    //only update roles
                    List<WriteRole> rolesBody;
                    try {
                        rolesBody = (List<WriteRole>) jsonRequest.getBodyObject(new TypeReference<List<WriteRole>>() {});
                        writeUser.setRoles(rolesBody);
                        userService.updateWriteUser(writeUser);
                        return  new JsonResponse(HttpURLConnection.HTTP_CREATED, rolesBody);
                    } catch (IOException io) {
                        return new ResponseBadRequest();
                    }

                } else {
                    return new ResponseNotFound();
                }
            } else {
                List<WriteUser> usersBody;

                try {
                    usersBody = (List<WriteUser>) jsonRequest.getBodyObject(new TypeReference<List<WriteUser>>() {});
                    userService.removeAllUsers();
                    userService.createWriteUsers(usersBody);
                    return new JsonResponse(HttpURLConnection.HTTP_CREATED, usersBody);
                } catch (IOException e) {
                    return new ResponseBadRequest();
                }
            }
        } else {
            return new ResponseUnsupportedMediaType();
        }

    }

    @Override
    public Response doPut(Request request) {
        Map<String, String> pathParams = request.getPathParams();
        if (pathParams == null) {
            return new ResponseBadRequest();
        }
        String userId = pathParams.get(USER_ID);
        String roles = pathParams.get(PATH_ROLES);
        String roleId = pathParams.get(ROLE_ID);

        if (userId == null ) {
            //Post must not be path variable of users
            //Not generate id's on this api
            //To create only one role of user use put
            return new ResponseNotImplemented();
        }

        if (userId != null && roles != null && roleId == null) {
            //Put not treatment collection
            return new ResponseNotImplemented();
        }


        User user = userService.getUser(userId);
        if( roles != null)
        {
            if( user == null){
                return new ResponseNotFound();
            }

            Role role = roleService.getRole(roleId);
            if( role == null){
                return new ResponseNotFound();
            }
            user.addRole(role);
            userService.updateUser(user);
            WriteUser writeUser = userService.getWriteUser(user.getId());
            return new JsonResponse(HttpURLConnection.HTTP_CREATED, writeUser);

        }else{
            //Add User
            JsonRequest jsonRequest;
            Object object;
            if (request instanceof JsonRequest) {
                WriteUser writeUser;
                jsonRequest = (JsonRequest) request;
                try {
                    writeUser = (WriteUser) jsonRequest.getBodyObject(WriteUser.class);
                } catch (IOException e) {
                    return new ResponseBadRequest();
                }
                if( !writeUser.getUsername().equals(userId)){
                    return new ResponseBadRequest();
                }
                userService.updateWriteUser(writeUser);
                return  new JsonResponse(HttpURLConnection.HTTP_CREATED, writeUser);
            }
            else{
                return new ResponseUnsupportedMediaType();
            }
        }



    }

    @Override
    public Response doDelete(Request request) {
        Map<String, String> pathParams = request.getPathParams();
        if (pathParams == null || pathParams.get(USER_ID) == null) {
            //Delete all users, but not specify by security badrequest
            return new ResponseBadRequest();
        }

        String userId = pathParams.get(USER_ID);
        User user = userService.getUser(userId);

        if (user == null) {
            return new ResponseNotFound();
        }

        String roles = pathParams.get(PATH_ROLES);

        if (roles != null && !roles.isEmpty()) {

            String roleId = pathParams.get(ROLE_ID);

            if (roleId == null || roleId.isEmpty()) {
                user.removeRoles();
            } else {
                user.removeRole(roleId);
            }
            userService.updateUser(user);
        } else {
            userService.removeUser(userId);
        }

        Response response = new ResponseEmpty();
        return response;
    }
}
