package com.onoguera.loginwebapp;

import com.onoguera.loginwebapp.model.UserVO;
import com.onoguera.loginwebapp.service.UserService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class LoaderEntitiesTest {

    private LoaderEntities loaderEntities = new LoaderEntities();

    @Test
    public void loadEntities(){

        this.loaderEntities.loadEntities();
        Collection<UserVO> userCollection = UserService.getInstance().getUsersVO();
        Assert.assertThat("Must load 4 Users", userCollection.size(), is(4));

        //Restore state
        for(UserVO user: userCollection){
            UserService.getInstance().removeUser(user.getUsername());
        }

        userCollection = UserService.getInstance().getUsersVO();
        Assert.assertThat("Must be 0 Users", userCollection.size(), is(0));
    }

}
