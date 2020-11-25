package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.PersonInfo;
import ims.entities.ProductDetails;
import ims.enums.ProductType;

import javax.management.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;

public class ProductDetailsDao  extends AbstractDao<ProductDetails>{
    public ProductDetailsDao() {
        super(ProductDetails.class);
    }

    public DepreciationDegree getDepreciationDegreeReference(Integer id){
        DepreciationDegree reference = manager.getReference(DepreciationDegree.class, id);

        return reference;
    }

    public ProductDetails getProductDetailsByBrandAndModel(String brandModel) throws NoSuchFieldException {
        Field field = ProductDetails.class.getDeclaredField("brandAndModel");
        ProductDetails productDetails =
                getRecordByAttribute(field, brandModel);

        if (productDetails != null)
            return productDetails;
        return new ProductDetails();
    }
}
