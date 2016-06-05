package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.UserDao;
import com.onoguera.loginwebapp.model.Role;
import com.onoguera.loginwebapp.model.User;
import com.onoguera.loginwebapp.model.UserVO;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void addUser(final User user, final Role role){
        user.addRole(role);
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
        return new UserVO(user.getId(),user.getRoles());
    }

    public List<UserVO> getUsersVO(){
        return this.userDao.elements().stream().map(u->
                new UserVO(u.getId(),u.getRoles())).collect(Collectors.toList());
    }

    public void removeUser(final String id){
        User user = userDao.findOne(id);
        if( user != null){
            user.deleteRoles();
            this.userDao.delete(id);
        }
    }

    public void updateUser(final User user){
        this.userDao.update(user);
    }

    public void bulkCreateUsers(Collection<User> collection){
        collection.forEach(user -> this.userDao.insert(user));
    }

    /**
     * Validate user and returns Roles of this.
     * If user not exist or not validate return null (whithout role)
     * @param user
     * @return
     */
    public List<Role> getRoles(User user) {
        List<Role> roles = new ArrayList<Role>();
        if( user.getPassword() == null){
            return roles;
        }
        User userStore = this.getUser(user.getId());
        if( userStore == null){
            return roles;
        }

        if(user.getPassword().equals(userStore.getPassword())){
            roles = new ArrayList<>(userStore.getRoles());
        }
        return roles;

    }
}
