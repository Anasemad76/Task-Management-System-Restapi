package model.user;



import java.util.ArrayList;
import java.util.List;



public abstract class User{

    private int id;

    private String username;

//    private String password;

    private boolean isAdmin;


    public User(){

    }

    public User(String username,boolean isAdmin) {
        this.username = username;
//        this.password = password;
        this.isAdmin = isAdmin;

    }



    public String getUsername() { return username; }

    public boolean getIsAdmin() {
        return isAdmin;
    }



    public int getId(){
        return id;
    }




    public abstract String displayMenu();

}

