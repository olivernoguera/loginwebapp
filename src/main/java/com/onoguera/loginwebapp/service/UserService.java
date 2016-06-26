package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.UserDao;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteUser;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oliver on 1/06/16.
 *
 */
public class UserService implements Service {

    private final static UserService INSTANCE = new UserService();
    private final UserDao userDao = UserDao.getInstance();
    private final static UserConverter userConverter = new UserConverter();

    private UserService() {
        super();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public void addUser(final User user) {
        this.userDao.insert(user);
    }

    public User getUser(final String id) {
        return this.userDao.findOne(id);
    }

    public void removeUser(final String id) {
        User user = userDao.findOne(id);
        if (user != null) {
            this.userDao.delete(id);
        }
    }

    public Collection<User> getUsers(){
        return this.userDao.elements();
    }

    public void updateUser(final User user) {
        this.userDao.update(user);
    }

    /**
     * Validate user and returns Roles of this.
     * If user not exist or not validate return null
     *
     * @param user
     * @return user with roles or null if not exist or not valid
     */
    public User validateUser(User user) {

        User userResult = null;
        if (user.getPassword() == null) {
            return userResult;
        }
        User userStore = this.getUser(user.getId());
        if (userStore != null && user.getPassword().equals(userStore.getPassword())) {
            userResult = userStore;
        }
        return userResult;
    }

    public void createUsers(Collection<User> collection) {
        collection.forEach(user -> this.userDao.insert(user));
    }

    public ReadUser getReadUser(final String id) {
        User user = this.userDao.findOne(id);
        if (user == null) {
            return null;
        }
        return  userConverter.entityToReadDTO(user);
    }

    public List<ReadUser> getReadUsers() {
        return this.userDao.elements().stream().map(u -> userConverter.entityToReadDTO(u)).collect(Collectors.toList());
    }

    public void createWriteUsers(List<WriteUser> writeUsers) {
        List<User> users = writeUsers.stream().map(wu -> userConverter.writeDTOtoEntity(wu)).collect(Collectors.toList());
        this.createUsers(users);
    }

    public void removeAllUsers(){
        this.userDao.deleteAll();
    }



    public WriteUser getWriteUser(final String userId) {
        User user = this.userDao.findOne(userId);
        if (user == null) {
            return null;
        }
        return  userConverter.entityToWriteDTO(user);
    }

    public void updateWriteUser(WriteUser writeUser) {
        User user = userConverter.writeDTOtoEntity(writeUser);
        this.updateUser(user);
    }

}
