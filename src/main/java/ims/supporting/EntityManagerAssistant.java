package ims.supporting;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityManagerAssistant<T> {
    private final Class<T> classType;

    public EntityManagerAssistant(Class<T> classType) {
        this.classType = classType;
    }

    private EntityManager initEntityManager() {
        return EntityFactory.getFactory().createEntityManager();
    }

    private void closeEntityManager(EntityManager manager) {
        manager.close();
    }

    private void closeEntityManagerFactory() {
        EntityFactory.closeFactory();
    }

    private void beginTransaction(EntityManager manager) {
        manager.getTransaction().begin();
    }

    private void rollback(EntityManager manager) {
        manager.getTransaction().rollback();
    }

    private void commit(EntityManager manager) {
        manager.getTransaction().commit();
    }

    public List<T> getAll() {
        EntityManager manager = initEntityManager();

        CriteriaQuery<T> criteria = manager.getCriteriaBuilder().createQuery(classType);
        criteria.select(criteria.from(classType));
        List<T> records = manager.createQuery(criteria).getResultList();

        closeEntityManager(manager);

        return records;
    }

    public T getRecordByAttribute(String columnName, String value) {
        EntityManager manager = initEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(classType);
        Root<T> recordRoot = criteriaQuery.from(classType);
        criteriaQuery.where(criteriaBuilder.equal(recordRoot.get(columnName), value));
        List<T> records = manager.createQuery(criteriaQuery).getResultList();

        closeEntityManager(manager);

        if (!records.isEmpty())
            return records.get(0);
        return null;
    }

    public T getRecordByMultipleAttributes(Map<String, String> valuesByColumns) {
        EntityManager manager = initEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(classType);
        Root<T> recordRoot = criteriaQuery.from(classType);
        List<Predicate> predicates = new ArrayList<>();

        valuesByColumns.forEach((key, value) -> predicates.add(criteriaBuilder.like(recordRoot.get(key), value)));

        criteriaQuery.select(recordRoot).where(predicates.toArray(new Predicate[]{}));
        List<T> records = manager.createQuery(criteriaQuery).getResultList();

        closeEntityManager(manager);

        if (!records.isEmpty())
            return records.get(0);
        return null;
    }

    public T getRecordReference(Integer id) {
        EntityManager manager = initEntityManager();
        T reference = manager.getReference(classType, id);
        closeEntityManager(manager);

        return reference;
    }

    public void updateRecord(T record) {
        EntityManager manager = initEntityManager();
        beginTransaction(manager);

        manager.merge(record);

        commit(manager);
        closeEntityManager(manager);
    }
}
