package com.onoguera.loginwebapp.startup;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
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


    @Test
    public void loadEntities() {
        AppContext.startContext();
        LoaderEntities.loadEntities();
        Collection<User> userCollection = UserService.getInstance().getUsers();
        Collection<Role> rolesCollection = RoleService.getInstance().getRoles();
        Assert.assertThat("Must load 4 Users", userCollection.size(), is(4));
        Assert.assertThat("Must load 4 Roles"+ rolesCollection, rolesCollection.size(), is(4));

        //Restore state
        for (User user : userCollection) {
            UserService.getInstance().removeUser(user.getId());
        }

        //Restore state
        for (Role role : rolesCollection) {
            RoleService.getInstance().removeRole(role.getId());
        }

        userCollection = UserService.getInstance().getUsers();
        rolesCollection = RoleService.getInstance().getRoles();
        Assert.assertThat("Must be 0 Users", userCollection.size(), is(0));
        Assert.assertThat("Must be 0 Roles", rolesCollection.size(), is(0));
    }

}
