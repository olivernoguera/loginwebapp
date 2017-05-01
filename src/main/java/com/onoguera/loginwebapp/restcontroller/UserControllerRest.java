package com.onoguera.loginwebapp.restcontroller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.response.JsonResponse;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseBadRequest;
import com.onoguera.loginwebapp.response.ResponseEmpty;
import com.onoguera.loginwebapp.response.ResponseNotFound;
import com.onoguera.loginwebapp.response.ResponseNotImplemented;
import com.onoguera.loginwebapp.response.ResponseUnsupportedMediaType;
import com.onoguera.loginwebapp.service.UserServiceInterface;

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



    private static final Pattern p =
            Pattern.compile(PATH + "/*(?<" + USER_ID + ">[^:\\/\\s]+)?\\/?(?<" + PATH_ROLES + ">"+
                    PATH_ROLES+")?\\/?(?<" + ROLE_ID + ">[^:\\/\\s]+)?");

    public UserControllerRest(UserServiceInterface userService) {
        super(userService);
    }

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

           return this.getUsers();

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


    private Response getUsers(){

        Collection<ReadUser> users = userService.getReadUsers();
        return new JsonResponse(HttpURLConnection.HTTP_OK, users);
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
        String userId = null;
        String roles = null;
        String roleId = null;

        if (pathParams != null) {
            userId = pathParams.get(USER_ID);
            roles = pathParams.get(PATH_ROLES);
            roleId = pathParams.get(ROLE_ID);
        }

        if (checkNotImplementedPost(userId, roles, roleId)) {
            return new ResponseNotImplemented();
        }

        if (request instanceof JsonRequest) {
            JsonRequest jsonRequest = (JsonRequest) request;
            if (userId != null) {
                return createRoles(userId, roles, jsonRequest);
            } else {
                return createUsers(jsonRequest);
            }
        } else {
            return new ResponseUnsupportedMediaType();
        }

    }

    private Response createUsers(JsonRequest jsonRequest) {
        try {
            List<WriteUser> usersBody = (List<WriteUser>)
                    jsonRequest.getBodyObject(new TypeReference<List<WriteUser>>() {});
            userService.setUsers(usersBody);
            return new JsonResponse(HttpURLConnection.HTTP_CREATED, usersBody);
        } catch (IOException e) {
            return new ResponseBadRequest();
        }
    }

    private Response createRoles(String userId, String roles, JsonRequest jsonRequest) {
        //only update roles
        try {
            List<WriteRole> rolesBody =
                    (List<WriteRole>) jsonRequest.getBodyObject(new TypeReference<List<WriteRole>>() {});
            userService.upsertRolesOfUser(userId,rolesBody);
            return  new JsonResponse(HttpURLConnection.HTTP_CREATED, rolesBody);
        } catch (IOException io) {
            return new ResponseBadRequest();
        }


    }

    private boolean checkNotImplementedPost(String userId, String roles, String roleId) {

        //Post only implemented for collections
        /**
         *  /users/{user_id} only user resource not implemented
         */
        if (roles == null && userId != null) {

            return true;
        }

        /**
         *  Only role resource not implemented
         *  /users/{user_id}/roles/{roleid}
         */
        if (userId != null && roles != null && roleId != null) {
            return true;
        }


        return false;
    }

    @Override
    public Response doPut(Request request) {

        Map<String, String> pathParams = request.getPathParams();
        String userId = null;
        String roles = null;
        String roleId = null;

        if (pathParams != null) {
            userId = pathParams.get(USER_ID);
            roles = pathParams.get(PATH_ROLES);
            roleId = pathParams.get(ROLE_ID);
        }

        if (checkNotImplementedPut(userId,roles,roleId) ) {
            //Post must not be path variable of users
            //Not generate id's on this api
            //To create only one role of user use put
            return new ResponseNotImplemented();
        }

        if( roles != null) {
            //Update roles
            return upsertRoleOfUser(userId,roleId);

        }else{

            if (request instanceof JsonRequest) {
                return upsertUser(userId, (JsonRequest)request);
            }else {
                return new ResponseUnsupportedMediaType();
            }
        }


    }

    private Response upsertUser(String userId, JsonRequest jsonRequest) {
        try {
            WriteUser writeUser = (WriteUser) jsonRequest.getBodyObject(WriteUser.class);
            if( !userId.equals(writeUser.getUsername())){
                return new ResponseBadRequest();
            }
            if( !userService.upsertUser(writeUser)){
                return new ResponseBadRequest();
            }
            return  new JsonResponse(HttpURLConnection.HTTP_CREATED, writeUser);
        } catch (IOException e) {
            return new ResponseBadRequest();
        }
    }

    private Response upsertRoleOfUser(String userId, String roleId) {

        if( !userService.upsertRolesOfUser(userId, Arrays.asList(new WriteRole(roleId)))){
            return  new ResponseNotFound();
        }
        return new JsonResponse(HttpURLConnection.HTTP_CREATED,  userService.getReadUser(userId));
    }


    private boolean checkNotImplementedPut(String userId, String roles, String roleId) {

        //Post only implemented for single resources
        /**
         *  /users only user resource not implemented
         */
        if (userId == null) {

            return true;
        }

        /**
         *  Only role resource not implemented
         *  /users/{user_id}/roles/{roleid}
         */
        if (userId != null && roles != null && roleId == null) {
            return true;
        }


        return false;
    }

    @Override
    public Response doDelete(Request request) {

        Map<String, String> pathParams = request.getPathParams();
        Response response = new ResponseEmpty();

        if (pathParams == null || pathParams.isEmpty()) {
            userService.removeUsers();
        }else {

            if (pathParams.get(USER_ID) == null) {
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
                if(!this.deleteRoles(user, roleId)){
                    return new ResponseBadRequest();
                }
            } else {
                userService.removeUser(userId);
            }
        }

        return response;
    }

    private boolean deleteRoles(User user, String roleId) {

        if (roleId == null || roleId.isEmpty() ) {
            //Delete all roles
            user.removeRoles();
        } else {

            //Delete role
            user.removeRole(roleId);
        }
        return userService.upsertUser(user);
    }


}
