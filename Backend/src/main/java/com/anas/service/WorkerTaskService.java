package com.anas.service;

import com.anas.model.task.Task;
import com.anas.model.user.User;

import java.time.LocalDate;
import java.util.List;

public interface WorkerTaskService {
    List<Task> listUserTasks(User user);
    boolean markTaskAsCompleted(String username, String taskTitle);
    List<Task> filterTaskByCompletedStatus(User user,boolean completed);
    List<Task> filterTaskByPriority(User user,int priority);
    List<Task> filterTaskByDueDate(User user,LocalDate dueDate,String condition);
}
