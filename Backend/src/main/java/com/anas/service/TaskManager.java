package com.anas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anas.repository.TaskDAO;
import com.anas.model.task.HighPriorityTask;
import com.anas.model.task.Task;
import com.anas.model.user.User;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TaskManager implements AdminTaskService, WorkerTaskService {
    private List<Task> tasks=new ArrayList<Task>();

    private TaskDAO taskDAO;
    @Autowired
    public TaskManager(TaskDAO taskDAO) {
        this.taskDAO=taskDAO;
    }


    public boolean addTask(String username,Task task) {
        boolean isSuccessfulQuery=taskDAO.addNewTask(username,task);
        System.out.println(isSuccessfulQuery ? "Task added successfully" : "Task wasn't added successfully");
        return isSuccessfulQuery;
    }


    public boolean deleteTask(String taskTitle){
       boolean isSuccessfulQuery=taskDAO.removeTask(taskTitle);
        System.out.println(isSuccessfulQuery ? "Task Deleted successfully" : "Task wasn't Deleted successfully");
        return isSuccessfulQuery;
    }

    // for terminal
    public boolean editTask(String taskTitle, Map<String,Object> updates,boolean isGUI){
        boolean isSuccessful=false;
        if (isGUI) {
                 isSuccessful= taskDAO.editTask(Integer.parseInt(taskTitle),updates);

        }else{
                 isSuccessful= taskDAO.editTask(taskTitle,updates);
        }

           return isSuccessful;

    }
    public boolean editTaskPriorty(int id, int newPriority) {
        boolean isSuccessful=taskDAO.editTaskPriort(id, newPriority);
        System.out.println(isSuccessful ? "Task Priority updated successfully" : "Task wasn't updated successfully");
        return isSuccessful;
    }

    public boolean approveTask(String taskTitle){
        boolean isSuccessful=taskDAO.approveTaskByAdmin(taskTitle);
        if (isSuccessful) {
            System.out.println("Task approved successfully");
        }
        return isSuccessful;
    }



    public List<Task> listTasks(){
        List<Task>  taskList= taskDAO.getAllTasks();
        for(Task task:taskList){
            System.out.println(task);
        }
        return taskList;


    }


    public List<Task> listUserTasks(User user){
        List<Task> taskList= taskDAO.getUserTasks(user);
        for(Task task:taskList){
            System.out.println(task);
        }
        return taskList;
    }




    public boolean markTaskAsCompleted(String username, String taskTitle){

        boolean isSuccessfulQuery=taskDAO.taskCompleted(taskTitle);
        System.out.println(isSuccessfulQuery ? "Task Completed successfully" : "Task wasn't Completed successfully");
        return isSuccessfulQuery;
    }

    public List<Task>  filterTaskByCompletedStatus(User user,boolean completed){
        List<Task> filteredTasks=taskDAO.filterByCompletion(user,completed);
        for (Task task:filteredTasks){
            System.out.println(task);
        }
        return filteredTasks;

    }
    public List<HighPriorityTask> filterAllHighPriorityTasks(){
        List<HighPriorityTask> filteredTasks=taskDAO.filterHighTaskPriority();
        for (HighPriorityTask task:filteredTasks){
            System.out.println(task);
        }
        return filteredTasks;

    }

    public List<Task> filterTaskByPriority(User user,int priority){
        List<Task> filteredTasks=taskDAO.filterByTaskPriority(user,priority);
        for (Task task:filteredTasks){
            System.out.println(task);
        }
        return filteredTasks;
    }


public List<Task> filterTaskByDueDate(User user,LocalDate dueDate,String condition){
        List<Task> filteredTasks=taskDAO.filterByTaskDUEDate(user,dueDate,condition);
        for (Task task:filteredTasks){
            System.out.println(task);
        }

        return filteredTasks;

}
}
