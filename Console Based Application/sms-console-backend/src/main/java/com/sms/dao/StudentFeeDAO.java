package com.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sms.entities.StudentFee;
import com.sms.util.DatabaseConfig;

public class StudentFeeDAO {

    // Allocate fee structure to all students of a specific classroom
    public boolean allocateFeeToStudents(long feeStructureId, long classRoomId) {
        try {
            Connection con = DatabaseConfig.getConnection(); //  DB connection

            // fetch all students in the given classroom
            String getStudents = "SELECT id FROM students WHERE class_id = ?";
            PreparedStatement ps = con.prepareStatement(getStudents);
            ps.setLong(1, classRoomId);
            ResultSet rs = ps.executeQuery();

            // insert fee records for each student
            String insertFee = "INSERT INTO student_fees (student_id, fee_structure_id, status) VALUES (?, ?, 'UNPAID')";
            PreparedStatement psInsert = con.prepareStatement(insertFee);
            
            boolean hasStudent = false;

            // loop through students and prepare batch insert
            while (rs.next()) {
                hasStudent = true;
                psInsert.setLong(1, rs.getLong("id"));
                psInsert.setLong(2, feeStructureId);
                psInsert.addBatch(); // add to batch
            }

            // execute batch only if students exist
            if (hasStudent) {
                psInsert.executeBatch();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace(); // print error
            return false;
        }
    }

    // Get all fee records for a specific student
    public List<StudentFee> getFeesForStudent(long studentId) {
        List<StudentFee> list = new ArrayList<>();

        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "SELECT * FROM student_fees WHERE student_id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setLong(1, studentId);
            ResultSet rs = ps.executeQuery();

            // convert each row into StudentFee object
            while (rs.next()) {

                // handle nullable payment_id
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
    
    // Update fee status after payment
    public boolean updateFeeStatus(long studentFeeId, String status, long paymentId) {
        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "UPDATE student_fees SET status = ?, payment_id = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, status);       // update status (PAID/UNPAID)
            ps.setLong(2, paymentId);     // link payment id
            ps.setLong(3, studentFeeId);  // specific record

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}