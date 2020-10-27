package ims.daos;

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

    }

    public User getUserByUsername(String username){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA");
        EntityManager manager = factory.createEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        criteriaQuery.where(criteriaBuilder.equal(userRoot.get("nickname"), username));
        List<User> users = manager.createQuery(criteriaQuery).getResultList();

        if(!users.isEmpty())
            return users.get(0);
        return null;
    }


}
