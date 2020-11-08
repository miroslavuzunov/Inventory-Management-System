package ims.daos;

import ims.supporting.EntityManagerAssistant;
import ims.entities.User;

public class UserDao extends EntityManagerAssistant<User> {

    public UserDao(Class<User> classType) {
        super(classType);
    }

    public User getUserByField(String column, String value) {
        return getRecordByAttribute(column, value);
    }
}
