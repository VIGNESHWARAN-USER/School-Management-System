package com.sms.service;
//Author: Vigneshwaran M 
/*
 * This class is for the business logic of user registration
 */
import java.util.List;
import java.util.Scanner;

import com.sms.dao.UserDAO;
import com.sms.entities.User;
import com.sms.exception.InvalidCredentialsException;
import com.sms.exception.UserNotFoundException;
import com.sms.util.InputValidator;

public class UserService {

	//Creating DAO object
    private UserDAO userDAO = new UserDAO();
    
    //Getting email and password from controller,validating email and passing to the DAO
    public User login(String email, String password) throws InvalidCredentialsException//Declaring the domain based exception
, UserNotFoundException
    {
        if(!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return null;
        }

        User user = userDAO.login(email, password);

        if(user == null) {
            throw new InvalidCredentialsException("Email or password is invalid");
        }

        return user;
    }

    //Getting email and new password from controller,validating password  and passing to the DAO

    public void updatePassword(String email, String newPassword) {
       
        if(newPassword == null || newPassword.length() < 4) {
            System.out.println("\n  Weak password. Minimum 4 characters required.");
            return;
        }

        userDAO.updatePassword(email, newPassword);
    }
}