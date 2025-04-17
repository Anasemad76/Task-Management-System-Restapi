package com.anas.model.user;

import com.anas.service.WorkerTaskService;

import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue(value = "Worker")
public class Worker extends User {
    @Transient // Prevents JPA from persisting this field
    private WorkerTaskService taskManager;
    public Worker(String username, String password, WorkerTaskService taskManager) {
        super(username, password,false);
        this.taskManager = taskManager;
    }
    public Worker(){}

    public void setTaskManager(WorkerTaskService taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void displayMenu() {

        System.out.println("Welcome to the Worker Menu");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("\nWorker Menu:");
                System.out.println("1. View Assigned Tasks");
                System.out.println("2. Mark Task as Completed");
                System.out.println("3. Filter by Completion Status");
                System.out.println("4. Filter by priority (1-3)");
                System.out.println("5. Filter by due date");
                System.out.println("6. Logout");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        taskManager.listUserTasks(this);
                        break;
                    case 2:
                        System.out.print("Enter task title to mark as completed: ");
                        String taskTitle = scanner.nextLine();
                        taskManager.markTaskAsCompleted(getUsername(), taskTitle);
                        break;
                    case 3:
                        try {
                            System.out.print("Show completed tasks? (true/false): ");
                            String completedInput = scanner.nextLine().trim().toLowerCase();
                            if (completedInput.equals("true") || completedInput.equals("false")) {
                                boolean completed = Boolean.parseBoolean(completedInput);
                                taskManager.filterTaskByCompletedStatus(this, completed);
                            } else {
                                throw new InputMismatchException();
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! Please enter 'true' or 'false'.");
                        }
                        break;
                    case 4:
                        try {
                            System.out.print("Choose priority (1-3): : ");
                            int priority = scanner.nextInt();
                            scanner.nextLine();
                            if (priority < 1 || priority > 3) {
                                throw new InputMismatchException();
                            } else {
                                taskManager.filterTaskByPriority(this, priority);
                            }

                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! Please enter a valid number (1-3).");
                        }

                        break;

                    case 5:
                        try {
                            System.out.print("Enter due date to filter (yyyy-MM-dd): ");
                            String dateInput = scanner.nextLine();
                            LocalDate date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            System.out.print("Filter by (before/after/on): ");
                            String condition = scanner.nextLine().toLowerCase();
                            if (!condition.equals("before") && !condition.equals("after") && !condition.equals("on")) {
                                System.out.println("Invalid option! Please enter 'before', 'after', or 'on'.");
                            } else {
                                taskManager.filterTaskByDueDate(this, date, condition);
                            }

                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format! Please use YYYY-MM-DD.");
                        }
                        break;

                    case 6:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            }catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }
}
