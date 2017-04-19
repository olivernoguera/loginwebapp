package com.onoguera.loginwebapp.entities;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by olivernoguera on 17/04/2017.
 *
 */
public class UserTest {

    private final static String MOCK_ID = "test1";
    private final static String MOCK_PASSWORD = "password1";
    private final static Role MOCK_ROLE1 = new Role("Role1");
    private final static Role MOCK_ROLE2 = new Role("Role2");

    /*
        Use comparator to sort before assert to method equals of is matcher compare in the same order
     */
    private static final Comparator<Role> ROLE_COMPARATOR =
            (l1, l2) -> (l1.getId().compareTo(l2.getId()));

    @Test
    public void userTests(){
        User userToTest = new User(MOCK_ID,MOCK_PASSWORD);

        List<Role> emptyRoles = new ArrayList<>();
        List<Role> twoRoles = new ArrayList<>();
        twoRoles.add(MOCK_ROLE1);
        twoRoles.add(MOCK_ROLE2);

        List<Role> onlyRole1 = new ArrayList<>();
        onlyRole1.add(MOCK_ROLE1);

        List<Role> onlyRole2 = new ArrayList<>();
        onlyRole2.add(MOCK_ROLE2);

        Assert.assertThat("RoleDaoTest::userTests::UserId",
                userToTest.getId(), is(MOCK_ID));
        Assert.assertThat("RoleDaoTest::userTests::UserPassword",
                userToTest.getPassword(), is(MOCK_PASSWORD));
        Assert.assertThat("RoleDaoTest::userTests::EmptyRoles",
                userToTest.getRoles(), is(emptyRoles));

        userToTest.setPassword(MOCK_PASSWORD+2);

        Assert.assertThat("RoleDaoTest::userTests::PasswordChanged",
                userToTest.getPassword(), is(MOCK_PASSWORD+2));

        userToTest.addRole(MOCK_ROLE1);

        Collections.sort(userToTest.getRoles(),ROLE_COMPARATOR);
        Assert.assertThat("RoleDaoTest::userTests::addRoles",
                userToTest.getRoles(), is(onlyRole1));

        userToTest.addRole(MOCK_ROLE2);
        Collections.sort(userToTest.getRoles(),ROLE_COMPARATOR);
        Assert.assertThat("RoleDaoTest::userTests::addTwoRoles",
                userToTest.getRoles(), is(twoRoles));

        userToTest.removeRole(MOCK_ROLE1.getId());
        Collections.sort(userToTest.getRoles(),ROLE_COMPARATOR);
        Assert.assertThat("RoleDaoTest::userTests::removeRole1",
                userToTest.getRoles(), is(onlyRole2));

        userToTest.addRole(MOCK_ROLE1);
        Collections.sort(userToTest.getRoles(),ROLE_COMPARATOR);
        Assert.assertThat("RoleDaoTest::userTests::addTwoRoles2",
                userToTest.getRoles(), equalTo(twoRoles));

        userToTest.removeRoles();
        Assert.assertThat("RoleDaoTest::userTests::removeRoles",
                userToTest.getRoles(), is(emptyRoles));


        userToTest.setRoles(twoRoles);
        Collections.sort(userToTest.getRoles(),ROLE_COMPARATOR);
        Assert.assertThat("RoleDaoTest::userTests::setRoles",
                userToTest.getRoles(), is(twoRoles));

        userToTest.removeRoles();
        Assert.assertThat("RoleDaoTest::userTests::removeRoles2",
                userToTest.getRoles(), is(emptyRoles));

        userToTest.addRoles(onlyRole2);
        Collections.sort(userToTest.getRoles(),ROLE_COMPARATOR);
        Assert.assertThat("RoleDaoTest::userTests::addRoles2",
                userToTest.getRoles(), is(onlyRole2));

        userToTest.addRoles(onlyRole1);
        Collections.sort(userToTest.getRoles(),ROLE_COMPARATOR);
        Assert.assertThat("RoleDaoTest::userTests::addRoles1",
                userToTest.getRoles(), is(twoRoles));

        userToTest.removeRoles();
        Assert.assertThat("RoleDaoTest::userTests::removeRoles3",
                userToTest.getRoles(), is(emptyRoles));

    }


    @Test
    public void userWithRolesTests(){

        List<Role> twoRoles = new ArrayList<>();
        twoRoles.add(MOCK_ROLE1);
        twoRoles.add(MOCK_ROLE2);

        User userToTest = new User(MOCK_ID,MOCK_PASSWORD,twoRoles);
        Assert.assertThat("RoleDaoTest::userWithRolesTests::UserId",
                userToTest.getId(), is(MOCK_ID));
        Assert.assertThat("RoleDaoTest::userWithRolesTests::UserPassword",
                userToTest.getPassword(), is(MOCK_PASSWORD));
        Assert.assertThat("RoleDaoTest::userWithRolesTests::twoRoles",
                userToTest.getRoles(), is(twoRoles));

    }

}
