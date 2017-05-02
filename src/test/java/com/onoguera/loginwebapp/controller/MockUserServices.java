package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;
import com.onoguera.loginwebapp.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by olivernoguera on 01/05/2017.
 */
class MockUserServices {

    public static class UserServiceMockValidateOK  implements UserService {


        @Override
        public User validateUser(User user) {
            return user;
        }

        @Override
        public List<ReadUser> getReadUsers() {
            return null;
        }

        @Override
        public User getUser(String userId) {
            return null;
        }

        @Override
        public ReadUser getReadUser(String userId) {
            return null;
        }

        @Override
        public boolean upsertUser(WriteUser writeUser) {
            return false;
        }

        @Override
        public boolean upsertUser(User user) {
            return false;
        }

        @Override
        public void removeUser(String id) {

        }

        @Override
        public boolean setUsers(List<WriteUser> users) {
            return false;
        }

        @Override
        public boolean upsertRolesOfUser(String userId, List<WriteRole> newroles) {
            return false;
        }

        @Override
        public void removeUsers() {

        }
    }


    public static class UserServiceMockValidateOKWithRoles  implements UserService {


        @Override
        public User validateUser(User user) {
            User userWithRoles = new User("test","test", Arrays.asList(new Role("PAGE1")));
            return userWithRoles;
        }

        @Override
        public List<ReadUser> getReadUsers() {
            return null;
        }

        @Override
        public User getUser(String userId) {
            return null;
        }

        @Override
        public ReadUser getReadUser(String userId) {
            return null;
        }

        @Override
        public boolean upsertUser(WriteUser writeUser) {
            return false;
        }

        @Override
        public boolean upsertUser(User user) {
            return false;
        }

        @Override
        public void removeUser(String id) {

        }

        @Override
        public boolean setUsers(List<WriteUser> users) {
            return false;
        }

        @Override
        public boolean upsertRolesOfUser(String userId, List<WriteRole> newroles) {
            return false;
        }

        @Override
        public void removeUsers() {

        }
    }

    public static class UserServiceMockValidateKO implements UserService {

        @Override
        public User validateUser(User user) {
            return null;
        }

        @Override
        public List<ReadUser> getReadUsers() {
            return null;
        }

        @Override
        public User getUser(String userId) {
            return null;
        }

        @Override
        public ReadUser getReadUser(String userId) {
            return null;
        }

        @Override
        public boolean upsertUser(WriteUser writeUser) {
            return false;
        }

        @Override
        public boolean upsertUser(User user) {
            return false;
        }

        @Override
        public void removeUser(String id) {

        }

        @Override
        public boolean setUsers(List<WriteUser> users) {
            return false;
        }

        @Override
        public boolean upsertRolesOfUser(String userId, List<WriteRole> newroles) {
            return false;
        }

        @Override
        public void removeUsers() {

        }
    }
}
