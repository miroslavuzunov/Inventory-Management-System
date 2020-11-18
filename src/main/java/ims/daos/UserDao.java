package ims.daos;

import ims.entities.City;
import ims.entities.User;
import ims.enums.Role;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User getUserByUsername(String username) {
       return getUserByField("nickname", username, User.class);
    }

    public User getUserByEmail(String email) {
       return getUserByField("email", email, User.class);
    }

    private <T> T getUserByField(String fieldName, String value, Class<T> classType) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(classType);
        Root<T> recordRoot = criteriaQuery.from(classType);
        criteriaQuery.where(criteriaBuilder.equal(recordRoot.get(fieldName), value));
        List<T> records = manager.createQuery(criteriaQuery).getResultList();

        if (!records.isEmpty())
            return records.get(0);
        return null;
    }
}
