package com.onoguera.loginwebapp;

import com.onoguera.loginwebapp.model.User;
import com.onoguera.loginwebapp.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class LoaderEntities {

    private UserService userService;

    private static final String USER_PREFIX = "USER_";
    private static final String PASSWORD_PREFIX = "PASS_";



    public LoaderEntities(){
        userService = UserService.getInstance();
    }

    public void loadEntities()
    {
        UserService userService = UserService.getInstance();
        List<User> users = LoaderEntities.createUsers(3);
        userService.bulkCreateUsers(users);

        User admin = new User("ADMIN","ADMIN");
        userService.addUser(admin);

    }

    private static List<User> createUsers(Integer numUsers){
        List<User> users = new ArrayList<>();

        for(int i = 1; i <= numUsers; i++){
            User user = new User(USER_PREFIX + i, PASSWORD_PREFIX + i);
            users.add(user);
        }
        return users;
    }


}
