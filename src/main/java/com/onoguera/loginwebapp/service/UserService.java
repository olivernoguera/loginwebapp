package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.UserDao;
import com.onoguera.loginwebapp.model.User;
import com.onoguera.loginwebapp.model.UserVO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oliver on 1/06/16.
 */
public class UserService implements  Service
{
    private final static UserService INSTANCE = new UserService();
    private final UserDao userDao  = UserDao.getInstance();

    private UserService(){
        super();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public void addUser(final User user){
        this.userDao.insert(user);
    }

    public User getUser(final String id){
        return this.userDao.findOne(id);
    }

    public UserVO getUserVO(final String id){
        User user = this.userDao.findOne(id);
        if( user == null){
            return null;
        }
        return new UserVO(user.getId());
    }

    public List<UserVO> getUsersVO(){
        return this.userDao.elements().stream().map(u->
                new UserVO(u.getId())).collect(Collectors.toList());
    }

    public void removeUser(final String id){
        this.userDao.delete(id);
    }

    public void updateUser(final User user){
        this.userDao.update(user);
    }

    public void bulkCreateUsers(Collection<User> collection){
        collection.forEach(user -> this.userDao.insert(user));
    }

}
