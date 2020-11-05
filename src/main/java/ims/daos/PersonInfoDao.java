package ims.daos;

import ims.entities.PersonInfo;
import ims.entities.User;
import ims.supporting.EntityManagerAssistant;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class PersonInfoDao {
    public PersonInfoDao() {
    }

    public PersonInfo getPersonInfoByEgn(String egn){
        EntityManager manager = EntityManagerAssistant.initEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<PersonInfo> criteriaQuery = criteriaBuilder.createQuery(PersonInfo.class);
        Root<PersonInfo> userRoot = criteriaQuery.from(PersonInfo.class);
        criteriaQuery.where(criteriaBuilder.equal(userRoot.get("egn"), egn));
        List<PersonInfo> personInfos = manager.createQuery(criteriaQuery).getResultList();

        EntityManagerAssistant.closeEntityManager(manager);

        if(!personInfos.isEmpty())
            return personInfos.get(0);
        return null;
    }
}
