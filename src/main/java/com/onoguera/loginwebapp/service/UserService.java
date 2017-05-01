package com.onoguera.loginwebapp.service;

import com.onoguera.loginwebapp.dao.Dao;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by oliver on 1/06/16.
 *
 */
public class UserService implements UserServiceInterface {

    private final static UserService INSTANCE = new UserService();
    private Dao userDao;
    private RoleServiceInterface roleService;
    private final static UserConverter userConverter = UserConverter.getInstance();
    private final static RoleConverter roleConverter = RoleConverter.getInstance();

    private UserService() {
        super();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }


    public void setUserDao(Dao userDao) {
        this.userDao = userDao;
    }

    public void setRoleService(RoleServiceInterface roleService) {
        this.roleService = roleService;
    }

    public User getUser(final String id) {
        return (User)this.userDao.findOne(id);
    }

    @Override
    public List<ReadUser> getReadUsers() {
        return this.getUsers().stream().map(u -> UserConverter.getInstance().entityToReadDTO(u)).collect(Collectors.toList());

    }
    private List<User> getUsers(){
        List<User> users = ( List<User>)this.userDao.elements().stream().collect(Collectors.toList());
        return users;
    }

    public ReadUser getReadUser(final String id) {
        User user = this.getUser(id);
        if (user == null) {
            return null;
        }
        return  UserConverter.getInstance().entityToReadDTO(user);
    }

    public void removeUser(final String id) {
        User user = this.getUser(id);
        if (user != null) {
            this.userDao.delete(id);
        }
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

    @Override
    public void removeUsers() {
        deleteUsers(this.getUsers());
    }

    @Override
    public boolean setUsers(List<WriteUser> writeUsers) {

        List<User> users = userConverter.writeDTOsToEntityList(writeUsers);
        if(this.checkUsers(users)){
            this.removeUsers();
            this.upsertUsersDTO(users);
            return true;
        }else{
            return false;
        }
    }

    private void deleteUsers(List<User> users) {
        for( User user: users){
            this.userDao.delete(user.getId());
        }
    }

    private boolean upsertUsersDTO(List<User> users) {

        if(this.checkUsers(users)){
            this.deleteUsers(users);
            for(User user: users){
                this.upsertUser(user);
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean upsertRolesOfUser(String userId, List<WriteRole> roles) {

        User user = this.getUser(userId);
        if( user == null){
            return false;
        }

        Set<Role> userRoles = new HashSet(user.getRoles());
        userRoles.addAll(roleConverter.writeDTOsToEntityList(roles));
        List<Role> updatedRoles = new ArrayList(userRoles);
        user.setRoles(updatedRoles);
        this.userDao.update(user);
        return true;

    }

    private boolean checkUsers(List<User> users) {

        for (User user : users) {
            User userStore = this.getUser(user.getId());
            if (validUser(user)) {
                if (!roleService.existsRoles(user.getRoles())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean upsertUser(WriteUser writeUser) {
        User user = userConverter.writeDTOtoEntity(writeUser);
        return upsertUser(user);
    }

    public boolean upsertUser(User user) {
        
        if(this.checkUsers(Arrays.asList(user))){
            this.deleteUsers(Arrays.asList(user));
            this.userDao.insert(user);
            return true;
        }else{
            return false;
        }
    }
    private static boolean validUser(User user) {
        if( user == null || user.getId() == null || user.getId().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ) {
            return false;
        }else{
            return true;
        }
    }

}
