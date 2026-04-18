package com.sms.service;

// Author : Vigneshwaran M
// This class handles member (Student, Teacher, Parent) related operations

import java.util.List;

import com.sms.dao.MembersDAO;
import com.sms.dao.UserDAO;
import com.sms.entities.User;

public class MembersService {

    // Creating DAO objects 
    private final MembersDAO membersDAO = new MembersDAO();
    private final UserDAO userDAO = new UserDAO();

    // Add a new member
    public String addMember(User member) {

        // Validation to check if email already exists
        if (userDAO.isEmailExists(member.getEmail())) {
            return "Email already exists! Cannot add member.";
        }

        boolean success = membersDAO.addMember(member);
        return success ? "Member added successfully!" : "Failed to add member.";
    }

    // Get all members
    public List<User> getAllMembers() {
        return membersDAO.getAllMembers(); // Collections used
    }

    // Update member details
    public String updateMember(User member) {
        boolean success = membersDAO.updateMember(member);
        return success ? "Member updated successfully!" : "Failed to update member.";
    }

    // Delete member by userId
    public String deleteMember(Long userId) {
        boolean success = membersDAO.deleteMember(userId);
        return success ? "Member deleted successfully!" : "Failed to delete member or member not found.";
    }
}