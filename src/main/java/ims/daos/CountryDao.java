package ims.daos;

import ims.supporting.EntityManagerAssistant;
import ims.entities.Country;

import java.util.List;

public class CountryDao extends EntityManagerAssistant<Country> {

    public CountryDao(Class<Country> classType) {
        super(classType);
    }

    public List<Country> getAllCountries() {
        return getAll();
    }

}
