package com.onoguera.loginwebapp.entities;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 15/09/2016.
 *
 */
public class EntityDaoTest {

    private static class EntityMock extends  Entity{

        public EntityMock(String id) {
            super(id);
        }

    }

    @Test
    public void equalsTest(){
        EntityMock entityMock = new EntityMock("test1");

        Assert.assertThat("EntityDaoTest::equalsTest::Null is not equals than entity",
                entityMock.equals(null), is(Boolean.FALSE));

        Assert.assertThat("EntityDaoTest::equalsTest::Integer is not equals than entity",
                entityMock.equals(new Integer(1)), is(Boolean.FALSE));

        Assert.assertThat("EntityDaoTest::equalsTest::EntityMock is equals than EntityMock with key test1",
                entityMock.equals(new EntityMock("test1")), is(Boolean.TRUE));
    }

    @Test
    public void hashCodeTest(){
        EntityMock entityMock = new EntityMock("test1");

        Assert.assertThat("EntityDaoTest::hashCodeTest::EntityMock is equals than hascode of test1 string",
                entityMock.hashCode(), is(entityMock.getId().hashCode()));
    }


    @Test
    public void getTest(){
        EntityMock entityMock = new EntityMock("test1");

        Assert.assertThat("EntityDaoTest::getTest::EntityMock is equals than test1 string",
                entityMock.getId(), is("test1"));
    }
}
