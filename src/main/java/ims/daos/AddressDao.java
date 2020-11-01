package ims.daos;

import ims.entities.Address;
import ims.entities.City;
import ims.entities.Country;
import ims.entities.PersonInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AddressDao {
    private EntityManagerFactory factory;
    private EntityManager manager;

    public AddressDao() {
         factory = Persistence.createEntityManagerFactory("JPA");
         manager = factory.createEntityManager();
    }

    public void beginTransaction(){
        manager.getTransaction().begin();
    }

    public void commitTransaction(){
        manager.getTransaction().commit();
    }

    public City getAddressReferenceToCity(Address address, Integer cityId) {
        return manager.getReference(City.class, cityId);
    }

}
