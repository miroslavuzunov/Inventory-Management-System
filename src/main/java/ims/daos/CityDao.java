package ims.daos;

import ims.supporting.EntityManagerAssistant;
import ims.entities.City;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityDao extends EntityManagerAssistant<City> {

    public CityDao(Class<City> classType) {
        super(classType);
    }

    public List<City> getAllCities() {
        return getAll();
    }

    public City getCity(City city) {
        Map<String, String> valuesByAttributes = new HashMap<>();
        valuesByAttributes.put("name", city.getName());
        valuesByAttributes.put("region", city.getRegion());

        return getRecordByMultipleAttributes(valuesByAttributes);
    }
}
