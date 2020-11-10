package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.ProductDetails;

public class ProductDetailsDao  extends AbstractDao<ProductDetails>{
    public ProductDetailsDao() {
        super(ProductDetails.class);
    }

    public DepreciationDegree getDepreciationDegreeReference(Integer id){
        initEntityManager();

        DepreciationDegree reference = manager.getReference(DepreciationDegree.class, id);

        closeEntityManager();

        return reference;
    }
}
