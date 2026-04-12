package com.sms.service;

import java.util.List;
import java.util.Scanner;
import com.sms.entities.User;
import com.sms.util.InputValidator;

public class UserService {

    private UserDAO userDAO = new UserDAO();
    
    private static final String ADMIN_EMAIL = "onlinetats@gmail.com";

    
    public User login(String email, String password) {
        if(!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return null;
        }

        User user = userDAO.login(email, password);

        if(user == null) {
            System.out.println("Invalid email or password.");
        }

        return user;
    }

    

    public void updatePassword(String email, String newPassword) {
       
        if(newPassword == null || newPassword.length() < 4) {
            System.out.println("\n  Weak password. Minimum 4 characters required.");
            return;
        }

        userDAO.updatePassword(email, newPassword);
    }

    
    public boolean updateUser(int userId, String name, String phone) {
        return userDAO.updateUser(userId, name, phone);
    }

    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
    
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}