package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.ProductDetails;
import ims.entities.User;

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

        if(!getRecordsByAttribute(field, brandModel).isEmpty())
            return getRecordsByAttribute(field, brandModel).get(0);
        return new ProductDetails();
    }

}
