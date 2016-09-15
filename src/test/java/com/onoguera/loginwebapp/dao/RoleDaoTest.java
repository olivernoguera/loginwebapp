package com.onoguera.loginwebapp.dao;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 15/09/2016.
 */
public class RoleDaoTest {

    @Test
    public void getInstanceTest(){
        Assert.assertThat("RoleDaoTest::getInstanceTest::Instance must be RoleDao",
               RoleDao.getInstance(), instanceOf(RoleDao.class));
    }
}
