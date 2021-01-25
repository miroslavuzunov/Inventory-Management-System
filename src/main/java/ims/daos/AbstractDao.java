package ims.daos;

import ims.supporting.EntityFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.lang.reflect.Field;
import java.util.*;

public abstract class AbstractDao<T> {
    private Class<T> classType;
    protected static EntityManager manager;

    protected AbstractDao(Class<T> classType) {
        this.classType = classType;
    }

    public static void newEntityManager() {
        manager = EntityFactory.getFactory().createEntityManager();
    }

    public static void closeEntityManager() {
        manager.close();
    }

    protected void beginTransaction() {
        if (isEntity(classType))           //TODO catch exception
            manager.getTransaction().begin();
    }

    protected void rollback() {
        manager.getTransaction().rollback();
    }

    protected void commit() {
        manager.getTransaction().commit();
    }

    public void saveRecord(T record) {
        beginTransaction();

        manager.persist(record);

        commit();
    }

    public void updateRecord(T record) {
        beginTransaction();

        manager.merge(record);

        commit();
    }

    public void deleteRecord(T record) {
        beginTransaction();

        manager.remove(record);

        commit();
    }

    public T getOne(Integer id) {
        return manager.find(classType, id);
    }

    public List<T> getAll() {
        CriteriaQuery<T> criteria = manager.getCriteriaBuilder().createQuery(classType);
        criteria.select(criteria.from(classType));

        return manager.createQuery(criteria).getResultList();
    }

    protected List<T> getRecordsByAttribute(Field field, Object value){
        boolean isFieldExisting = Arrays.stream(classType.getDeclaredFields()).anyMatch(f -> f.getName().equals(field.getName())); //Checks if the field is in classType

        if (isFieldExisting) {
            CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(classType);
            Root<T> recordRoot = criteriaQuery.from(classType);
            criteriaQuery.where(criteriaBuilder.equal(recordRoot.get(field.getName()), value));

            return manager.createQuery(criteriaQuery).getResultList();
        }
        return null;
    }

    public List<T> getRecordsByMultipleAttributes(Map<Field, Object> valuesByColumns) {

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(classType);
        Root<T> recordRoot = criteriaQuery.from(classType);
        List<Predicate> predicates = new ArrayList<>();

        valuesByColumns.forEach((key, value) -> predicates.add(criteriaBuilder.equal(recordRoot.get(key.getName()), value)));

        criteriaQuery.select(recordRoot).where(predicates.toArray(new Predicate[]{}));

        return manager.createQuery(criteriaQuery).getResultList();
    }

    private boolean isEntity(Class<?> clazz) { //Checks if given class type is entity (ensures type safety)
        boolean entityFound = false;
        Set<EntityType<?>> entities = manager.getMetamodel().getEntities();

        for (EntityType<?> entityType : entities) {
            Class<?> entityClass = entityType.getJavaType(); //Wildcard used because generic type is unknown during compile time
            if (entityClass.equals(clazz))
                entityFound = true;
        }
        return entityFound;
    }
}
