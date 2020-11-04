package ims.daos;

import ims.supporting.EntityManagerAssistant;
import ims.entities.City;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CityDao {

    public List<City> getAll(){
        EntityManager manager = EntityManagerAssistant.initEntityManager();

        CriteriaQuery<City> criteria = manager.getCriteriaBuilder().createQuery(City.class);
        criteria.select(criteria.from(City.class));
        List<City> cities = manager.createQuery(criteria).getResultList();

        EntityManagerAssistant.closeEntityManager(manager);

        return cities;
    }

    public City getCity(City city){
        EntityManager manager = EntityManagerAssistant.initEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<City> criteriaQuery = criteriaBuilder.createQuery(City.class);
        Root<City> userRoot = criteriaQuery.from(City.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(userRoot.get("name"), city.getName()));
        predicates.add(criteriaBuilder.equal(userRoot.get("region"), city.getRegion()));
        criteriaQuery.select(userRoot).where(predicates.toArray(new Predicate[]{}));
        List<City> cities = manager.createQuery(criteriaQuery).getResultList();

        EntityManagerAssistant.closeEntityManager(manager);

        if(!cities.isEmpty())
            return cities.get(0);
        return null;
    }

}
