package com.anas.controller;

import com.anas.dto.TaskAdminDto;
import com.anas.model.task.HighPriorityTask;
import com.anas.model.task.Task;
import com.anas.model.user.User;
import com.anas.model.user.Worker;
import com.anas.service.TaskManager;
import com.anas.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/task")
public class TaskController {
    @Autowired
    private TaskManager taskManager;

//    @Autowired
//    private UserManager userManager;

    //Worker Controllers

    //If you need to send a request body (like a JSON object), the appropriate HTTP method would be POST
    @PostMapping("/listTasks")
    public ResponseEntity<List<Task>> listTasks(@RequestBody Worker worker) {
        System.out.println(">>>> listTasks called");
        System.out.println(">>>> user: " + worker.getId());
        List<Task> tasks=taskManager.listUserTasks(worker);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

//    @GetMapping("/listTasks/{workerId}")
//    public ResponseEntity<List<Task>> listTasks(@PathVariable Long workerId) {
//        System.out.println(">>>> listTasks called");
//        System.out.println(">>>> workerId: " + workerId);
//        Worker worker = userManager.getWorkerById(workerId); // You would need to fetch the Worker from DB by ID
//        List<Task> tasks = taskManager.listUserTasks(worker);
//        return new ResponseEntity<>(tasks, HttpStatus.OK);
//    }


    @PostMapping("/completeTask/{taskTitle}")
    @ResponseBody
    public boolean completeTask(@PathVariable String taskTitle) {
        return taskManager.markTaskAsCompleted("",taskTitle);
    }

    @PostMapping("/tasks/filter/completion")
    public ResponseEntity<List<Task>> filterTasksByCompletion(@RequestBody Worker worker, @RequestParam boolean completed) {
        List<Task> tasks = taskManager.filterTaskByCompletedStatus(worker, completed);
        return ResponseEntity.ok(tasks); // if user data ir wrong it will send empty list
    }
    @PostMapping("/tasks/filter/priority")
    public ResponseEntity<List<Task>> filterTasksByPriority(@RequestBody Worker worker, @RequestParam int priority) {
        List<Task> tasks = taskManager.filterTaskByPriority(worker, priority);
        return ResponseEntity.ok(tasks);
    }
    // why @ModelAttribute didn't work here, i sent data in query parameter
    @PostMapping("/tasks/filter/date")
    public ResponseEntity<List<Task>> filterTasksByDate(@RequestBody Worker worker, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("condition") String condition) {
        List<Task> tasks = taskManager.filterTaskByDueDate(worker,date,condition);
        return ResponseEntity.ok(tasks);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

//    CHECK LW WORKER KALEM EL ENDPOINTS DOL EH EL HAY7SL????????

    // Admin Controllers (ba3d keda mommken te3ml controller lel admin lewhdo )

    @PostMapping("/addTask")
    public ResponseEntity<Boolean> addTask(@RequestParam String username,@RequestBody Task task) {
        boolean isSuccessful=false;
        if (task.getPriority()==3){
            // If it is high priority, create a HighPriorityTask
            // is it ok to create an object or it must be a bean ( as long as i dont need DI yes)
            HighPriorityTask highPriorityTask = new HighPriorityTask(task.getTaskTitle(), task.getTaskDescription(), task.getIsCompleted(),task.getDueDate());
             isSuccessful = taskManager.addTask(username, highPriorityTask);
        }else{
             isSuccessful = taskManager.addTask(username, task);
        }


        if (isSuccessful) {
            return ResponseEntity.status(HttpStatus.CREATED).body(true );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    @DeleteMapping("/deleteTask")
    public ResponseEntity<Boolean> deleteTask(@RequestParam String title) {
        boolean isSuccessful = taskManager.deleteTask(title);

        if (isSuccessful) {
            return ResponseEntity.status(HttpStatus.CREATED).body(true );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    @PostMapping("/approveTask")
    public ResponseEntity<Boolean> approveTask(@RequestParam String title) {
        boolean isSuccessful = taskManager.approveTask(title);

        if (isSuccessful) {
            return ResponseEntity.status(HttpStatus.CREATED).body(true );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    @GetMapping("/listAllTasks")
    public ResponseEntity<List<TaskAdminDto>> listTasks() {
        List <Task> tasks = taskManager.listTasks();
        List<TaskAdminDto> taskAdminDtos = tasks
                .stream()
                .map(task -> new TaskAdminDto(task.getTaskId(),task.getTaskTitle(),task.getTaskDescription() , task.getAssignedUser().getUsername(),task.getIsCompleted(),task.getPriority(),task.getDueDate().toString()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskAdminDtos);
    }

    @GetMapping("/listHighPriorityTasks")
    public ResponseEntity<List<TaskAdminDto>> listHighPrioirtyTasks() {
        List <HighPriorityTask> tasks = taskManager.filterAllHighPriorityTasks();
        List<TaskAdminDto> taskAdminDtos = tasks

                .stream()
                .map(task -> new TaskAdminDto(task.getTaskId(),task.getTaskTitle(),task.getTaskDescription() , task.getAssignedUser().getUsername(),task.getIsCompleted(),task.getPriority(),task.getDueDate().toString(),task.getisApproved()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskAdminDtos);
    }
    @PatchMapping("/editPriority")
    public ResponseEntity<Boolean> editPriority(@RequestParam("taskId") int id, @RequestParam("priority") int newPriority) {
        boolean isSuccessful = taskManager.editTaskPriorty(id,newPriority);
        if (isSuccessful) {
            return ResponseEntity.status(HttpStatus.CREATED).body(true );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PatchMapping("/editTask")
    public ResponseEntity<Boolean> editTask(@RequestParam("title") String title,
                                            @RequestParam("flag") boolean isGui,
                                            @RequestBody Map<String, Object> updates) {
        System.out.println(">>> "+title);
        System.out.println(">>> "+isGui);
        System.out.println(">>> "+updates);
        boolean isSuccessful = taskManager.editTask(title,updates,isGui);
        if (isSuccessful) {
            return ResponseEntity.status(HttpStatus.CREATED).body(true );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }






}
