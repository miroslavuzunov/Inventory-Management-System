package ims.daos;

import ims.entities.City;
import ims.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CityDao {
    public CityDao(){}

    public City getCity(City city){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA");
        EntityManager manager = factory.createEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<City> criteriaQuery = criteriaBuilder.createQuery(City.class);
        Root<City> userRoot = criteriaQuery.from(City.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(userRoot.get("name"), city.getName()));
        predicates.add(criteriaBuilder.equal(userRoot.get("region"), city.getRegion()));

        criteriaQuery.select(userRoot).where(predicates.toArray(new Predicate[]{}));

        List<City> cities = manager.createQuery(criteriaQuery).getResultList();

        if(!cities.isEmpty())
            return cities.get(0);
        return null;
    }
}
