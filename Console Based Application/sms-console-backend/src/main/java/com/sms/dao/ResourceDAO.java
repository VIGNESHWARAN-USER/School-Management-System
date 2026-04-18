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

    public boolean addClassRoom(ClassRoom classRoom) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "INSERT INTO classrooms (class_name, section, capacity, academic_year) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, classRoom.getClassName());
            ps.setString(2, classRoom.getSection());
            ps.setInt(3, classRoom.getCapacity());
            ps.setString(4, classRoom.getAcademicYear());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteClassRoom(long id) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "DELETE FROM classrooms WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, id);

            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Cannot delete ClassRoom! It is currently in use by Subjects or Students.");
            return false;
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    public boolean isClassRoomExists(long id) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "SELECT 1 FROM classrooms WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public boolean addSubject(Subject subject) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "INSERT INTO subjects (subject_name, subject_code, class_id) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, subject.getSubjectName());
            ps.setString(2, subject.getSubjectCode());
            ps.setLong(3, subject.getClassId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSubject(long subjectId) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "DELETE FROM subjects WHERE subject_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, subjectId);

            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Cannot delete Subject! It may be referenced by Teachers or Exams.");
            return false;
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "SELECT * FROM subjects";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
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
