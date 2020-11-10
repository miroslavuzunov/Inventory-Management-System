package ims.daos;

import ims.supporting.EntityFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;
import java.util.List;
import java.util.Set;

public abstract class AbstractDao<T> {
    private Class<T> classType;
    protected EntityManager manager;

    protected AbstractDao(Class<T> classType) {
        initEntityManager();

        if (isEntity(classType))
            this.classType = classType;
    }

    protected void initEntityManager() {
        this.manager = EntityFactory.getFactory().createEntityManager();
    }

    protected void closeEntityManager() {
        this.manager.close();
    }

    protected void beginTransaction() {
        manager.getTransaction().begin();
    }

    protected void rollback() {
        manager.getTransaction().rollback();
    }

    protected void commit() {
        manager.getTransaction().commit();
    }

    public void saveRecord(T record) {
        initEntityManager();
        beginTransaction();

        manager.persist(record);

        commit();
        closeEntityManager();
    }

    public void updateRecord(T record) {
        initEntityManager();
        beginTransaction();

        manager.merge(record);

        commit();
        closeEntityManager();
    }

    public void deleteRecord(T record) {
        initEntityManager();
        beginTransaction();

        manager.remove(record);

        commit();
        closeEntityManager();
    }

    public T getOne(Long id) {
        return manager.find(classType, id);
    }

    public List<T> getAll() {
        initEntityManager();

        CriteriaQuery<T> criteria = manager.getCriteriaBuilder().createQuery(classType);
        criteria.select(criteria.from(classType));
        List<T> records = manager.createQuery(criteria).getResultList();

        closeEntityManager();

        return records;
    }

    private boolean isEntity(Class<?> clazz) { //Checks if given class type is entity (ensures type safety)
        boolean entityFound = false;
        Set<EntityType<?>> entities = manager.getMetamodel().getEntities();

        for (EntityType<?> entityType : entities) {
            Class<?> entityClass = entityType.getJavaType();
            if (entityClass.equals(clazz))
                entityFound = true;
        }
        return entityFound;
    }
}
