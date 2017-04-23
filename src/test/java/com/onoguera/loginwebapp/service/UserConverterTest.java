package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 23/04/2017.
 */
public class UserConverterTest {

    private UserConverter userConverter = UserConverter.getInstance();
    private RoleConverter roleConverter = RoleConverter.getInstance();

    @Test
    public void entityToReadDTOTest (){

        User userMock = new User("mockId","mockPassword");
        ReadUser readUser = userConverter.entityToReadDTO(userMock);

        List<Role> readRolesMock = new ArrayList<>();
        Role expectedReadRole = new Role("MockRole");
        readRolesMock.add(expectedReadRole);

        List<ReadRole> exptectedReadRoles =
                userMock.getRoles().stream().map(r->roleConverter.entityToReadDTO(r)).collect(Collectors.toList());
        Assert.assertThat("UserConverterTest entityToReadDTOTest getUsername",
                readUser.getUsername(), is(userMock.getId()));
        Assert.assertThat("UserConverterTest entityToReadDTOTest getRoles",
                readUser.getRoles(), is(exptectedReadRoles));

        userMock.setRoles(readRolesMock);
        exptectedReadRoles =
                userMock.getRoles().stream().map(r->roleConverter.entityToReadDTO(r)).collect(Collectors.toList());
        readUser = userConverter.entityToReadDTO(userMock);
        Assert.assertThat("UserConverterTest entityToReadDTOTest setRoles",
                readUser.getRoles(), is(exptectedReadRoles));

        userMock.setRoles(null);
        readUser = userConverter.entityToReadDTO(userMock);
        Assert.assertThat("UserConverterTest entityToReadDTOTest nullRoles",
                readUser.getRoles(), is(new ArrayList<>()));
    }

    @Test
    public void entityToWriteDTOTest (){

        List<Role> readRolesMock = new ArrayList<>();
        Role expectedReadRole = new Role("MockRole");
        readRolesMock.add(expectedReadRole);

        User userMock = new User("mockId","mockPassword",readRolesMock);
        WriteUser writeUser = userConverter.entityToWriteDTO(userMock);

        List<WriteRole> exptectedReadRoles =
                userMock.getRoles().stream().map(r->roleConverter.entityToWriteDTO(r)).collect(Collectors.toList());
        Assert.assertThat("UserConverterTest entityToWriteDTOTest getUsername",
                writeUser.getUsername(), is(userMock.getId()));
        Assert.assertThat("UserConverterTest entityToWriteDTOTest getRoles",
                writeUser.getRoles(), is(exptectedReadRoles));


        User exptectedUser = userConverter.writeDTOtoEntity(writeUser);
        Assert.assertThat("UserConverterTest entityToWriteDTOTest getUsername",
                exptectedUser.getId(), is(userMock.getId()));
        Assert.assertThat("UserConverterTest entityToWriteDTOTest getPassword",
                exptectedUser.getPassword(), is(userMock.getPassword()));
        Assert.assertThat("UserConverterTest entityToWriteDTOTest getRoles",
                exptectedUser.getRoles(), is(userMock.getRoles()));

    }

    @Test
    public void entityToWriteDTONullValueTest (){

        User userMock = new User("mockId","mockPassword",null);
        WriteUser writeUser = userConverter.entityToWriteDTO(userMock);

        Assert.assertThat("UserConverterTest entityToWriteDTONullValueTest getUsername",
                writeUser.getUsername(), is(userMock.getId()));
        Assert.assertThat("UserConverterTest entityToWriteDTONullValueTest getRoles",
                writeUser.getRoles(), is(new ArrayList<>()));


        User exptectedUser = userConverter.writeDTOtoEntity(writeUser);
        Assert.assertThat("UserConverterTest entityToWriteDTONullValueTest getUsername",
                exptectedUser.getId(), is(userMock.getId()));
        Assert.assertThat("UserConverterTest entityToWriteDTONullValueTest getPassword",
                exptectedUser.getPassword(), is(userMock.getPassword()));
        Assert.assertThat("UserConverterTest entityToWriteDTONullValueTest getRoles",
                exptectedUser.getRoles(), is(userMock.getRoles()));


        writeUser.setRoles(null);
        exptectedUser = userConverter.writeDTOtoEntity(writeUser);

        Assert.assertThat("UserConverterTest entityToWriteDTONullValueTest get null roles",
                exptectedUser.getRoles(), is(new ArrayList<>()));

    }
}
