package com.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sms.entities.Parent;
import com.sms.entities.Student;
import com.sms.entities.Teacher;
import com.sms.entities.User;
import com.sms.util.DatabaseConfig;
import com.sms.util.PasswordUtil;

public class MembersDAO {

    public boolean addMember(User user) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "INSERT INTO users (name, email, password, role) " +
                    "VALUES (?, ?, ?, ?)";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, PasswordUtil.hashPassword(user.getPassword()));
            ps.setString(4, user.getRole());
            
            boolean res = ps.executeUpdate() > 0;
            if (!res) {
                return false;
            }

            query = "Select id from users where email = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();
            long userId = 0;
            if (rs.next()) {
                userId = rs.getLong(1);
            }
            
            if (user instanceof Student) {
                Student s = (Student) user;
                String query2 = "INSERT INTO students (age, address, parent_email, class_id, id) " +
                        "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ps2.setInt(1, s.getAge());
                ps2.setString(2, s.getAddress());
                ps2.setString(3, s.getParentEmail());
                ps2.setLong(4, s.getClassId());
                ps2.setLong(5, userId);
                return ps2.executeUpdate() > 0;
            } else if (user instanceof Teacher) {
                Teacher t = (Teacher) user;
                String query2 = "INSERT INTO teachers (phone_number, subject_id, class_id, id) " +
                        "VALUES (?, ?, ?, ?)";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ps2.setString(1, t.getPhoneNumber());
                ps2.setLong(2, t.getSubjectId());
                ps2.setLong(3, t.getClassId());
                ps2.setLong(4, userId);
                return ps2.executeUpdate() > 0;
            } else if (user instanceof Parent) {
                Parent p = (Parent) user;
                String query2 = "INSERT INTO parents (age, address, mobile_number, id) " +
                        "VALUES (?, ?, ?, ?)";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ps2.setInt(1, p.getAge());
                ps2.setString(2, p.getAddress());
                ps2.setString(3, p.getMobileNumber());
                ps2.setLong(4, userId);
                return ps2.executeUpdate() > 0;
            }

            return res;                        
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<User> getAllMembers() {
        List<User> members = new ArrayList<>();
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "SELECT * FROM users WHERE role != 'ADMIN'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String dbRole = rs.getString("role");
                if ("STUDENT".equalsIgnoreCase(dbRole)) {
                    String query2 = "SELECT * FROM students WHERE id = ?";
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ps2.setLong(1, rs.getLong("id"));
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        members.add(new Student(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            dbRole,
                            rs2.getInt("age"),
                            rs2.getString("address"),
                            rs2.getString("parent_email"),
                            rs2.getLong("class_id")
                    ));
                    }
                } else if ("TEACHER".equalsIgnoreCase(dbRole)) {
                    String query2 = "SELECT * FROM teachers WHERE id = ?";
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ps2.setLong(1, rs.getLong("id"));
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        members.add(new Teacher(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            dbRole,
                            rs2.getString("phone_number"),
                            rs2.getLong("subject_id"),
                            rs2.getLong("class_id")
                    ));
                }
                } else if ("PARENT".equalsIgnoreCase(dbRole)) {
                    String query2 = "SELECT * FROM parents WHERE id = ?";
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ps2.setLong(1, rs.getLong("id"));
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        members.add(new Parent(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            dbRole,
                            rs2.getString("mobile_number"),
                            rs2.getString("address"),
                            rs2.getInt("age")
                    ));
                }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return members;
    }
    
    public boolean updateMember(User user) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "UPDATE users SET name=?, email=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setLong(3, user.getId());
            
            boolean res = ps.executeUpdate() > 0;
            if (!res) {
                return false;
            }
            
            if (user instanceof Student) {
                Student s = (Student) user;
                String query2 = "UPDATE students SET age=?, address=?, parent_email=?, class_id=? WHERE id=?";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ps2.setInt(1, s.getAge());
                ps2.setString(2, s.getAddress());
                ps2.setString(3, s.getParentEmail());
                ps2.setLong(4, s.getClassId());
                ps2.setLong(5, s.getId());
                return ps2.executeUpdate() > 0;
            } else if (user instanceof Teacher) {
                Teacher t = (Teacher) user;
                String query2 = "UPDATE teachers SET phone_number=?, subject_id=?, class_id=? WHERE id=?";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ps2.setString(1, t.getPhoneNumber());
                ps2.setLong(2, t.getSubjectId());
                ps2.setLong(3, t.getClassId());
                ps2.setLong(4, t.getId());
                return ps2.executeUpdate() > 0;
            } else if (user instanceof Parent) {
                Parent p = (Parent) user;
                String query2 = "UPDATE parents SET age=?, address=?, mobile_number=? WHERE id=?";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ps2.setInt(1, p.getAge());
                ps2.setString(2, p.getAddress());
                ps2.setString(3, p.getMobileNumber());
                ps2.setLong(4, p.getId());
                return ps2.executeUpdate() > 0;
            }
            
            return res;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteMember(Long userId) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "DELETE FROM users WHERE id=? AND role != 'ADMIN'";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, userId);
            
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Cannot delete Member! They are referenced elsewhere in the system.");
            return false;
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }
}
