package ims.services;

import ims.daos.UserDao;
import ims.entities.User;

public class UserService {
    private final UserDao userDao;

    public UserService(){
        userDao = new UserDao(User.class);
    }

    public User getUserByUsername(String username){
        return userDao.getUserByField("nickname", username);
    }


}
