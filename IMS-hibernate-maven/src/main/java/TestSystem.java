import javax.persistence.*;
import java.util.List;

public class TestSystem {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("IMS-hibernate-maven");

    public static void main(String[] args) {
        addCity(1, "Varna", "NorthEast");
        addCity(2, "Sofia", "SouthWest");
        addCity(3, "Qmbol", "SouthEast");
        getCity("Sofia");
        updateName(2, "Ruse");
        updateRegion(2, "North Central");
        deleteCity(1);

// This allows us to update,create,delete and so on in the DB and we have to close it.
        ENTITY_MANAGER_FACTORY.close();

    }

    public static void addCity(int id, String name, String region) { // Add int id if we want to add the id explicitly.
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            City city = new City();
            city.setId(id);
            city.setName(name);
            city.setRegion(region);
            em.persist(city);
            et.commit();
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void getCity(String name) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "SELECT * FROM City c WHERE c.name = :name";

        TypedQuery<City> tq = em.createQuery(query, City.class);
        tq.setParameter("name", name);
        City city = null;
        try {
            city = tq.getSingleResult();
            System.out.println(city.getId() + " " + city.getName() + " " +
                    city.getRegion());
        } catch (NoResultException e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void getCities() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "SELECT * FROM city c";
        TypedQuery<City> tq = em.createQuery(query, City.class);
        List<City> cities;
        try {
            cities = tq.getResultList();
            // TODO: Implement check if we actually got results
            cities.forEach(city -> System.out.println(city.getId() + " " + city.getName() +
                    " " + city.getRegion()));
        } catch (NoResultException e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void updateName(int id, String name) { // Add int id if we want to add the id explicitly.
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        City city = null;
        try {
            et = em.getTransaction();
            et.begin();
            city = em.find(City.class, id);
            //TODO: Verify that in city it came through and its not null
            city.setName(name);
            em.persist(city);
            et.commit();
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void updateRegion(int id, String region) { // Add int id if we want to add the id explicitly.
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        City city = null;
        try {
            et = em.getTransaction();
            et.begin();
            city = em.find(City.class, id);
            //TODO: Verify that in city it came through and its not null
            city.setRegion(region);
            em.persist(city);
            et.commit();
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void deleteCity(int id) { // Add int id if we want to add the id explicitly.
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        City city = null;
        try {
            et = em.getTransaction();
            et.begin();
            city = em.find(City.class, id);
            //TODO: Verify that in city it came through and its not null
            em.remove(city);
            em.persist(city);
            et.commit();
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

}
