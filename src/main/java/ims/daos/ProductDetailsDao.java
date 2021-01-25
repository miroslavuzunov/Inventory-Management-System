package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.entities.User;
import ims.enums.ProductType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;

public class ProductDetailsDao extends AbstractDao<ProductDetails> {
    public ProductDetailsDao() {
        super(ProductDetails.class);
    }

    public DepreciationDegree getDepreciationDegreeReference(Integer id) {
        return manager.getReference(DepreciationDegree.class, id);
    }

    public ProductDetails getProductDetailsByBrandAndModel(String brandModel) throws NoSuchFieldException {
        Field field = ProductDetails.class.getDeclaredField("brandAndModel");
        List<ProductDetails> foundDetails = getRecordsByAttribute(field, brandModel);

        if (!foundDetails.isEmpty())
            return foundDetails.get(0);
        return null;
    }

    public List<ProductDetails> getAllInitiallyLtta() throws NoSuchFieldException {
        Field initialProductType = ProductDetails.class.getDeclaredField("initialProductType");

        return getRecordsByAttribute(initialProductType, ProductType.LTTA);
    }


}
