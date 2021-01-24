package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.PersonInfo;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.enums.RecordStatus;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class ProductDao extends AbstractDao<Product> {
    public ProductDao() {
        super(Product.class);
    }

    public Product getLastRecord() {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> recordRoot = criteriaQuery.from(Product.class);
        criteriaQuery.select(recordRoot);
        criteriaQuery.orderBy(criteriaBuilder.desc(recordRoot.get("id")));

        List<Product> products = manager.createQuery(criteriaQuery).getResultList();

        if (!products.isEmpty())
            return products.get(0);
        return null;
    }

    public List<Product> getAllEnabled() throws NoSuchFieldException {
        Field field = Product.class.getDeclaredField("status");

        return getRecordsByAttribute(field, RecordStatus.ENABLED);
    }

    public List<Product> getAllProductsByMultipleProductDetails(List<ProductDetails> productsDetails){
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> recordRoot = criteriaQuery.from(Product.class);

        CriteriaBuilder.In<ProductDetails> in = criteriaBuilder.in(recordRoot.get("productDetails"));

        for(ProductDetails details : productsDetails){
            in.value(details);
        }

        criteriaQuery.select(recordRoot).where(in);

        return manager.createQuery(criteriaQuery).getResultList();
    }
}
