package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.Product;
import ims.entities.ProductDetails;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
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
        return new Product();
    }
}
