package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.model.UserVO;
import com.onoguera.loginwebapp.service.UserService;
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
public class UserController extends  BaseController implements AuthController {

    //private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String PATH = "/users";

    private static final String USER_ID = "userId";

    private static final UserService userService = UserService.getInstance();

    private static final Pattern p = Pattern.compile(PATH+"/*(?<" + USER_ID + ">\\S*)");

    @Override
    public Pattern getURLPattern() {
        return p;
    }

    @Override
    public List<String> getPathParams() {
        return  Arrays.asList(USER_ID);
    }

    @Override
    public Response doGet(Request request) {

        Map<String,String> pathParams = request.getPathParams();
        Response response;

        if( pathParams == null || pathParams.isEmpty()){

            Collection<UserVO> users = userService.getUsersVO();
            response = new JsonResponse(HttpURLConnection.HTTP_OK,users);

        }
        else{

            String userId = pathParams.get(USER_ID);
            if( userId == null){
                return new ResponseBadRequest();
            }
            UserVO user = userService.getUserVO(userId);
            if (user == null) {
                return new ResponseNotFound();
            }
            response = new JsonResponse(HttpURLConnection.HTTP_OK, user);
        }
        return response;
    }

    @Override
    public Response doPost(Request request) {
        return new ResponseNotImplemented();
    }

    @Override
    public Response doPut(Request request) {
        return new ResponseNotImplemented();
    }

    @Override
    public Response doDelete(Request request) {
        return new ResponseNotImplemented();
    }
}
