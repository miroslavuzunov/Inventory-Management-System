package ims.daos;

import ims.entities.User;

import java.lang.reflect.Field;

public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User getUserByUsername(String username) throws NoSuchFieldException {
        Field field = User.class.getDeclaredField("nickname");
        User user =
                getRecordsByAttribute(field, username).get(0);

        if (user != null)
            return user;
        return new User();
    }

    public User getUserByEmail(String email) throws NoSuchFieldException {
        Field field = User.class.getDeclaredField("email");
        User user =
                getRecordsByAttribute(field, email).get(0);

        if (user != null)
            return user;
        return new User();
    }
}
