package ims.daos;

import ims.entities.City;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityDao extends AbstractDao<City> {

    public CityDao() {
        super(City.class);
    }

    public City getRecordByNameAndRegion(String name, String region) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<City> criteriaQuery = criteriaBuilder.createQuery(City.class);
        Root<City> recordRoot = criteriaQuery.from(City.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.like(recordRoot.get("name"), name));
        predicates.add(criteriaBuilder.like(recordRoot.get("region"), region));

        criteriaQuery.select(recordRoot).where(predicates.toArray(new Predicate[]{}));
        List<City> records = manager.createQuery(criteriaQuery).getResultList();

        if (!records.isEmpty())
            return records.get(0);
        return null;
    }
}
