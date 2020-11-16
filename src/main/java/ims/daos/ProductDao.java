package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.Product;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


public class ProductDao extends AbstractDao<Product> {
    public ProductDao() {
        super(Product.class);
    }

    public Product getLastRecord() {
        Query query = manager.createQuery("SELECT row FROM Product row ORDER BY row.id DESC");

        List<Product> products = query.getResultList();

        if (!products.isEmpty())
            return products.get(0);
        return new Product();
    }

    public Product getProductByInvNumber(String invNum) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> recordRoot = criteriaQuery.from(Product.class);
        criteriaQuery.where(criteriaBuilder.equal(recordRoot.get("inventoryNumber"), invNum));
        List<Product> records = manager.createQuery(criteriaQuery).getResultList();

        if (!records.isEmpty())
            return records.get(0);
        return new Product();
    }
}
