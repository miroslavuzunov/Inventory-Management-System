package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.ProductDetails;

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
                getRecordsByAttribute(field, brandModel).get(0);

        if (productDetails != null)
            return productDetails;
        return new ProductDetails();
    }

}
