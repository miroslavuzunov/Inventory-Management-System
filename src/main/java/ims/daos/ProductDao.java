package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.PersonInfo;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.enums.RecordStatus;

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

    public List<Product> getAllEnabled() throws NoSuchFieldException {
        Field field = Product.class.getDeclaredField("status");

        return getRecordsByAttribute(field, RecordStatus.ENABLED);
    }
}
