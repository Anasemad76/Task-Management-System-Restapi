package model.user;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;


public class Admin extends User {


    public Admin(String username, String password) {
        super(username,true);

    }
    public Admin(){}



    @Override
    public String displayMenu() {
        return "ID: "+this.getId()+" || "+"username "+this.getUsername()+" || "+" isAdmin "+ this.getIsAdmin();
    }

//    @Override
//    public void displayMenu() {
//        System.out.println("Welcome to the Admin Menu");
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            try{
//                System.out.println("\nAdmin Menu:");
//                System.out.println("1. Create Task");
//                System.out.println("2. View All Tasks");
//                System.out.println("3. Delete Task");
//                System.out.println("4. Edit Task");
//                System.out.println("5. Approve completed Task");
//                System.out.println("6. Logout");
//                System.out.print("Choose an option: ");
//                int choice = scanner.nextInt();
//                scanner.nextLine();
//                switch (choice) {
//                    case 1:
//                        System.out.print("Enter task title: ");
//                        String taskTitle = scanner.nextLine();
//                        System.out.print("Enter task description: ");
//                        String taskDesc = scanner.nextLine();
//                        System.out.print("Enter username to assign: ");
//                        String assignedUser = scanner.nextLine();
//                        int priority;
//                        while (true) {
//                            System.out.print("Enter priority (1-3): ");
//                            try {
//                                priority = scanner.nextInt();
//                                scanner.nextLine();
//                                if (priority < 1 || priority > 3) {
//                                    throw new InputMismatchException("Priority must be between 1 and 3.");
//                                }
//                                break;
//                            } catch (InputMismatchException e) {
//                                System.out.println("Invalid input! Please enter a number (1-3).");
//
//                            }
//                        }
//                        LocalDate dueDate = null;
//                        while (true) {
//                            System.out.print("Enter due date (yyyy-MM-dd): ");
//                            try {
//                                dueDate = LocalDate.parse(scanner.nextLine());
//                                break;
//
//                            } catch (DateTimeParseException e) {
//                                System.out.println("Invalid input! Please enter a valid date (yyyy-MM-dd).");
//                            }
//                        }
//                        if(priority==3){
//                            taskManager.addTask(assignedUser, new HighPriorityTask(taskTitle, taskDesc, false, dueDate));
//
//                        }else {
//                            taskManager.addTask(assignedUser, new Task(taskTitle, taskDesc, false, priority, dueDate));
//                        }
//                        break;
//                    case 2:
//                        taskManager.listTasks();
//                        break;
//                    case 3:
//                        System.out.print("Enter Task Title to delete: ");
//                        String taskTit = scanner.nextLine();
//                        taskManager.deleteTask(taskTit);
//                        break;
//                    case 4:
//                        System.out.print("Enter Task Title to edit: ");
//                        String taskTit2 = scanner.nextLine();
//                        Map<String, Object> updates = new HashMap<>();
//                        while (true) {
//                            System.out.println("Enter Task Information to edit (title/description/username/priority/date/done): ");
//                            String toBeEdited = scanner.nextLine();
//                            if (toBeEdited.equalsIgnoreCase("Done")) {
//                                break;
//                            }
//                            switch (toBeEdited) {
//                                case "title":
//                                    toBeEdited = "taskTitle";
//                                    break;
//                                case "description":
//                                    toBeEdited = "taskDescription";
//                                    break;
//                                case "username":
//                                    toBeEdited = "assignedUser";
//                                    break;
//                                case "priority":
//                                    toBeEdited = "priority";
//                                    break;
//                                case "date":
//                                    toBeEdited = "dueDate";
//                                    break;
//                                default:
//                                    System.out.println("Invalid field! Try again.");
//                                    continue;
//                            }
//                            System.out.println("Enter information: ");
//                            String updated = scanner.nextLine();
//                            updates.put(toBeEdited, updated);
//                        }
//                        taskManager.editTask(taskTit2, updates,false);
//                        break;
//                    case 5:
//                        System.out.println("Enter Task Title to approve: ");
//                        String approveTaskTitle = scanner.nextLine();
//                        taskManager.approveTask(approveTaskTitle);
//                        break;
//
//                    case 6:
//                        System.out.println("Logging out...");
//                        return;
//                    default:
//                        System.out.println("Invalid option!");
//                }
//            }catch (InputMismatchException ex){
//                System.out.println("Invalid input! Please enter a number.");
//                scanner.nextLine();
//            }catch (Exception ex) {
//                System.out.println("An unexpected error occurred: " + ex.getMessage());
//            }
//        }
//    }
}
