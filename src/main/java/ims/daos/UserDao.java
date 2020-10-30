package ims.daos;

import ims.entities.Address;
import ims.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDao {

    public UserDao(){}

    public void addUser(User user){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA");
        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(user);
        manager.getTransaction().commit();
    }

    public User getUserByField(String column, String value){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA");
        EntityManager manager = factory.createEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        criteriaQuery.where(criteriaBuilder.equal(userRoot.get(column), value));
        List<User> users = manager.createQuery(criteriaQuery).getResultList();

        if(!users.isEmpty())
            return users.get(0);
        return null;
    }
}
