package com.onoguera.loginwebapp.entities;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 15/09/2016.
 *
 */
public class RoleDaoTest {

    @Test
    public void createDefaultRole(){
        Role roleToTest = new Role("test1");

        Assert.assertThat("RoleDaoTest::createDefaultRole::Default role must be test1 id",
                roleToTest.getId(), is("test1"));

        Assert.assertThat("RoleDaoTest::createDefaultRole::Default role must not  be writeAccess",
                roleToTest.isWriteAccess(), is(Boolean.FALSE));
    }

    @Test
    public void createCustomRole(){
        Role roleToTest = new Role("test1", false);
        Assert.assertThat("RoleDaoTest::createCustomRole::Custom role must be test1 id",
                roleToTest.getId(), is("test1"));
        Assert.assertThat("RoleDaoTest::createCustomRole::Default role test1 must not  be writeAccess",
                roleToTest.isWriteAccess(), is(Boolean.FALSE));

        roleToTest = new Role("test2", true);
        Assert.assertThat("RoleDaoTest::createCustomRole::Custom role must be test2 id",
                roleToTest.getId(), is("test2"));
        Assert.assertThat("RoleDaoTest::createCustomRole::Default role test2 must not  be writeAccess",
                roleToTest.isWriteAccess(), is(Boolean.TRUE));
    }

}
