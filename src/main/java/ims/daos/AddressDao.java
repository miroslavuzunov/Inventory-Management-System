package ims.daos;

import ims.entities.Address;
import ims.entities.City;

public class AddressDao extends AbstractDao<Address> {

    public AddressDao() {
        super(Address.class);
    }

    public City getCityReference(Integer id) {
        initEntityManager();

        City reference = manager.getReference(City.class, id);

        closeEntityManager();

        return reference;
    }

}
