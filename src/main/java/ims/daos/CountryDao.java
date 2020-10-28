package ims.daos;

import ims.entities.Country;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
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
}
