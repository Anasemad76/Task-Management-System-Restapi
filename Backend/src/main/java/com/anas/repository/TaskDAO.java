package com.anas.repository;

import jakarta.persistence.*;
import com.anas.model.task.HighPriorityTask;
import com.anas.model.task.Task;
import com.anas.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TaskDAO {


    @PersistenceContext
    private EntityManager em;

    @Autowired
    public TaskDAO(DatabaseConnection dbConnection) {

        this.em = dbConnection.getEntityManager();
    }

    public List<Task> getAllTasks() {
        //note: TypedQuery<T> is a subinterface of Query that enforces type safety
        // And used only for SELECT queries.
        TypedQuery<Task> query = em.createQuery("SELECT t from Task t ", Task.class);
        List<Task> tasks = query.getResultList();
        return tasks;


    }


    public List<Task> getUserTasks(User user) {
        System.out.println(">>>>>> LOOK "+ user.getId());
        TypedQuery<Task> query=em.createQuery("SELECT t from Task t where t.assignedUser = :user", Task.class);
        query.setParameter("user", user);
        List<Task> tasks = query.getResultList();
        return tasks;


    }


    public boolean addNewTask(String username,Task task) {
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            User user = query.getSingleResult();
            if (user == null) {
                System.out.println(" Assigned Worker not found!");
                return false;
            }
            task.setAssignedUser(user);
            em.persist(task);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }

    }


    //remove by taskId
    public boolean removeTask(int taskId) {
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Task task = em.find(Task.class, taskId); // returns null if not found
            if(task != null) {
                em.remove(task);
                tx.commit();
                em.clear(); // Clears cache, forcing a fresh DB fetch, used after executing executeUpdate() queries to ensure updated data is loaded.
                return true;
            }
            return false;


        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }

    }


    //remove by taskTitle
    public boolean removeTask(String taskTitle) {
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Query query=em.createQuery("DELETE FROM Task t WHERE t.taskTitle = :title");
            query.setParameter("title", taskTitle);
            int row=query.executeUpdate(); // Executes update (this is used for UPDATE, DELETE, INSERT)
            tx.commit();
            em.clear();
            return row>0;

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;

        }

    }

    //make here editbyId to edit the task title if wanted from gui


    // for terminal
    //FADELY UPDATE EL PRIORITY W MAWDOO3 EL HIGHTASK AND TASKssssssssssssssssssssssssssssssssss
    public boolean editTask(String taskTitle, Map<String,Object> updates) {
        if (updates.isEmpty()) {
            System.out.println("No updates provided.");
            return false;
        }
        
        StringBuilder sql = new StringBuilder("UPDATE Task t SET ");

        updates.forEach( (key, value) -> {
            if (key.equals("priority")) {
                updates.put(key, Integer.parseInt(value.toString()));
            } else if (key.equals("isCompleted") || key.equals("isApproved")) {
                updates.put(key, Boolean.parseBoolean(value.toString()));
            } else if (key.equals("dueDate")) {
                updates.put(key, LocalDate.parse(value.toString()));
            }
            sql.append("t.").append(key).append(" = :").append(key).append(", ");

        });

        sql.setLength(sql.length()-2);
        sql.append(" WHERE t.taskTitle= :title");
        Query query = em.createQuery(sql.toString());
        EntityTransaction tx = em.getTransaction();
        try {
            updates.forEach((key, value) -> {



                if (key.equals("assignedUser")) {
                    TypedQuery<User> query2 = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
                    query2.setParameter("username", value);
                    User user = query2.getSingleResult();
                    query.setParameter(key, user);


                } else if (key.equals("priority")) {
                    if ( Integer.parseInt(value.toString()) == 3 ) {
                        TypedQuery<Task> query2=em.createQuery("SELECT t FROM Task t WHERE t.taskTitle = :title", Task.class);
                        query2.setParameter("title", taskTitle);
                        Task task = query2.getSingleResult();
                        boolean isSuccess=removeTask(taskTitle);
                        //em.flush(); // Ensure deletion is executed before inserting a new one
                        if (isSuccess) {
                            addNewTask(task.getAssignedUser().getUsername(),new HighPriorityTask(task.getTaskTitle(), task.getTaskDescription(),false,task.getDueDate()));

                        }

                    }else{
                        TypedQuery<Task> query2=em.createQuery("SELECT t FROM Task t WHERE t.taskTitle = :title", Task.class);
                        query2.setParameter("title", taskTitle);
                        Task task = query2.getSingleResult();
                        int oldPriority=task.getPriority();
                        if (oldPriority==3){
                            boolean isSuccess=removeTask(taskTitle);
                            //em.flush();
                            if (isSuccess) {
                                addNewTask(task.getAssignedUser().getUsername(),new Task(task.getTaskTitle(), task.getTaskDescription(),false,Integer.parseInt(value.toString()),task.getDueDate()));
                            }
                        }

                    }

                } else {
                    query.setParameter(key, value);
                }

            });
            tx.begin();
            query.setParameter("title", taskTitle);
            int rows = query.executeUpdate();
            tx.commit();
            em.clear(); // Clear persistence context to force fresh data
            if (rows > 0) {
                System.out.println("Task updated successfully");
                return true;
            }else{
                System.out.println("No task found with title: " +taskTitle);
                return false;
            }

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }

    }

    //overloaded version for GUI (i f iwant to update the title)
    public boolean editTask(int id, Map<String,Object> updates) {
        if (updates.isEmpty()) {
            System.out.println("No updates provided.");
            return false;
        }

        StringBuilder sql = new StringBuilder("UPDATE Task t SET ");

        updates.forEach( (key, value) -> {

            sql.append("t.").append(key).append(" = :").append(key).append(", ");

        });

        sql.setLength(sql.length()-2);
        sql.append(" WHERE t.taskId= :id");
        Query query = em.createQuery(sql.toString());
        EntityTransaction tx = em.getTransaction();
        try {
            updates.forEach((key, value) -> {

                // just get User Object from username String bec Task has User user field
                if (key.equals("assignedUser")) {
                    TypedQuery<User> query2 = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
                    query2.setParameter("username", value);
                    User user = query2.getSingleResult();
                    query.setParameter(key, user);

                } else {
                    query.setParameter(key, value);
                }

            });
            tx.begin();
            query.setParameter("id",id);
            int rows = query.executeUpdate();
            tx.commit();
            em.clear(); // Clear persistence context to force fresh data
            if (rows > 0) {
                System.out.println("Task updated successfully");
                return true;
            }else{
                System.out.println("No task found with task ID: " +id);
                return false;
            }

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }

    }
    // to edit priority(form high to normal task or vise versa)
    public boolean editTaskPriort(int id, int newPriority) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            if (newPriority == 3) { //changing task from 1 or 2 to 3
                Task task = em.find(Task.class, id);
                if (task != null && task.getPriority() != newPriority) {
                    em.remove(task);
                    em.flush(); // flush() actually runs the query on your database immediately
                    HighPriorityTask highTask = new HighPriorityTask(task.getTaskTitle(), task.getTaskDescription(), task.getAssignedUser(), task.getIsCompleted(), task.getDueDate());
                    em.persist(highTask);
                    tx.commit();
                    return true;
                }


            } else { // changing priority from 3 to 1 or 2 or update task priority
                HighPriorityTask highTask = em.find(HighPriorityTask.class, id);
                if (highTask != null) { // then chaange is from 3 to  1 or 2
                    // Remove it from HighPriorityTask table


                    em.remove(highTask);
                    em.flush();  // Ensure removal before inserting the new record
                    // why is task not found ????????????? subclass if deleted superclass entity gets deleted too why?????????
//                    Task task = em.find(Task.class, id);
//                    System.out.println("here please look : "+task);
                     Task task = new Task(highTask.getTaskTitle(), highTask.getTaskDescription(), highTask.getAssignedUser(), highTask.getIsCompleted(), newPriority, highTask.getDueDate());

                    // Persist as a regular Task
                   em.persist(task);
//                   task.setPriority(newPriority);
//                    em.merge(task);
                    tx.commit();
                    return true;
                } else { // change is from 1 or 2 to 1 or 2
                    Task task = em.find(Task.class, id);
                    if (task != null && task.getPriority() != newPriority) {
                        task.setPriority(newPriority);
                        em.merge(task); // updates a detached entity that is no longer in the persistence context
                        tx.commit();
                        return true;
                    }


                }


            }
        }catch (Exception e) {
            tx.rollback(); // Ensure rollback on failure
            e.printStackTrace();
        }
    return false;
    }



    //complete by taskId
    public boolean taskCompleted(int taskId) {
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Task task = em.find(Task.class, taskId);
            if(task != null) {
                task.setIsCompleted(true);
                // em.merge(task); not needed becasue task is managed by persistence context(handled by em) not detached
                tx.commit();
                return true;
            }
            return false;

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;

        }

    }

    //complete by task title
    public boolean taskCompleted(String taskTitle) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery("UPDATE Task t SET t.isCompleted = true WHERE t.taskTitle = :title");
            query.setParameter("title", taskTitle);
            int rows = query.executeUpdate();
            tx.commit();
            em.clear();
            return rows > 0;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;

        }

    }


    public List<Task> filterByCompletion(User user,boolean isCompleted) {
        TypedQuery<Task> query = em.createQuery(
                "SELECT t FROM Task t WHERE t.assignedUser = :user AND t.isCompleted = :completed", Task.class);
        query.setParameter("user", user);
        query.setParameter("completed", isCompleted);
        return query.getResultList();
    }


    public List<Task> filterByTaskPriority(User user,int priority) {
        TypedQuery<Task> query= em.createQuery("SELECT t FROM Task t WHERE t.assignedUser= :user AND t.priority = :priority", Task.class);
        query.setParameter("user", user);
        query.setParameter("priority", priority);
        return query.getResultList();
    }



    public List<Task> filterByTaskDUEDate(User user, LocalDate dueDate,String condition) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM Task t WHERE t.assignedUser = :user AND ");
        switch(condition){
            case "before":
                jpql.append("dueDate < :duedate ");
                break;
            case "after":
                jpql.append("dueDate > :duedate ");
                break;
            case "on":
                jpql.append("dueDate = :duedate ");
                break;
            default:
                System.out.println("Invalid condition: Use 'before', 'after', or 'on'");
                return new ArrayList<>();
        }
        TypedQuery<Task> query=em.createQuery(jpql.toString(), Task.class);
        query.setParameter("user",user);
        query.setParameter("duedate", dueDate);

        return query.getResultList();


    }
    public boolean approveTaskByAdmin(String taskTitle) {
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            TypedQuery<Long> query= em.createQuery("SELECT COUNT(*) FROM Task t WHERE t.taskTitle = :title AND t.isCompleted = true", Long.class);
            query.setParameter("title", taskTitle);
            Long count = query.getSingleResult();
            if (count == 0) {
                System.out.println("Task must be completed before approval!");
                tx.rollback();
                return false;
            }
            Query query2 = em.createQuery("UPDATE HighPriorityTask t SET t.isApproved = true WHERE t.taskTitle = :title");
            query2.setParameter("title", taskTitle);
            int rows = query2.executeUpdate();
            tx.commit();
            em.clear();
            return rows > 0;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }

    }
    public List<HighPriorityTask> filterHighTaskPriority() {
        TypedQuery<HighPriorityTask> query = em.createQuery("SELECT t FROM HighPriorityTask t WHERE t.priority = :priority", HighPriorityTask.class);
        query.setParameter("priority", "3");
        return query.getResultList();
    }



}