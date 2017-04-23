package com.onoguera.loginwebapp.dao;

import java.util.Collection;

/**
 * Created by olivernoguera on 23/04/2017.
 */
public interface Dao<T> {

    void insert(final T entity);

    T findOne(final String id);

    Collection<T> elements();

    void delete(final String id);

    void update(final T entity);

    void deleteAll();

}
