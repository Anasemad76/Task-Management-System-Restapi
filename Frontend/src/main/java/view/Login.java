package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import model.user.Admin;
import model.user.User;
import model.user.Worker;

public class Login extends JFrame{
    private JPanel loginPanel;
    private JTextField userName;
    private JButton loginButton;
    private JLabel alreadyUser;
    private JPasswordField password;


    public Login() {

        setContentPane(loginPanel);
        setTitle("view.Login");
        setBounds(400, 200, 450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userName.getText();
                String pass = new String(password.getPassword());
                User user =loginUser(username,pass);
                if (user != null) {
                    if (user.getIsAdmin()) {
                        JOptionPane.showMessageDialog(loginPanel, "Hello, " + userName.getText());
                        dispose();
                        AdminDashboard adminDashboard = new AdminDashboard(user);
                        //user.displayMenu();
                    } else {
                        JOptionPane.showMessageDialog(loginPanel, "Hello, " + userName.getText());
                        dispose();
                        UserDashboard userDashboard = new UserDashboard(user);
                        //user.displayMenu();
                    }
                }else{
                    JOptionPane.showMessageDialog(loginPanel, "Invalid username or password");

                }


            }
        });
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


}
