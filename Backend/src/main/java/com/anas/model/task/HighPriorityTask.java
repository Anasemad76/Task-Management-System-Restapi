package com.anas.model.task;

import java.time.LocalDate;
import jakarta.persistence.*;
import com.anas.model.user.User;

@Entity
public class HighPriorityTask extends Task {
    private static final int HIGH_PRIORITY = 3;
    @Column(name = "is_approved",nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean isApproved=false;
    //cols:admin id , extra fields
    // el table da sah bas zawed extra features howar el worker ye complete w admin ya approve hena bas
    public HighPriorityTask() {}

    public HighPriorityTask(String taskTitle, String taskDescription, User assignedUser, boolean isCompleted, LocalDate dueDate) {
        super(taskTitle, taskDescription, assignedUser, isCompleted, HIGH_PRIORITY, dueDate);

    }

    public HighPriorityTask(String taskTitle, String taskDescription, boolean isCompleted, LocalDate dueDate) {
        super(taskTitle, taskDescription,isCompleted, HIGH_PRIORITY, dueDate);

    }

    public boolean getisApproved() {
        return isApproved;
    }

    @Override
    public int getPriority() {
            return HIGH_PRIORITY;
    }
    @Override
    public String toString() {
        return super.toString()+ " | approved : " +isApproved ;
    }
}
