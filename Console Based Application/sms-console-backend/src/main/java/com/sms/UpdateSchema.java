package com.sms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class UpdateSchema {
    public static void main(String[] args) {
        String url = "jdbc:mysql://erp-vigneshwaran-6b62.k.aivencloud.com:16990/sms_console";
        String user = "avnadmin";
        String pass = "AVNS_pglAGGn58sK9XJ7RYqD";

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            try (Statement stmt = con.createStatement()) {
                String parentChildrenTable = "CREATE TABLE IF NOT EXISTS parent_children (" +
                                             "parent_id BIGINT, " +
                                             "student_id BIGINT, " +
                                             "PRIMARY KEY (parent_id, student_id), " +
                                             "FOREIGN KEY (parent_id) REFERENCES parents(id), " +
                                             "FOREIGN KEY (student_id) REFERENCES students(id)" +
                                             ")";
                stmt.execute(parentChildrenTable);
                System.out.println("Created parent_children table.");

                String studentFeesTable = "CREATE TABLE IF NOT EXISTS student_fees (" +
                                          "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                                          "student_id BIGINT, " +
                                          "fee_structure_id BIGINT, " +
                                          "status VARCHAR(50) DEFAULT 'UNPAID', " +
                                          "payment_id BIGINT NULL, " +
                                          "FOREIGN KEY (student_id) REFERENCES students(id), " +
                                          "FOREIGN KEY (fee_structure_id) REFERENCES fee_structures(id), " +
                                          "FOREIGN KEY (payment_id) REFERENCES payments(id)" +
                                          ")";
                stmt.execute(studentFeesTable);
                System.out.println("Created student_fees table.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
