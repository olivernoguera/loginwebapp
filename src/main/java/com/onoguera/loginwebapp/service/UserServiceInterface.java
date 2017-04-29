package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteUser;

import java.util.Collection;
import java.util.List;

/**
 * Created by olivernoguera on 26/06/2016.
 */
public interface UserServiceInterface {

    User validateUser(User user);

    Collection<ReadUser> getReadUsers();

    ReadUser getReadUser(String userId);

    WriteUser getWriteUser(String userId);

    void updateWriteUser(WriteUser writeUser);

    void removeAllUsers();

    void createWriteUsers(List<WriteUser> usersBody);

    User getUser(String userId);

    void updateUser(User user);

    void removeUser(String userId);
}
