package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;

import java.util.List;

/**
 * Created by olivernoguera on 26/06/2016.
 */
public interface UserServiceInterface {

    User validateUser(User user);

    List<ReadUser> getReadUsers();

    User getUser(final String userId);

    ReadUser getReadUser(final String userId);

    boolean upsertUser(WriteUser writeUser);

    boolean upsertUser(User user);

    void removeUser(final String id);

    boolean setUsers(List<WriteUser> users);

    boolean upsertRolesOfUser(String userId, List<WriteRole> newroles);

    void removeUsers();
}
