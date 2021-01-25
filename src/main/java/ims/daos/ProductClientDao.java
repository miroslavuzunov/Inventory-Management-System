package ims.daos;

import ims.entities.*;
import ims.enums.RecordStatus;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductClientDao extends AbstractDao<ProductClient> {
    public ProductClientDao() {
        super(ProductClient.class);
    }

    public List<Product> getClientsAllCurrentProducts(User client) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<ProductClient> root = criteriaQuery.from(ProductClient.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("client"), client));
        predicates.add(criteriaBuilder.equal(root.get("status"), RecordStatus.ENABLED));

        criteriaQuery.select(root.get("product")).where(predicates.toArray(new Predicate[]{}));

        return manager.createQuery(criteriaQuery).getResultList();
    }

    public List<ProductClient> getTransactionsByClientAndStatus(User client, RecordStatus recordStatus) throws NoSuchFieldException {
        Map<Field,Object> attributes = new HashMap<>();
        Field clientField = ProductClient.class.getDeclaredField("client");
        Field statusField = ProductClient.class.getDeclaredField("status");

        attributes.put(clientField, client);
        attributes.put(statusField, recordStatus);

        return getRecordsByMultipleAttributes(attributes);
    }

}
