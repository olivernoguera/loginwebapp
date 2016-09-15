package com.onoguera.loginwebapp.dao;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by olivernoguera on 15/09/2016.
 */
public class SessionDaoTest {

    @Test
    public void getInstanceTest(){
        Assert.assertThat("SessionDaoTest::getInstanceTest::Instance must be SessionDao",
                SessionDao.getInstance(), instanceOf(SessionDao.class));
    }
}
