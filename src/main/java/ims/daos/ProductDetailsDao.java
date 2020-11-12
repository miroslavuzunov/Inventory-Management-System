package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.ProductDetails;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ProductDetailsDao  extends AbstractDao<ProductDetails>{
    public ProductDetailsDao() {
        super(ProductDetails.class);
    }

    public DepreciationDegree getDepreciationDegreeReference(Integer id){
        DepreciationDegree reference = manager.getReference(DepreciationDegree.class, id);

        return reference;
    }

    public ProductDetails getProductDetailsByBrandAndModel(String brandModel) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<ProductDetails> criteriaQuery = criteriaBuilder.createQuery(ProductDetails.class);
        Root<ProductDetails> recordRoot = criteriaQuery.from(ProductDetails.class);
        criteriaQuery.where(criteriaBuilder.equal(recordRoot.get("brand_model"), brandModel));
        List<ProductDetails> records = manager.createQuery(criteriaQuery).getResultList();

        if (!records.isEmpty())
            return records.get(0);
        return null;
    }
}
