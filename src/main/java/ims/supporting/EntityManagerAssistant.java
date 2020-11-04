package ims.supporting;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerAssistant {
    private static final EntityManagerFactory factory;

    static{
        factory = Persistence.createEntityManagerFactory("JPA");
    }

    public static EntityManager initEntityManager(){
        return factory.createEntityManager();
    }

    public static void closeEntityManager(EntityManager manager){
        manager.close();
    }

    public static void closeEntityManagerFactory(){
        factory.close();
    }

    public static void beginTransaction(EntityManager manager){
        manager.getTransaction().begin();
    }

    public static void rollback(EntityManager manager){
        manager.getTransaction().rollback();
    }

    public static void commit(EntityManager manager){
        manager.getTransaction().commit();
    }
}
