package ims.daos;

import ims.supporting.EntityManagerAssistant;
import ims.entities.Country;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class CountryDao {

    public CountryDao() {}

    public List<Country> getAll(){
        EntityManager manager = EntityManagerAssistant.initEntityManager();

        CriteriaQuery<Country> criteria = manager.getCriteriaBuilder().createQuery(Country.class);
        criteria.select(criteria.from(Country.class));
        List<Country> countries = manager.createQuery(criteria).getResultList();

        EntityManagerAssistant.closeEntityManager(manager);

        return countries;
    }

}
