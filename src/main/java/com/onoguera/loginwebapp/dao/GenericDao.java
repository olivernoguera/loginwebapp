package com.onoguera.loginwebapp.dao;


import com.onoguera.loginwebapp.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public abstract class GenericDao<T extends Entity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericDao.class);

    private final Map<String, T> entitySet;

    public GenericDao() {
        this.entitySet = new ConcurrentHashMap<>();
    }

    public void insert(final T entity) {
        this.entitySet.put(entity.getId(), entity);
    }

    public T findOne(final String id) {
        return this.entitySet.get(id);
    }

    public Collection<T> elements() {
        return this.entitySet.values();
    }

    public void delete(final String id) {
        this.entitySet.remove(id);
    }

    public void update(final T entity) {
        this.entitySet.put(entity.getId(), entity);
    }

    public void deleteAll() {
        this.entitySet.clear();
    }
}