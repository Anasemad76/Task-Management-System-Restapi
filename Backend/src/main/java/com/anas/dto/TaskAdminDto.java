package com.anas.dto;

import com.anas.model.task.Task;

public class TaskAdminDto {
    private int taskId;
    private String taskTitle;
    private String taskDescription;
    private String assignedUser;
    private boolean isCompleted;
    private int priority;
    private String  dueDate;
    private boolean isApproved;

    public TaskAdminDto() {

    }
    //for task
    public TaskAdminDto(int taskId,String taskTitle, String taskDescription, String assignedUser, boolean isCompleted, int priority, String dueDate) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assignedUser = assignedUser;
        this.isCompleted = isCompleted;
        this.priority = priority;
        this.dueDate = dueDate;

    }
    //for highpriority
    public TaskAdminDto(int taskId,String taskTitle, String taskDescription, String assignedUser, boolean isCompleted, int priority, String dueDate, boolean isApproved) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assignedUser = assignedUser;
        this.isCompleted = isCompleted;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isApproved =isApproved;

    }
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String username) {
        this.assignedUser = username;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }
    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}
