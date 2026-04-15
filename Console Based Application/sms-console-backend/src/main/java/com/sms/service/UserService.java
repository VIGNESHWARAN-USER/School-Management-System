package com.sms.service;

import java.util.List;
import java.util.Scanner;

import com.sms.dao.UserDAO;
import com.sms.entities.User;
import com.sms.exception.InvalidCredentialsException;
import com.sms.util.InputValidator;

public class UserService {

    private UserDAO userDAO = new UserDAO();
    
    public User login(String email, String password) throws InvalidCredentialsException {
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
}