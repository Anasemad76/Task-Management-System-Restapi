package model.task;

import model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDate;


//separate tables for each subclass
//My conculsion:for InheritanceType.JOINED if either superclass or sublcass is deleted it will be deleted from both tables
//In the end , think of it as a form of polymorphism if you deleted the child then the superclass won't have a purpose


public class Task {

    private int taskId;

    private String taskTitle;

    private String taskDescription;




//    private User assignedUser;
    private String assignedUser;


    private boolean isCompleted;

    private int priority;



    private String  dueDate;

    public Task() {}


    public Task(String taskTitle, String taskDescription, boolean isCompleted, int priority, String dueDate) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.isCompleted = isCompleted;
        this.dueDate =dueDate;
        this.priority =priority;
    }





    public String getUsername(){
        return assignedUser;
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

    public String getAssignedUser() {
        return assignedUser;
    }



    public boolean getIsCompleted() {
        return isCompleted;
    }
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }


    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String  getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return "Task: " + taskTitle + " | desc : " + taskDescription +  " | priority : " + priority + " | date: " + dueDate + " | Completed: " + isCompleted;
    }
}
