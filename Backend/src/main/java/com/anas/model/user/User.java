package com.anas.model.user;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.anas.model.task.Task;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// used by JPA to differentiate between different subclass types
@DiscriminatorColumn(name = "type",discriminatorType = DiscriminatorType.STRING)
public abstract class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;
    //CascadeType.ALL bec I want if anything happens in task table to affect task list
    //orphanRemoval=true bec if I deleted a task from this list I want it to be deleted from database too
    @OneToMany(mappedBy = "assignedUser",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference // this is used for jackson json res
    List<Task> tasks=new ArrayList<>();
    //Note:When an entity has a lazy-loaded relationship (like @OneToMany or @ManyToOne), calling a getter may trigger a query.


    // tasks should remain in list and database even if completed
    public User(){

    }

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;

    }



    public String getUsername() { return username; }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public int getId(){
        return id;
    }

    // this will change state of the list in memory not in db, it needs to be merged to db by em to change db
    public void addTask(Task task) {
        tasks.add(task);
        task.setAssignedUser(this);
    }
    public void removeTask(Task task) {
        tasks.remove(task);
        task.setAssignedUser(null);
    }


    public abstract void displayMenu();

}

