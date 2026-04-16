package com.sms.dao;

import com.sms.util.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class PaymentDAO {
    
    public long createPayment(double amountPaid, String paymentMethod, String remarks) {
        try {
            Connection con = DatabaseConfig.getConnection();
            String query = "INSERT INTO payments (amount_paid, payment_date, status, remarks, payment_method) VALUES (?, ?, 'SUCCESS', ?, ?)";
            PreparedStatement ps = con.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, amountPaid);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setString(3, remarks);
            ps.setString(4, paymentMethod);
            
            int res = ps.executeUpdate();
            if (res > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
