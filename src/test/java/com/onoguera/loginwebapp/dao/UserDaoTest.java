package com.onoguera.loginwebapp.dao;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by olivernoguera on 15/09/2016.
 */
public class UserDaoTest {

    @Test
    public void getInstanceTest(){
        Assert.assertThat("UserDaoTest::getInstanceTest::Instance must be UserDao",
                UserDao.getInstance(), instanceOf(UserDao.class));
    }
}
