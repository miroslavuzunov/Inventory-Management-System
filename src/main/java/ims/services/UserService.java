package ims.services;

import ims.daos.UserDao;
import ims.entities.User;

public class UserService {
    private final UserDao userDao;

    public UserService(){
        userDao = new UserDao();
    }

    public User getUserByUsername(String username) throws NoSuchFieldException {
        return userDao.getUserByUsername(username);
    }


}
