package ims.daos;

import ims.entities.Product;
import ims.entities.ProductClient;
import ims.entities.ProductDetails;
import ims.entities.User;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.List;

public class ProductClientDao extends AbstractDao<ProductClient> {
    public ProductClientDao() {
        super(ProductClient.class);
    }

    public List<Product> getClientsAllProducts(User client) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<ProductClient> root = criteriaQuery.from(ProductClient.class);
        criteriaQuery.select(root.get("product")).where(criteriaBuilder.equal(root.get("client"), client));
        List<Product> records = manager.createQuery(criteriaQuery).getResultList();

        return records;
    }
}
