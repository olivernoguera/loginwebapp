package com.onoguera.loginwebapp;

import com.onoguera.loginwebapp.model.Role;
import com.onoguera.loginwebapp.model.UserVO;
import com.onoguera.loginwebapp.service.RoleService;
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
        Collection<Role> rolesCollection = RoleService.getInstance().getRoles();
        Assert.assertThat("Must load 4 Users", userCollection.size(), is(4));
        Assert.assertThat("Must load 3 Roles", rolesCollection.size(), is(3));

        //Restore state
        for(UserVO user: userCollection){
            UserService.getInstance().removeUser(user.getUsername());
        }

        //Restore state
        for(Role role: rolesCollection){
            RoleService.getInstance().removeRole(role.getId());
        }

        userCollection = UserService.getInstance().getUsersVO();
        rolesCollection = RoleService.getInstance().getRoles();
        Assert.assertThat("Must be 0 Users", userCollection.size(), is(0));
        Assert.assertThat("Must be 0 Roles", rolesCollection.size(), is(0));
    }

}
