package ims.daos;

import ims.entities.PersonInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

public class PersonInfoDao extends AbstractDao<PersonInfo> {

    public PersonInfoDao() {
        super(PersonInfo.class);
    }

    public PersonInfo getRecordByEgnAndPeriod(String egn) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<PersonInfo> criteriaQuery = criteriaBuilder.createQuery(PersonInfo.class);
        Root<PersonInfo> recordRoot = criteriaQuery.from(PersonInfo.class);
        criteriaQuery.where(criteriaBuilder.equal(recordRoot.get("egn"), egn));
        List<PersonInfo> records = manager.createQuery(criteriaQuery).getResultList();

        if (!records.isEmpty())
            return records.get(0);
        return new PersonInfo();
    }
}
