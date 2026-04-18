package com.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sms.entities.Grade;
import com.sms.util.DatabaseConfig;

public class GradeDAO {

    public boolean upsertGrade(Grade grade) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "INSERT INTO grades (student_id, exam_id, marks_obtained, letter_grade, remarks) " +
                           "VALUES (?, ?, ?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE marks_obtained=?, letter_grade=?, remarks=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, grade.getStudentId());
            ps.setLong(2, grade.getExamId());
            ps.setDouble(3, grade.getMarksObtained());
            ps.setString(4, grade.getLetterGrade());
            ps.setString(5, grade.getRemarks());
            
            ps.setDouble(6, grade.getMarksObtained());
            ps.setString(7, grade.getLetterGrade());
            ps.setString(8, grade.getRemarks());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Grade> getGradesByStudent(long studentId) {
        List<Grade> grades = new ArrayList<>();
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "SELECT * FROM grades WHERE student_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                grades.add(new Grade(
                    rs.getLong("id"),
                    rs.getLong("student_id"),
                    rs.getLong("exam_id"),
                    rs.getDouble("marks_obtained"),
                    rs.getString("letter_grade"),
                    rs.getString("remarks")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    public List<Grade> getGradesByExam(long examId) {
        List<Grade> grades = new ArrayList<>();
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "SELECT * FROM grades WHERE exam_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, examId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                grades.add(new Grade(
                    rs.getLong("id"),
                    rs.getLong("student_id"),
                    rs.getLong("exam_id"),
                    rs.getDouble("marks_obtained"),
                    rs.getString("letter_grade"),
                    rs.getString("remarks")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    public Grade getGradeForStudent(long studentId, long examId) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "SELECT * FROM grades WHERE student_id=? AND exam_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, studentId);
            ps.setLong(2, examId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Grade(
                    rs.getLong("id"),
                    rs.getLong("student_id"),
                    rs.getLong("exam_id"),
                    rs.getDouble("marks_obtained"),
                    rs.getString("letter_grade"),
                    rs.getString("remarks")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
