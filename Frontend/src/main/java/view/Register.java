package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import model.user.Admin;
import model.user.User;
import model.user.Worker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


public class Register extends JFrame{
    private JTextField userName;
    private JButton registerBtn;
    private JButton loginButton;
    private JPanel registerPanel;
    private JCheckBox adminCheckBox;
    private JLabel alreadyUser;
    private JPasswordField password1;
    private JPasswordField password2;

    // must be class field to be accessed efficiency inside eventListeners
//    private UserManager userManager;
//    private TaskManager taskManager;
    private String username;
    private String password;
    private boolean isAdmin;

    public Register() {
//        this.userManager=userManager;
//        this.taskManager=taskManager;
        this.setContentPane(this.registerPanel);
        this.setTitle("Register");
        this.setBounds(400, 200, 450, 300);
//    r.setSize(500,500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userName.getText();
                String pass1 = new String(password1.getPassword());
                String pass2 = new String(password2.getPassword());
                if (!pass1.equals(pass2)) {
                    JOptionPane.showMessageDialog(Register.this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean isAdmin = adminCheckBox.isSelected();
                boolean isRegisteredSuccessfully=registerUser(username, pass1, isAdmin);
                if (isRegisteredSuccessfully) {
                    User user =loginUser(username,pass1);
                    System.out.println(">>>>>>>>> "+user.displayMenu());
                    JOptionPane.showMessageDialog(registerBtn,userName.getText()+" ,Hello");
                    try {
                        if (user.getIsAdmin()) {
                            dispose();
                            AdminDashboard adminDashboard = new AdminDashboard(user);
                        }else {
                            dispose();
                           UserDashboard userDashboard = new UserDashboard(user);
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                }

            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                view.Login loginForm = new view.Login(); // Assuming view.Login is another JFrame class
                loginForm.setVisible(true);

            }
        });


    }
    public boolean registerUser(String username, String pass1, boolean isAdmin){

        try {
            // your req
            Map<String,String> registerMap=new HashMap<>();
            registerMap.put("username",username);
            registerMap.put("password",pass1);
            registerMap.put("isAdmin", String.valueOf(isAdmin));
            Gson gson = new Gson();
            String json = gson.toJson(registerMap);
            HttpRequest request = HttpRequest.newBuilder()
                    // example of queryParameter endpoint
                    .uri(new URI("http://localhost:8080/Backend/api/user/register"))
                    .header("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // inorder to send the req you must create this object
            HttpClient client = HttpClient.newHttpClient();
            // send req and , what is the return type expected, the result is a HttpResponse object
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());
            return Boolean.parseBoolean(response.body());

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    public User loginUser(String username, String pass1){
        try {
            Map<String,String> loginMap=new HashMap<>();
            loginMap.put("username",username);
            loginMap.put("password",pass1);
            Gson gson = new Gson();
            String json = gson.toJson(loginMap);
            HttpRequest request = HttpRequest.newBuilder()
                    // example of queryParameter endpoint
                    .uri(new URI("http://localhost:8080/Backend/api/user/login"))
                    .header("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // inorder to send the req you must create this object
            HttpClient client = HttpClient.newHttpClient();
            // send req and , what is the return type expected, the result is a HttpResponse object
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(">>> "+response.body());
            User user=null;
            // this code we use jackson to convert from json to work with JSON data without binding it to a specific Java class
            // ya3ny zay object keda
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            boolean isAdminResult = root.get("isAdmin").asBoolean();
            if (isAdminResult){
                user=gson.fromJson(response.body(), Admin.class);
            }else{
                user=gson.fromJson(response.body(), Worker.class);

            }

            return user;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    public static void main(String[] args) {

        try {
            Register r=new Register();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
