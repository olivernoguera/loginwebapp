package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olivernoguera on 07/06/2016.
 *
 */
public class UserConverter implements Converter<ReadUser,WriteUser,User> {


    private final static UserConverter userConverter = new UserConverter();

    /**
     *  Protect to singleton
     */
    private UserConverter(){}

    public static UserConverter getInstance(){
        return userConverter;
    }

    @Override
    public ReadUser entityToReadDTO(User entity) {
        if( entity.getRoles() == null){
            return new ReadUser(entity.getId(),new ArrayList<>());
        }
        List<ReadRole> readRoleList =
                entity.getRoles().stream().map(r -> RoleConverter.getInstance().entityToReadDTO(r)).collect(Collectors.toList());
        return new ReadUser(entity.getId(),readRoleList);
    }

    @Override
    public WriteUser entityToWriteDTO(User entity) {

        List<WriteRole> writeRoleList =
                entity.getRoles().stream().map(r -> RoleConverter.getInstance().entityToWriteDTO(r)).collect(Collectors.toList());
        return new WriteUser(entity.getId(), entity.getPassword(),writeRoleList);
    }

    @Override
    public User writeDTOtoEntity(WriteUser dto) {
        if( dto.getRoles() == null){
            return new User(dto.getUsername(), dto.getPassword(),new ArrayList<>());
        }
        List<Role> entityRoleList =
                dto.getRoles().stream().map(r -> RoleConverter.getInstance().writeDTOtoEntity(r)).collect(Collectors.toList());
        return new User(dto.getUsername(), dto.getPassword(),entityRoleList);
    }

    public static List<User> writeDTOsToEntityList(List<WriteUser> writeUsers){
        return writeUsers.stream().map(w -> userConverter.writeDTOtoEntity(w)).collect(Collectors.toList());
    }

}
