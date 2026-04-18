package com.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sms.entities.FeeStructure;
import com.sms.util.DatabaseConfig;

public class FeeStructureDAO {

    // Add new fee structure into database
    public boolean addFeeStructure(FeeStructure feeStructure) {
        try {
            Connection con = DatabaseConfig.getConnection(); // get DB connection

            String query = "INSERT INTO fee_structures (class_room_id, total_amount, description, term) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);

            // set values
            ps.setLong(1, feeStructure.getClassRoomId());
            ps.setDouble(2, feeStructure.getTotalAmount());
            ps.setString(3, feeStructure.getDescription());
            ps.setString(4, feeStructure.getTerm());

            int res = ps.executeUpdate(); // execute insert

            if (res > 0) {
                // get generated ID of new fee structure
                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    long newId = rs.getLong(1);

                    // allocate  fee to all students of that class
                    new StudentFeeDAO().allocateFeeToStudents(newId, feeStructure.getClassRoomId());
                }
                return true;
            }

            return false;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
           
            System.out.println("Error: ClassRoom does not exist or invalid reference.");
            return false;

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    // Update existing fee structure
    public boolean updateFeeStructure(FeeStructure feeStructure) {
        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "UPDATE fee_structures SET class_room_id = ?, total_amount = ?, description = ?, term = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            // set updated values
            ps.setLong(1, feeStructure.getClassRoomId());
            ps.setDouble(2, feeStructure.getTotalAmount());
            ps.setString(3, feeStructure.getDescription());
            ps.setString(4, feeStructure.getTerm());
            ps.setLong(5, feeStructure.getId());

            return ps.executeUpdate() > 0;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            // invalid classroom reference
            System.out.println("Error: ClassRoom does not exist or invalid reference.");
            return false;

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    // Delete fee structure by ID
    public boolean deleteFeeStructure(long id) {
        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "DELETE FROM fee_structures WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            // cannot delete if linked with transactions or student fees
            System.out.println("Error: Cannot delete Fee Structure! It is currently tied to transactions or external references.");
            return false;

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    // Get all fee structures from database
    public List<FeeStructure> getAllFeeStructures() {
        List<FeeStructure> list = new ArrayList<>(); // store results

        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "SELECT * FROM fee_structures";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            // get result and convert to objects
            while (rs.next()) {
                list.add(new FeeStructure(
                        rs.getLong("id"),
                        rs.getLong("class_room_id"),
                        rs.getDouble("total_amount"),
                        rs.getString("description"),
                        rs.getString("term")
                ));
            }

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
        }

        return list;
    }
}