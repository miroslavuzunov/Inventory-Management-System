package ims.supporting;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityFactory {
    private static final EntityManagerFactory factory;

    static{
        factory = Persistence.createEntityManagerFactory("JPA");
    }

    public static EntityManagerFactory getFactory(){
        return factory;
    }

    public static void closeFactory(){
        factory.close();
    }

}
