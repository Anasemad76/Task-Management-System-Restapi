package com.anas.controller;

import com.anas.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.anas.service.UserManager;

import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserManager userManager;

    @GetMapping("/test")
    public String test(){
        System.out.println(">>>> INSIDE TEST");
        return "test";
    }


    @PostMapping("/register")
    public ResponseEntity<Boolean> registerUser(@RequestBody Map<String, String> credentials){
        System.out.println(">>> INSIDE REGISTER com.anas.controller");
        String username=credentials.get("username");
        String password=credentials.get("password");
        boolean isAdmin=Boolean.parseBoolean(credentials.get("isAdmin"));
        boolean isSuccessful=userManager.registerUser(username,password,isAdmin);
        if (isSuccessful){
            // status code 200 & true value
            return ResponseEntity.ok(true);
        }else{
            // status code 400(bad req)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }
//   change it to accept json body for security(not query parameter)
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody Map<String, String> credentials){
        String username=credentials.get("username");
        String password=credentials.get("password");
        User user=userManager.loginUser(username,password);
        if (user != null){
            // note Jackson only includes fields that have getters
            // status code 200 & true value
            return ResponseEntity.ok(user);

        }else{
            // status code 400(bad req)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }
}
