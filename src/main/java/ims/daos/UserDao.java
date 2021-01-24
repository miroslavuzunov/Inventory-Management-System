package ims.daos;

import ims.entities.User;

import java.lang.reflect.Field;
import java.util.List;

public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User getUserByUsername(String username) throws NoSuchFieldException {
        Field field = User.class.getDeclaredField("nickname");
        List<User> foundUsers = getRecordsByAttribute(field, username);

        if (!foundUsers.isEmpty())
            return foundUsers.get(0);
        return null;
    }

    public User getUserByEmail(String email) throws NoSuchFieldException {
        Field field = User.class.getDeclaredField("email");
        List<User> foundUsers = getRecordsByAttribute(field, email);

        if (!foundUsers.isEmpty())
            return foundUsers.get(0);
        return null;
    }
}
