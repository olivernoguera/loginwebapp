package com.onoguera.loginwebapp.dao;

import com.onoguera.loginwebapp.entities.Entity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 15/09/2016.
 */
public class GenericDaoTest {

    private EntityMockDao genericMockDao = new EntityMockDao();

    private static class EntityMockDao extends GenericDao<EntityMock> {

    }

    private static class EntityMock extends Entity {

        private final Integer value;

        public EntityMock(String id, Integer value) {
            super(id);
            this.value = value;
        }
    }

    @After
    public void tearDown() throws Exception {
        genericMockDao.deleteAll();
        Assert.assertThat("GenericDaoTest::testInsert::Must be clear",
                genericMockDao.elements().size(), is(Integer.valueOf(0)));
    }

    @Test
    public void testInsert() throws Exception {
        EntityMock entityMock1 = new EntityMock("test1", 1);
        genericMockDao.insert(entityMock1);
        Assert.assertThat("GenericDaoTest::testInsert::Must be one element",
                genericMockDao.elements().size(), is(Integer.valueOf(1)));
    }

    @Test
    public void testFindOneIsEqual() throws Exception {
        EntityMock entityMock1 = new EntityMock("test1", 1);
        genericMockDao.insert(entityMock1);
        Assert.assertThat("GenericDaoTest::testFindOneIsEqual::Must be one element",
                genericMockDao.elements().size(), is(Integer.valueOf(1)));
        EntityMock entityMock2 = genericMockDao.findOne(entityMock1.getId());
        Assert.assertThat("GenericDaoTest::testFindOneIsEqual::findOne entity must be equals than insert",
                entityMock1, is(entityMock2));
    }

    @Test
    public void testElements() throws Exception {
        EntityMock entityMock1 = new EntityMock("test1", 1);
        EntityMock entityMock2 = new EntityMock("test2", 2);
        genericMockDao.insert(entityMock1);
        genericMockDao.insert(entityMock2);
        Set<EntityMock> entitiesMock = genericMockDao.elements().stream().collect(Collectors.toSet());
        Assert.assertThat("GenericDaoTest::testElements::Must be two elements",
                entitiesMock.size(), is(Integer.valueOf(2)));
        Assert.assertThat("GenericDaoTest::testElements::Must exist entity one",
                entitiesMock.contains(entityMock1), is(Boolean.TRUE));
        Assert.assertThat("GenericDaoTest::testElements::Must exist entity Two",
                entitiesMock.contains(entityMock2), is(Boolean.TRUE));
    }

    @Test
    public void testDeleteOne() throws Exception {
        EntityMock entityMock1 = new EntityMock("test1", 1);
        EntityMock entityMock2 = new EntityMock("test2", 2);

        genericMockDao.insert(entityMock1);
        genericMockDao.insert(entityMock2);
        Assert.assertThat("GenericDaoTest::testDeleteOne::Must be two elements",
                genericMockDao.elements().size(), is(Integer.valueOf(2)));

        genericMockDao.delete(entityMock1.getId());
        Assert.assertThat("GenericDaoTest::testDeleteOne::Must be one element",
                genericMockDao.elements().size(), is(Integer.valueOf(1)));
        Assert.assertThat("GenericDaoTest::testDeleteOne::Must one element",
                genericMockDao.elements().size(), is(Integer.valueOf(1)));
        Assert.assertThat("GenericDaoTest::testDeleteOne::Must exist entity two",
                genericMockDao.elements().contains(entityMock2), is(Boolean.TRUE));

    }

}