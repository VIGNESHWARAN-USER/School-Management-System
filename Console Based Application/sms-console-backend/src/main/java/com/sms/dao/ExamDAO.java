package com.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sms.entities.Exam;
import com.sms.util.DatabaseConfig;

public class ExamDAO {

    public boolean addExam(Exam exam) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "INSERT INTO exams (name, description, subject_id, class_id, exam_date, max_marks) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, exam.getName());
            ps.setString(2, exam.getDescription());
            ps.setLong(3, exam.getSubjectId());
            ps.setLong(4, exam.getClassRoomId());
            ps.setString(5, exam.getExamDate());
            ps.setDouble(6, exam.getMaxMarks());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateExam(Exam exam) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "UPDATE exams SET name=?, description=?, subject_id=?, class_id=?, exam_date=?, max_marks=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, exam.getName());
            ps.setString(2, exam.getDescription());
            ps.setLong(3, exam.getSubjectId());
            ps.setLong(4, exam.getClassRoomId());
            ps.setString(5, exam.getExamDate());
            ps.setDouble(6, exam.getMaxMarks());
            ps.setLong(7, exam.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteExam(long examId) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "DELETE FROM exams WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, examId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error deleting exam: " + e.getMessage());
            return false;
        }
    }

    public List<Exam> getAllExams() {
        List<Exam> exams = new ArrayList<>();
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "SELECT * FROM exams";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                exams.add(new Exam(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getLong("subject_id"),
                    rs.getLong("class_id"),
                    rs.getString("exam_date"),
                    rs.getDouble("max_marks")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exams;
    }

    public List<Exam> getExamsByClass(long classId) {
        List<Exam> exams = new ArrayList<>();
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "SELECT * FROM exams WHERE class_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, classId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                exams.add(new Exam(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getLong("subject_id"),
                    rs.getLong("class_id"),
                    rs.getString("exam_date"),
                    rs.getDouble("max_marks")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exams;
    }

    public Exam getExamById(long examId) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "SELECT * FROM exams WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, examId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Exam(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getLong("subject_id"),
                    rs.getLong("class_id"),
                    rs.getString("exam_date"),
                    rs.getDouble("max_marks")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
