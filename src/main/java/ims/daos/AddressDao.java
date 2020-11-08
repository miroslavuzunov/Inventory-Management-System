package ims.daos;

import ims.supporting.EntityManagerAssistant;
import ims.entities.Address;
import ims.entities.City;

public class AddressDao extends EntityManagerAssistant<City> {

    public AddressDao(Class<City> classType) {
        super(classType);
    }

    public void setAddressReferenceToCity(Address address, Integer cityId) {
        address.setCity(getRecordReference(cityId));
    }

}
