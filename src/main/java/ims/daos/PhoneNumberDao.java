package ims.daos;

import ims.entities.PhoneNumber;
import ims.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PhoneNumberDao {
    public PhoneNumberDao() {
    }

    public void addPhone(PhoneNumber phoneNumber){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA");
        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        manager.merge(phoneNumber);
        manager.getTransaction().commit();
    }
}
