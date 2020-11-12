package ims.daos;

import ims.supporting.EntityFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;
import java.util.List;
import java.util.Set;

public abstract class AbstractDao<T> {
    private Class<T> classType;
    protected static EntityManager manager;

    static{
        manager = EntityFactory.getFactory().createEntityManager();
    }

    protected AbstractDao(Class<T> classType) {
        if (isEntity(classType))
            this.classType = classType;
    }

    public static void closeEntityManager() {
        manager.close();
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

    public T getOne(Long id) {
        return manager.find(classType, id);
    }

    public List<T> getAll() {

        CriteriaQuery<T> criteria = manager.getCriteriaBuilder().createQuery(classType);
        criteria.select(criteria.from(classType));
        List<T> records = manager.createQuery(criteria).getResultList();

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
