package ims.daos;

import ims.entities.City;
import ims.entities.DepreciationDegree;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DepreciationDegreeDao extends AbstractDao<DepreciationDegree> {

    public DepreciationDegreeDao() {
        super(DepreciationDegree.class);
    }

    public DepreciationDegree getRecordByCategoryAndGroup(String category, String group){
        initEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<DepreciationDegree> criteriaQuery = criteriaBuilder.createQuery(DepreciationDegree.class);
        Root<DepreciationDegree> recordRoot = criteriaQuery.from(DepreciationDegree.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.like(recordRoot.get("category"), category));
        predicates.add(criteriaBuilder.like(recordRoot.get("product_group"), group));

        criteriaQuery.select(recordRoot).where(predicates.toArray(new Predicate[]{}));
        List<DepreciationDegree> records = manager.createQuery(criteriaQuery).getResultList();

        closeEntityManager();

        if (!records.isEmpty())
            return records.get(0);
        return null;
    }
}
