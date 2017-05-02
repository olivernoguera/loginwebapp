package com.onoguera.loginwebapp.startup;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.service.PageApiRoleService;
import com.onoguera.loginwebapp.service.AuthorizationService;
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

        AppContext.startContext(0);
        LoaderEntities.loadEntities();
        Collection<ReadUser> userCollection = AuthorizationService.getInstance().getReadUsers();
        Collection<Role> rolesCollection = PageApiRoleService.getInstance().getRoles();
        Assert.assertThat("Must load 4 Users", userCollection.size(), is(4));
        Assert.assertThat("Must load 6 Roles"+ rolesCollection, rolesCollection.size(), is(5));

        //Restore state
        for (ReadUser user : userCollection) {
            AuthorizationService.getInstance().removeUser(user.getUsername());
        }

        //Restore state
        for (Role role : rolesCollection) {
            PageApiRoleService.getInstance().removeRole(role.getId());
        }

        userCollection = AuthorizationService.getInstance().getReadUsers();
        rolesCollection = PageApiRoleService.getInstance().getRoles();
        Assert.assertThat("Must be 0 Users", userCollection.size(), is(0));
        Assert.assertThat("Must be 0 Roles", rolesCollection.size(), is(0));
    }

}
