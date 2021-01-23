package ims.daos;

import ims.entities.User;

import java.lang.reflect.Field;

public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User getUserByUsername(String username) throws NoSuchFieldException {
        Field field = User.class.getDeclaredField("nickname");

        if(!getRecordsByAttribute(field, username).isEmpty())
           return getRecordsByAttribute(field, username).get(0);
        return null;
    }

    public User getUserByEmail(String email) throws NoSuchFieldException {
        Field field = User.class.getDeclaredField("email");

        if(!getRecordsByAttribute(field, email).isEmpty())
            return getRecordsByAttribute(field, email).get(0);
        return null;
    }
}
