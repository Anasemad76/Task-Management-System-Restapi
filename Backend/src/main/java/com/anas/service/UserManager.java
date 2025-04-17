package com.anas.service;

import com.anas.model.user.Admin;
import com.anas.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anas.repository.UserDAO;
import com.anas.model.user.Worker;

@Service
public class UserManager {

    private final UserDAO userDAO;
    private final TaskManager taskManager;


    @Autowired
    public UserManager(UserDAO userDAO,TaskManager taskManager) {
        this.userDAO = userDAO;
        this.taskManager=taskManager;

    }

    public boolean registerUser(String username, String password, boolean isAdmin) {
        User user=null;
        if (isAdmin) {
            user=new Admin(username, password,taskManager);
        }else {
            user=new Worker(username,password,taskManager);
        }
        boolean isSuccessful = userDAO.registerUserDB(user) ;
        if(isSuccessful) {
            System.out.println("User registered Successfully!");
        }else {
            System.out.println("User registering Failed!");
        }
        return isSuccessful;


    }

    public User loginUser(String username, String password) {

        return userDAO.loginUser(username, password,taskManager);

    }
}
