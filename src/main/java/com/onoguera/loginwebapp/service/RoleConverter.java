package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.WriteRole;

/**
 * Created by olivernoguera on 07/06/2016.
 */
public class RoleConverter implements Converter<ReadRole,WriteRole,Role> {

    @Override
    public ReadRole entityToReadDTO(Role entity) {
        return new ReadRole(entity.getId());
    }

    @Override
    public WriteRole entityToWriteDTO(Role entity) {
        return new WriteRole(entity.getId(),entity.isWriteAccess());
    }

    @Override
    public Role writeDTOtoEntity(WriteRole dto) {
        return new Role(dto.getRole(), dto.isWriteAccess());
    }
}