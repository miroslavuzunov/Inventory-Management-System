package ims.daos;

import ims.entities.PhoneNumber;
import ims.supporting.EntityManagerAssistant;
import ims.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDao {

    public UserDao(){}

    public void addUser(User user, PhoneNumber phoneNumber){
        EntityManager manager = EntityManagerAssistant.initEntityManager();
        EntityManagerAssistant.beginTransaction(manager);

        manager.merge(user); //save or update

        EntityManagerAssistant.commit(manager);
        EntityManagerAssistant.closeEntityManager(manager);
    }

    public User getUserByField(String column, String value){
        EntityManager manager = EntityManagerAssistant.initEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(userRoot.get(column), value));
        List<User> users = manager.createQuery(criteriaQuery).getResultList();

        EntityManagerAssistant.closeEntityManager(manager);

        if(!users.isEmpty())
            return users.get(0);
        return null;
    }
}
