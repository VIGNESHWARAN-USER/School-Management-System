package com.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sms.entities.ClassRoom;
import com.sms.entities.Subject;
import com.sms.util.DatabaseConfig;

public class ResourceDAO {

    // Add new classroom to database
    public boolean addClassRoom(ClassRoom classRoom) {
        try {
            Connection con = DatabaseConfig.getConnection(); // get DB connection

            String query = "INSERT INTO classrooms (class_name, section, capacity, academic_year) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            // set values
            ps.setString(1, classRoom.getClassName());
            ps.setString(2, classRoom.getSection());
            ps.setInt(3, classRoom.getCapacity());
            ps.setString(4, classRoom.getAcademicYear());

            return ps.executeUpdate() > 0; // execute insert

        } catch (Exception e) {
            e.printStackTrace(); // print error
            return false;
        }
    }

    // Delete classroom using id
    public boolean deleteClassRoom(long id) {
        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "DELETE FROM classrooms WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            // handle foreign key constraint (classroom used elsewhere)
            System.out.println("Error: Cannot delete ClassRoom! It is currently in use by Subjects or Students.");
            return false;

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    // Check if classroom exists in Database
    public boolean isClassRoomExists(long id) {
        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "SELECT 1 FROM classrooms WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // returns true if exists

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all classrooms
    public List<ClassRoom> getAllClassRooms() {
        List<ClassRoom> list = new ArrayList<>();

        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "SELECT * FROM classrooms";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

           
            while (rs.next()) {
                list.add(new ClassRoom(
                    rs.getLong("id"),
                    rs.getString("class_name"),
                    rs.getString("section"),
                    rs.getInt("capacity"),
                    rs.getString("academic_year")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Add new subject
    public boolean addSubject(Subject subject) {
        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "INSERT INTO subjects (subject_name, subject_code, class_id) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            // set values
            ps.setString(1, subject.getSubjectName());
            ps.setString(2, subject.getSubjectCode());
            ps.setLong(3, subject.getClassId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete subject using id
    public boolean deleteSubject(long subjectId) {
        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "DELETE FROM subjects WHERE subject_id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setLong(1, subjectId);

            return ps.executeUpdate() > 0;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            // subject linked with teacher/exam
            System.out.println("Error: Cannot delete Subject! It may be referenced by Teachers or Exams.");
            return false;

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    // Get all subjects
    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();

        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "SELECT * FROM subjects";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            // convert rows into Subject objects
            while (rs.next()) {
                list.add(new Subject(
                    rs.getLong("subject_id"),
                    rs.getString("subject_name"),
                    rs.getString("subject_code"),
                    rs.getLong("class_id")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}