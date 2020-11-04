package ims.daos;

import ims.supporting.EntityManagerAssistant;
import ims.entities.Address;
import ims.entities.City;

import javax.persistence.EntityManager;

public class AddressDao {

    public AddressDao() {}

    public City getAddressReferenceToCity(Address address, Integer cityId) {
        EntityManager manager = EntityManagerAssistant.initEntityManager();

        City city = manager.getReference(City.class, cityId);

        EntityManagerAssistant.closeEntityManager(manager);

        return city;
    }

}
