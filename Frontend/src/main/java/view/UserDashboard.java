package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.user.User;
import model.user.Worker;
import model.task.HighPriorityTask;
import model.task.Task;
import model.user.User;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDashboard  extends JFrame {
    private JLabel taskLabel;
    private JTable tasksTable;
    private JComboBox filterCombo;
    private JButton logOutButton;
    private JPanel mainPanel;
    private JPanel textPanel;
    private JPanel filterPanel;
    private JPanel tablePanel;
    private JScrollPane scrollPane;
    private JButton markAsCompletedButton;
    private JButton backButton;
    private User user;

    String titleMarkAsCompletedTask;

    public UserDashboard(User user) {
        this.user = user;
        this.titleMarkAsCompletedTask=null;




        // Table column names
        setTitle("Worker Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Task ID","Task Title", "Task Description", "Priority", "Due Date", "Completed"};
        // Table model to hold data
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tasksTable.setModel(model);

        // Populate table with worker's tasks
        loadWorkerTasks(model);
        tasksTable.getColumnModel().getColumn(0).setMinWidth(250);


        setContentPane(mainPanel);
        setVisible(true);


        filterCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] columnNames = {"Task ID","Task Title", "Task Description", "Priority", "Due Date", "Completed"};
                DefaultTableModel model = (DefaultTableModel) tasksTable.getModel();
                model.setColumnIdentifiers(columnNames);

                String selectedFilter = (String) filterCombo.getSelectedItem();
                switch (selectedFilter) {
                    case "All":
                        loadWorkerTasks(model);
                        break;
                    case "Priority":
                        String priorityInput = JOptionPane.showInputDialog(filterCombo, "Enter priority (1-3):");
                        if (priorityInput != null) {
                            try {
                                int priority = Integer.parseInt(priorityInput);
                                if (priority < 1 || priority > 3) {
                                    JOptionPane.showMessageDialog(filterCombo, "Invalid priority! Enter a value between 1 and 3.");
                                } else {
                                    List<Task> filteredTasks=filterTaskByPriority(user, priority);
                                    if (priority==3){
                                        String[] highPriorityColumns ={"Task ID","Task Title", "Task Description", "Priority", "Due Date", "Completed","Approved"};
                                         model = (DefaultTableModel) tasksTable.getModel();

                                        // Check if the "Approve" column already exists to avoid duplication
                                        if (model.getColumnCount() != highPriorityColumns.length) {
                                            model.setColumnIdentifiers(highPriorityColumns);
                                        }

                                        printFilteredTasks(model,filteredTasks,"high");
                                        return;

                                    }
                                    printFilteredTasks(model,filteredTasks);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(filterCombo, "Please enter a valid number.");
                            }
                        }
                        break;
                    case "Due date":
                        String dateInput = JOptionPane.showInputDialog(filterCombo, "Enter Due Date (YYYY-MM-DD):");

                        if (dateInput != null) {
                            try {
                                LocalDate dueDate = LocalDate.parse(dateInput);
                                String[] options = {"on", "before", "after"};
                                String condition = (String) JOptionPane.showInputDialog(
                                        filterCombo,
                                        "Select condition:",
                                        "Filter by Due Date",
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        options,
                                        options[0] // Default selection
                                );
//
                                List<Task> filteredTasks=filterTaskByDueDate(user,dueDate.toString() ,condition);
                                printFilteredTasks(model,filteredTasks);
                            } catch (DateTimeParseException ex) {
                                JOptionPane.showMessageDialog(filterCombo, "Invalid date format! Please use YYYY-MM-DD.");
                            }
                        }
                        break;
                    case "Completion status":
                        boolean isCompleted = JOptionPane.showConfirmDialog(
                                filterCombo,
                                "Show completed tasks?",
                                "Filter",
                                JOptionPane.YES_NO_OPTION
                        ) == JOptionPane.YES_OPTION;


                        List<Task> filteredTasks=filterTaskByCompletedStatus(user, isCompleted);
                        printFilteredTasks(model,filteredTasks);
                }
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Register registerForm = new Register();
                registerForm.setVisible(true);

            }
        });

        markAsCompletedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tasksTable.getSelectedRow();
                System.out.println(row);
                if (row < 0) {
                    JOptionPane.showMessageDialog(markAsCompletedButton, "Please select a row from the table", "Try Again", JOptionPane.ERROR_MESSAGE);

                } else {
                    boolean isSuccessful=markTaskAsCompleted(user.getUsername(), titleMarkAsCompletedTask);
                    if(isSuccessful){
                        JOptionPane.showMessageDialog(tasksTable, "Task Marked As Completed Successfully", "Ok", JOptionPane.INFORMATION_MESSAGE);
                        loadWorkerTasks(model);
                    }else{
                        JOptionPane.showMessageDialog(tasksTable, "Error happened in database", "Try Again", JOptionPane.ERROR_MESSAGE);
                    }

                }

            }
        });


        tasksTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) { // Prevents multiple triggers
                int row = tasksTable.getSelectedRow();
                if (row != -1) { // Ensure a row is selected
                    titleMarkAsCompletedTask = model.getValueAt(row, 1).toString();
                    System.out.println("Selected Task Title: " + titleMarkAsCompletedTask);
                }
            }
        });


    }



    private void loadWorkerTasks(DefaultTableModel model) {
        String[] columnNames = {"Task ID","Task Title", "Task Description", "Priority", "Due Date", "Completed"};
        model = (DefaultTableModel) tasksTable.getModel();
        model.setColumnIdentifiers(columnNames);
        model.setRowCount(0);
        // Fetch tasks assigned to this worker from the database
        List<Task> workerTasks =listUserTasks(user);
        for(Task task:workerTasks){
            model.addRow(new Object[]{
                    task.getTaskId(),
                    task.getTaskTitle(),
                    task.getTaskDescription(),
                    task.getPriority(),
//                  task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    task.getDueDate(),
                    task.getIsCompleted()
            });
        }

    }
    private void printFilteredTasks(DefaultTableModel model,List<Task> taskList) {
        model.setRowCount(0);
        for (Task task : taskList) {
            model.addRow(new Object[]{
                    task.getTaskId(),
                    task.getTaskTitle(),
                    task.getTaskDescription(),
                    task.getPriority(),
                    //                  task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    task.getDueDate(),
                    task.getIsCompleted()
            });

        }

    }
    private void printFilteredTasks(DefaultTableModel model,List<Task> taskList,String condition) {
        model.setRowCount(0);
        for (Task task : taskList) {
            System.out.println("Task Class: " + task.getClass().getName());
            System.out.println(" is approved ? "+((HighPriorityTask)task).getisApproved());
            model.addRow(new Object[]{
                    task.getTaskId(),
                    task.getTaskTitle(),
                    task.getTaskDescription(),
                    task.getPriority(),
                    //                  task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    task.getDueDate(),
                    task.getIsCompleted(),
                    ((HighPriorityTask) task).getisApproved() ? "Approved ✅" : "Not Approved ❌"
            });

        }

    }

    public List<Task> listUserTasks(User user){
        try {
            Worker worker = (Worker) user;
            Gson gson = new Gson();
            String json = gson.toJson(worker);




            HttpRequest request = HttpRequest.newBuilder()
                    // example of queryParameter endpoint
                    .uri(new URI("http://localhost:8080/Backend/api/task/listTasks"))
                    .header("Content-Type", "application/json") // the server needs to know what format you're sending (JSON, XML, plain text, etc.)
                    .POST(HttpRequest.BodyPublishers.ofString(json)).build();

            // inorder to send the req you must create this object
            HttpClient client = HttpClient.newHttpClient();
            // send req and , what is the return type expected, the result is a HttpResponse object
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());

            //used to tell gson that the result will be List<Task>
            java.lang.reflect.Type taskListType = new TypeToken<List<Task>>(){}.getType();
            List<Task> tasks = gson.fromJson(response.body(), taskListType);
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    // methods el fadlaly to support rest api kol el filter w mark as completed

    //NOT TESTED YET
    public boolean markTaskAsCompleted(String username,String title){
        try {
            HttpRequest request = HttpRequest.newBuilder()

                    .uri(new URI("http://localhost:8080/Backend/api/task/completeTask/"+title))
                    .POST(HttpRequest.BodyPublishers.noBody()).build();


            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());
            return Boolean.parseBoolean(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public List<Task> filterTaskByPriority(User user,int priority){
        try {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/Backend/api/task/tasks/filter/priority?priority="+priority))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());

            if(priority==3){
                java.lang.reflect.Type taskListType = new TypeToken<List<HighPriorityTask>>(){}.getType();
                List<HighPriorityTask> highPriorityTasks  = gson.fromJson(response.body(), taskListType);
                List<Task> tasks = new ArrayList<>(highPriorityTasks);
                return tasks;
            }
            java.lang.reflect.Type taskListType = new TypeToken<List<Task>>(){}.getType();
            List<Task> tasks = gson.fromJson(response.body(), taskListType);
            return tasks;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public List<Task> filterTaskByDueDate(User user,String dueDate,String condition){
        try {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/Backend/api/task/tasks/filter/date?date="+dueDate+"&condition="+condition))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());

            java.lang.reflect.Type taskListType = new TypeToken<List<Task>>(){}.getType();
            List<Task> tasks = gson.fromJson(response.body(), taskListType);
            return tasks;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Task> filterTaskByCompletedStatus(User user,boolean isCompleted){
        try {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/Backend/api/task/tasks/filter/completion?completed="+isCompleted))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());

            java.lang.reflect.Type taskListType = new TypeToken<List<Task>>(){}.getType();
            List<Task> tasks = gson.fromJson(response.body(), taskListType);
            return tasks;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }




}
