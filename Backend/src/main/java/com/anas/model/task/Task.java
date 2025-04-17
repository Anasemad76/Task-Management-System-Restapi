package com.anas.model.task;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.anas.model.user.User;

@Entity
@Table(name = "tasks")
//separate tables for each subclass
//My conculsion:for InheritanceType.JOINED if either superclass or sublcass is deleted it will be deleted from both tables
//In the end , think of it as a form of polymorphism if you deleted the child then the superclass won't have a purpose
@Inheritance(strategy = InheritanceType.JOINED)

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int taskId;
    @Column(name = "task_title", nullable = false , unique = true)
    private String taskTitle;
    @Column(name = "task_description")
    private String taskDescription;


    @ManyToOne()
    @JoinColumn(name = "user_id") //not needed here as hibernate knows that this will be a foreign key by default
    @JsonBackReference // this is used for jackson json respond
    private User assignedUser;
    // columnDef =can be used to specify the exact SQL column definition
    @Column(name = "is_completed", nullable = false ,columnDefinition = "BIT DEFAULT 0")
    private boolean isCompleted;
    @Column(name = "priority", nullable = false , columnDefinition = "INT CHECK (priority >= 1 AND priority <= 3)")
    private int priority;
    // note : @Temporal is used only with java.util.Date
    @Column(name = "due_date")
    @JsonFormat(pattern = "yyyy-MM-dd") //to serialize it in the format you want
    private LocalDate  dueDate;

    public Task() {}

    // if no asignedUser yet
    public Task(String taskTitle, String taskDescription, boolean isCompleted, int priority, LocalDate dueDate) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.isCompleted = isCompleted;
        this.dueDate =dueDate;
        this.priority =priority;
    }
    // if assignedUser was determined when creating the task
    public Task(String taskTitle, String taskDescription, User assignedUser, boolean isCompleted, int priority, LocalDate dueDate) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assignedUser = assignedUser;
        this.isCompleted = isCompleted;
        this.dueDate =dueDate;
        this.priority =priority;
    }





    public String getTaskTitle() {
        return taskTitle;
    }
    public int getTaskId() {
        return taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }
    public User getAssignedUser() {
        return assignedUser;
    }


    public boolean getIsCompleted() {
        return isCompleted;
    }
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate  getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return "Task: " + taskTitle + " | desc : " + taskDescription +  " | Assigned To: " + assignedUser.getUsername() + " | priority : " + priority + " | date: " + dueDate + " | Completed: " + isCompleted;
    }
}
