package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.WriteRole;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olivernoguera on 07/06/2016.
 *
 */
public final class RoleConverter implements Converter<ReadRole,WriteRole,Role> {

    private static final RoleConverter roleConverter = new RoleConverter();

    /**
     * Procted to singleton
     */
    private RoleConverter(){};

    public static RoleConverter getInstance(){
        return roleConverter;
    }

    @Override
    public ReadRole entityToReadDTO(Role entity) {
        return new ReadRole(entity.getId());
    }

    @Override
    public WriteRole entityToWriteDTO(Role entity) {
        return new WriteRole(entity.getId());
    }

    @Override
    public Role writeDTOtoEntity(WriteRole dto) {
        return new Role(dto.getRole());
    }

    public static List<Role> writeDTOsToEntityList(List<WriteRole> writeRoles){
        return writeRoles.stream().map(w -> roleConverter.writeDTOtoEntity(w)).collect(Collectors.toList());
    }
}
