package ims.daos;

import ims.entities.DepreciationDegree;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DepreciationDegreeDao extends AbstractDao<DepreciationDegree> {

    public DepreciationDegreeDao() {
        super(DepreciationDegree.class);
    }

    public DepreciationDegree getRecordByCategory(String category){
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<DepreciationDegree> criteriaQuery = criteriaBuilder.createQuery(DepreciationDegree.class);
        Root<DepreciationDegree> recordRoot = criteriaQuery.from(DepreciationDegree.class);
        criteriaQuery.where(criteriaBuilder.equal(recordRoot.get("category"), category));
        List<DepreciationDegree> records = manager.createQuery(criteriaQuery).getResultList();

        if (!records.isEmpty())
            return records.get(0);
        return new DepreciationDegree();
    }
}
