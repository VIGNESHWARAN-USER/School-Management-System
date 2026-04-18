package com.sms.dao;
//Author:Vigneshwaran M
/*
 * This class for the query logic of an user based on the roles like admin
 * teacher,parent,student
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sms.entities.Administrator;
import com.sms.entities.Parent;
import com.sms.entities.Student;
import com.sms.entities.Teacher;
import com.sms.entities.User;
import com.sms.util.DatabaseConfig;
import com.sms.util.PasswordUtil;

public class UserDAO {
	
	//Using this method for checking the given email is present 
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
	
	
    //Getting email and password from user and returning if the user is present in the user table
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
                 
                //Comparing the raw password and the hashed password
                if (PasswordUtil.verifyPassword(password, storedPassword)) {

                    String dbRole = rs.getString("role");
                    long userId = rs.getLong("id");

                    if (dbRole.equalsIgnoreCase("ADMIN")) {
                    	//Upcasting concept is used
                        user = new Administrator(
                                userId,
                                rs.getString("name"),
                                rs.getString("email"),
                                storedPassword,
                                dbRole
                        );
                    } else if (dbRole.equalsIgnoreCase("STUDENT")) 
                    	
                    {
                        String studentQuery = "SELECT * FROM students WHERE id = ?";
                        PreparedStatement sps = con.prepareStatement(studentQuery);
                        sps.setLong(1, userId);
                        ResultSet srs = sps.executeQuery();
                        if (srs.next()) {
                        	//Upcasting concept is used

                            user = new Student(
                                    userId,
                                    rs.getString("name"),
                                    rs.getString("email"),
                                    storedPassword,
                                    dbRole,
                                    srs.getInt("age"),
                                    srs.getString("address"),
                                    srs.getString("parent_email"),
                                    srs.getLong("class_id")
                            );
                        }
                    } else if (dbRole.equalsIgnoreCase("PARENT")) {
                        String parentQuery = "SELECT * FROM parents WHERE id = ?";
                        PreparedStatement pps = con.prepareStatement(parentQuery);
                        pps.setLong(1, userId);
                        ResultSet prs = pps.executeQuery();
                        if (prs.next()) {
                           List<Long> childIds = new ArrayList<>();//Collections used
                            String pq3 = "SELECT student_id FROM parent_children WHERE parent_id = ?";
                            PreparedStatement pps3 = con.prepareStatement(pq3);
                            pps3.setLong(1, userId);
                            ResultSet prs3 = pps3.executeQuery();
                            while(prs3.next()) {
                                childIds.add(prs3.getLong("student_id"));
                            }
                        	//Upcasting concept is used

                            user = new Parent(
                                    userId,
                                    rs.getString("name"),
                                    rs.getString("email"),
                                    storedPassword,
                                    dbRole,
                                    prs.getString("mobile_number"),
                                    prs.getString("address"),
                                    prs.getInt("age"),
                                    childIds
                            );
                        }
                    } else if (dbRole.equalsIgnoreCase("TEACHER")) {
                        String teacherQuery = "SELECT * FROM teachers WHERE id = ?";
                        PreparedStatement tps = con.prepareStatement(teacherQuery);
                        tps.setLong(1, userId);
                        ResultSet trs = tps.executeQuery();
                        if (trs.next()) {
                        	//Upcasting concept is used

                            user = new Teacher(
                                    userId,
                                    rs.getString("name"),
                                    rs.getString("email"),
                                    storedPassword,
                                    dbRole,
                                    trs.getString("phone_number"),
                                    trs.getLong("subject_id"),
                                    trs.getLong("class_id")
                            );
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
    
    //This method is used to update password of the user
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
    

    //This method is used to delete the user based on id
    public boolean deleteUser(int userId) {
        try {
        	
            Connection con = DatabaseConfig.getConnection();
            String query = "DELETE FROM users WHERE userId=?";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);
            
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLIntegrityConstraintViolationException e)//To handle foreign key constraint exception
        {
            System.out.println("Error: Cannot delete User! They are referenced elsewhere in the system.");
            return false;
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }
        
}