package com.sms.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.sms.entities.Administrator;
import com.sms.entities.Parent;
import com.sms.entities.Student;
import com.sms.entities.Teacher;
import com.sms.entities.User;
import com.sms.util.DatabaseConfig;
import com.sms.util.PasswordUtil;

public class UserDAO {

	public boolean isEmailExists(String email) {

	    try {
	        Connection con = DatabaseConfig.getConnection();

	        String query = "SELECT 1 FROM users WHERE email = ?";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, email);

	        ResultSet rs = ps.executeQuery();

	        return rs.next();

	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	

    public User login(String email, String password) {

        User user = null;

        try {

            Connection con = DatabaseConfig.getConnection();

            String query = "SELECT * FROM users WHERE email=?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String storedPassword = rs.getString("password");

                if (PasswordUtil.verifyPassword(password, storedPassword)) {

                    String dbRole = rs.getString("role");
                    if (dbRole.equalsIgnoreCase("ADMIN")) {
                        user = new Administrator(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                storedPassword,
                                dbRole
                        );
                    } else if (dbRole.equalsIgnoreCase("STUDENT")) {
                        user = new Student(
                        		rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                storedPassword,
                                dbRole,
                                rs.getInt("age"),
                                rs.getString("address"),
                                rs.getString("parent_email"),
                                rs.getLong("class_id")
                        );
                    } else if (dbRole.equalsIgnoreCase("PARENT")) {
                        user = new Parent(
                        		rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                storedPassword,
                                dbRole,
                                rs.getString("address"),
                                rs.getString("mobile_number"),
                                rs.getInt("age")
                        );
                    } else if (dbRole.equalsIgnoreCase("TEACHER")) {
                        user = new Teacher(
                        		rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                storedPassword,
                                dbRole,
                                rs.getString("phone_number"),
                                rs.getLong("subject_id"),
                                rs.getLong("class_id")
                        );
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
    
    public void updatePassword(String email, String newPassword) {

        try {

            Connection con = DatabaseConfig.getConnection();

            String query = "UPDATE users SET password=? WHERE email=?";

            PreparedStatement ps = con.prepareStatement(query);

            String hashedPassword = PasswordUtil.hashPassword(newPassword);

            ps.setString(1, hashedPassword);
            ps.setString(2, email);

            int rows = ps.executeUpdate();


            if(rows <= 0) {
                System.out.println("Email not found!");
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    

    public boolean deleteUser(int userId) {
        try {
        	
            Connection con = DatabaseConfig.getConnection();
            String query = "DELETE FROM users WHERE userId=?";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);
            
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Cannot delete User! They are referenced elsewhere in the system.");
            return false;
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }
        
}