package ims.daos;

import ims.entities.Country;

import java.util.List;

public class CountryDao extends AbstractDao<Country> {

    public CountryDao() {
        super(Country.class);
    }

}
