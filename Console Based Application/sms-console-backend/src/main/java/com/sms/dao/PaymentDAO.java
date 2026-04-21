package com.sms.dao;

//Author: Reshma K

/*
* This class for the query logic of a payment based on create, update, select and delete payment
*/

import com.sms.util.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class PaymentDAO {

	// Create a payment record and return generated payment ID
	public long createPayment(double amountPaid, String paymentMethod, String remarks) {
		try {
			Connection con = DatabaseConfig.getConnection(); // DB connection

			// insert payment details into payments table
			String query = "INSERT INTO payments (amount_paid, payment_date, status, remarks, payment_method) VALUES (?, ?, 'SUCCESS', ?, ?)";
			PreparedStatement ps = con.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);

			// set values
			ps.setDouble(1, amountPaid);
			ps.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // current date & time
			ps.setString(3, remarks);
			ps.setString(4, paymentMethod);

			int res = ps.executeUpdate(); // execute insert

			// get generated payment ID
			if (res > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					return rs.getLong(1); // return payment id
				}
			}

		} catch (Exception e) {
			e.printStackTrace(); // print error
		}

		return -1; // return -1 if failed
	}
}