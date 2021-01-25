package ims.daos;

import ims.entities.City;
import ims.entities.Country;
import ims.entities.ProductClient;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityDao extends AbstractDao<City> {

    public CityDao() {
        super(City.class);
    }

    public City getRecordByNameAndRegion(String name, String region) throws NoSuchFieldException {
        Map<Field,Object> attributes = new HashMap<>();
        Field nameField = City.class.getDeclaredField("name");
        Field regionField = City.class.getDeclaredField("region");

        attributes.put(nameField, name);
        attributes.put(regionField, region);

        List<City> foundCities = getRecordsByMultipleAttributes(attributes);

        if(!foundCities.isEmpty())
            return foundCities.get(0);
        return null;
    }
}
