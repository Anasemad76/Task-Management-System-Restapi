package com.anas.repository;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnection {
    //note this could also be an interface with just one static method and still cannot be initiated
    private final EntityManagerFactory emf;
    public DatabaseConnection() {
        this.emf = Persistence.createEntityManagerFactory("hibernate");

    }
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        emf.close();
    }

//    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("hibernate");
//    public static EntityManager getEntityManager(){
//        //EntityManager is a session for managing database operations.
//        return ENTITY_MANAGER_FACTORY.createEntityManager();
//    }
//    // should be called once, when application shut down
//    public static void close(){
//        ENTITY_MANAGER_FACTORY.close();
//    }

}
