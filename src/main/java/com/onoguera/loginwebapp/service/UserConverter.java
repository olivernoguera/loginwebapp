package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olivernoguera on 07/06/2016.
 */
public class UserConverter implements Converter<ReadUser,WriteUser,User> {

    private final static RoleConverter roleConverter = new RoleConverter();

    @Override
    public ReadUser entityToReadDTO(User entity) {
        List<ReadRole> readRoleList =
                entity.getRoles().stream().map(r -> roleConverter.entityToReadDTO(r)).collect(Collectors.toList());
        return new ReadUser(entity.getId(),readRoleList);
    }

    @Override
    public WriteUser entityToWriteDTO(User entity) {
        List<WriteRole> writeRoleList =
                entity.getRoles().stream().map(r -> roleConverter.entityToWriteDTO(r)).collect(Collectors.toList());
        return new WriteUser(entity.getId(), entity.getPassword(),writeRoleList);
    }

    //@Override
    //public User readDTOtoEntity(ReadUser dto) {
    //    return new ReadUser(dto.getUsername(),entity.getRoles());
    //}

    @Override
    public User writeDTOtoEntity(WriteUser dto) {
        List<Role> entityRoleList =
                dto.getRoles().stream().map(r -> roleConverter.writeDTOtoEntity(r)).collect(Collectors.toList());
        return new User(dto.getUsername(), dto.getPassword(),entityRoleList);
    }
}
