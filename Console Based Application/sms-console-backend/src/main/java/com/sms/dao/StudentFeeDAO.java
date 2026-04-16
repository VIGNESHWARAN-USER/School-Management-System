package com.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sms.entities.StudentFee;
import com.sms.util.DatabaseConfig;

public class StudentFeeDAO {

    public boolean allocateFeeToStudents(long feeStructureId, long classRoomId) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String getStudents = "SELECT id FROM students WHERE class_id = ?";
            PreparedStatement ps = con.prepareStatement(getStudents);
            ps.setLong(1, classRoomId);
            ResultSet rs = ps.executeQuery();

            String insertFee = "INSERT INTO student_fees (student_id, fee_structure_id, status) VALUES (?, ?, 'UNPAID')";
            PreparedStatement psInsert = con.prepareStatement(insertFee);
            
            boolean hasStudent = false;
            while (rs.next()) {
                hasStudent = true;
                psInsert.setLong(1, rs.getLong("id"));
                psInsert.setLong(2, feeStructureId);
                psInsert.addBatch();
            }
            if (hasStudent) {
                psInsert.executeBatch();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<StudentFee> getFeesForStudent(long studentId) {
        List<StudentFee> list = new ArrayList<>();
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "SELECT * FROM student_fees WHERE student_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long payId = rs.getLong("payment_id");
                Long paymentId = rs.wasNull() ? null : payId;
                list.add(new StudentFee(
                    rs.getLong("id"),
                    rs.getLong("student_id"),
                    rs.getLong("fee_structure_id"),
                    rs.getString("status"),
                    paymentId
                ));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean updateFeeStatus(long studentFeeId, String status, long paymentId) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "UPDATE student_fees SET status = ?, payment_id = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, status);
            ps.setLong(2, paymentId);
            ps.setLong(3, studentFeeId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
