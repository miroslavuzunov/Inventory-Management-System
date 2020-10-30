package ims.daos;

import ims.entities.Country;
import ims.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CountryDao {

    public CountryDao() {
    }

    public List<Country> getAll(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA");
        EntityManager manager = factory.createEntityManager();

        CriteriaQuery<Country> criteria = manager.getCriteriaBuilder().createQuery(Country.class);
        criteria.select(criteria.from(Country.class));
        return manager.createQuery(criteria).getResultList();
    }

    public Country getCountry(Country country) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA");
        EntityManager manager = factory.createEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Country> criteriaQuery = criteriaBuilder.createQuery(Country.class);
        Root<Country> userRoot = criteriaQuery.from(Country.class);

        criteriaQuery.where(criteriaBuilder.equal(userRoot.get("name"), country.getName()));
        List<Country> countries = manager.createQuery(criteriaQuery).getResultList();

        if(!countries.isEmpty())
            return countries.get(0);

        return null;
    }
}
