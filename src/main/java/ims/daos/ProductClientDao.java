package ims.daos;

import ims.entities.*;
import ims.enums.RecordStatus;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<ProductClient> getTransactionsByClientAndStatus(User client, RecordStatus recordStatus) throws NoSuchFieldException {
        Map<Field,Object> attributes = new HashMap<>();
        Field clientField = ProductClient.class.getDeclaredField("client");
        Field statusField = ProductClient.class.getDeclaredField("status");

        attributes.put(clientField, client);
        attributes.put(statusField, recordStatus);

        List<ProductClient> transactions = getRecordsByMultipleAttributes(attributes);

        return transactions;
    }
}
