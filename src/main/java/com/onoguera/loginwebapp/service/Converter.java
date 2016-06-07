package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.Entity;
import com.onoguera.loginwebapp.model.ReadDTO;
import com.onoguera.loginwebapp.model.WriteDTO;

/**
 * Created by olivernoguera on 07/06/2016.
 */
public interface Converter <RD extends ReadDTO, WD extends WriteDTO,E extends Entity>  {

    <RD> RD entityToReadDTO(final E entity);

    <WD> WD entityToWriteDTO(final E entity);

    //Not applicable
    //public abstract E readDTOtoEntity(final RD dto);

    <E> E writeDTOtoEntity(final WD dto);

}
