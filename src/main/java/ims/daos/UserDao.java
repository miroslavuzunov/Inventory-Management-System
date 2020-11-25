package ims.daos;

import ims.entities.City;
import ims.entities.DepreciationDegree;
import ims.entities.ProductDetails;
import ims.entities.User;
import ims.enums.Role;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User getUserByUsername(String username) throws NoSuchFieldException {
        Field field = User.class.getDeclaredField("nickname");
        User user =
                getRecordByAttribute(field, username);

        if (user != null)
            return user;
        return new User();
    }

    public User getUserByEmail(String email) throws NoSuchFieldException {
        Field field = User.class.getDeclaredField("email");
        User user =
                getRecordByAttribute(field, email);

        if (user != null)
            return user;
        return new User();
    }
}
