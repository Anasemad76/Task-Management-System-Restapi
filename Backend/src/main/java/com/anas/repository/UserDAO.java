package com.anas.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import com.anas.model.user.Admin;
import com.anas.model.user.User;
import com.anas.model.user.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.anas.service.TaskManager;

@Repository
public class UserDAO {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public UserDAO(DatabaseConnection dbConnection) {

        this.em = dbConnection.getEntityManager();
    }

    public boolean registerUserDB(User user){
        // With a Transaction, it either ALL succeeds or ALL fails(Moslty used with write operations)
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) { // tx not commited and still active locking database resources
                tx.rollback(); // free database resources and undo any changes happend before error
            }
            e.printStackTrace();
            return false;
        }
        return true;


    }


    public User loginUser(String username, String password, TaskManager taskManager) {
            // JPQL REQUIRED HERE TO WRITE A COMPLEX NOT BUILT IN QUERY
            try{
                TypedQuery<User> query =em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password",User.class);
                query.setParameter("username", username);
                query.setParameter("password", password);
                //JPA returns the correct subclass object, but the declared reference type is User
                User user= query.getSingleResult(); // returns single only one user (If no user is found → Exception is thrown (NoResultException))
                //.get just fetches result

                //instanceof checks the actual object type, not the reference type
                 if(user instanceof Admin){
                        ((Admin)user).setTaskManager(taskManager);
                 }else if(user instanceof Worker){
                        ((Worker)user).setTaskManager(taskManager);
                    }
                 return user;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }





    }



}
